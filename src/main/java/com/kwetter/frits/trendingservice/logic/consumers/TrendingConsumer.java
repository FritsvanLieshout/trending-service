package com.kwetter.frits.trendingservice.logic.consumers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwetter.frits.trendingservice.configuration.KafkaProperties;
import com.kwetter.frits.trendingservice.entity.Trending;
import com.kwetter.frits.trendingservice.logic.dto.TrendingDTO;
import com.kwetter.frits.trendingservice.repository.TrendingRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TrendingConsumer {

    private final Logger log = LoggerFactory.getLogger(TrendingConsumer.class);
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final KafkaProperties kafkaProperties;

    public static final String TOPIC = "trending-item-added";

    private KafkaConsumer<String, String> kafkaConsumer;
    private TrendingRepository trendingRepository;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public TrendingConsumer(KafkaProperties kafkaProperties, TrendingRepository trendingRepository) {
        this.kafkaProperties = kafkaProperties;
        this.trendingRepository = trendingRepository;
    }

    @PostConstruct
    public void start() {

        log.info("Kafka consumer starting...");
        this.kafkaConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        kafkaConsumer.subscribe(Collections.singletonList(TOPIC));
        log.info("Kafka consumer started");

        executorService.execute(() -> {
            try {
                while (!closed.get()) {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));
                    for (ConsumerRecord<String, String> record : records) {
                        log.info("Consumed message in {} : {}", TOPIC, record.value());

                        var objectMapper = new ObjectMapper();
                        var trendingDTO = objectMapper.readValue(record.value(), TrendingDTO.class);
                        if (trendingDTO != null && !trendingDTO.getTrends().isEmpty()) {
                            for (var trend : trendingDTO.getTrends()) {
                                var trending = trendingRepository.findTrendingByTrend(trend);
                                if (trending != null) {
                                    trending.setCount(trending.getCount() + 1);
                                    trendingRepository.save(trending);
                                }
                                else {
                                    var currentDateTime = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
                                    trendingRepository.save(new Trending(trend, 1, currentDateTime));
                                }
                            }
                        }
                    }
                }
                kafkaConsumer.commitSync();
            } catch (WakeupException e) {
                if (!closed.get()) throw e;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                log.info("Kafka consumer close");
                kafkaConsumer.close();
            }
        });
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void shutdown() {
        log.info("Shutdown Kafka consumer");
        closed.set(true);
        kafkaConsumer.wakeup();
    }
}

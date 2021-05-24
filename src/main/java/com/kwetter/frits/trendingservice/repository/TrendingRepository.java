package com.kwetter.frits.trendingservice.repository;

import com.kwetter.frits.trendingservice.entity.Trending;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TrendingRepository extends MongoRepository<Trending, String> {
    List<Trending> findTop5ByOrderByCountDesc();
    Trending findTrendingByTrend(String trend);
}

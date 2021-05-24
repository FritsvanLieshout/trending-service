package com.kwetter.frits.trendingservice.repository;

import com.kwetter.frits.trendingservice.entity.Trending;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrendingRepository extends MongoRepository<Trending, String> {
    List<Trending> findTop5ByOrderByCountDesc();
    Trending findTrendingByTrend(String trend);
}

package com.kwetter.frits.trendingservice.interfaces;

import com.kwetter.frits.trendingservice.entity.Trending;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface TrendingLogic {
    List<Trending> getTop5TrendingItems();
}

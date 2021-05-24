package com.kwetter.frits.trendingservice.logic;

import com.kwetter.frits.trendingservice.entity.Trending;
import com.kwetter.frits.trendingservice.interfaces.TrendingLogic;
import com.kwetter.frits.trendingservice.repository.TrendingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrendingLogicImpl implements TrendingLogic {

    private final TrendingRepository trendingRepository;

    public TrendingLogicImpl(TrendingRepository trendingRepository) { this.trendingRepository = trendingRepository; }

    @Override
    public List<Trending> getTop5TrendingItems() {
       return trendingRepository.findTop5ByOrderByCountDesc();
    }
}

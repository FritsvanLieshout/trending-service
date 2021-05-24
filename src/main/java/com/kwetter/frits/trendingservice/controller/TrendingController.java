package com.kwetter.frits.trendingservice.controller;

import com.kwetter.frits.trendingservice.entity.Trending;
import com.kwetter.frits.trendingservice.logic.TrendingLogicImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/trending")
public class TrendingController {

    private final TrendingLogicImpl trendingLogic;

    public TrendingController(TrendingLogicImpl trendingLogic) { this.trendingLogic = trendingLogic; }

    @GetMapping("/items")
    public ResponseEntity<List<Trending>> getTrendingItems() {
        try {
            var trends = new ArrayList<>(trendingLogic.getTop5TrendingItems());
            if (trends.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(trends, HttpStatus.OK);
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

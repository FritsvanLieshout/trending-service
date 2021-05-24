package com.kwetter.frits.trendingservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trending")
public class Trending {

    @Id
    private String id;

    private String trend;
    private int count;
    private String createdAt;

    public Trending() {}

    public Trending(String trend, int count, String createdAt) {
        this.trend = trend;
        this.count = count;
        this.createdAt = createdAt;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTrend() { return trend; }

    public void setTrend(String trend) { this.trend = trend; }

    public int getCount() { return count; }

    public void setCount(int count) { this.count = count; }

    public String getCreatedAt() { return createdAt; }

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}

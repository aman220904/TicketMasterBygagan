package com.example.ticketmaster;

import java.io.Serializable;

public class Event implements Serializable {
    private String eventName;
    private String startDate;
    private String priceRange;
    private String url;
    private String imageUrl;

    public Event(String eventName, String startDate, String priceRange, String url, String imageUrl) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.priceRange = priceRange;
        this.url = url;
        this.imageUrl = imageUrl;
    }

    public String getEventName() {
        return eventName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public String getUrl() {
        return url;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

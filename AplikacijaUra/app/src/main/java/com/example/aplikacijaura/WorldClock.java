package com.example.aplikacijaura;

public class WorldClock {
    private final String cityName;
    private final String timeZoneId;

    public WorldClock(String cityName, String timeZoneId) {
        this.cityName = cityName;
        this.timeZoneId = timeZoneId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }
}

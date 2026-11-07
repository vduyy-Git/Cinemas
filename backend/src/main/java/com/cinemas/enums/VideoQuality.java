package com.cinemas.enums;

public enum VideoQuality {
    P360("360p"),
    P480("480p"),
    P720("720p"),
    P1080("1080p"),
    K2("2k"),
    K4("4k");

    private final String databaseValue;

    VideoQuality(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public String getDatabaseValue() {
        return databaseValue;
    }

    public static VideoQuality fromDatabaseValue(String value) {
        for (VideoQuality quality : values()) {
            if (quality.databaseValue.equals(value)) {
                return quality;
            }
        }
        throw new IllegalArgumentException("Unknown video quality: " + value);
    }
}


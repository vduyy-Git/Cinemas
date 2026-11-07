package com.cinemas.converter;

import com.cinemas.enums.VideoQuality;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
@Converter
public class VideoQualityConverter implements AttributeConverter<VideoQuality, String> {
    @Override public String convertToDatabaseColumn(VideoQuality value) {
        return value == null ? null : value.getDatabaseValue();
    }
    @Override public VideoQuality convertToEntityAttribute(String value) {
        return value == null ? null : VideoQuality.fromDatabaseValue(value);
    }
}



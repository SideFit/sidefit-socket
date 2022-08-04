package com.project.sidefitrsocket.util;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class DateUtil {

    public Long localDateTimeToUnixTime(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public LocalDateTime unixTimeToLocalDateTime(Long unixTime) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(unixTime), ZoneId.systemDefault());
    }
}

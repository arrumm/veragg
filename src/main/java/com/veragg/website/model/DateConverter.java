package com.veragg.website.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DateConverter {
    private DateConverter() {
    }

    public static long dateToTimestamp(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime).getTime();
    }
}

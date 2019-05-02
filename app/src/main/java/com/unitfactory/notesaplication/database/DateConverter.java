package com.unitfactory.notesaplication.database;

import androidx.room.TypeConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateConverter {
    private static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Kyiv"));

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    public static String getFormattedDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(date);
        return dateFormat.format(calendar.getTime());

    }

    public static String getFormattedTime(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        calendar.setTime(date);
        return timeFormat.format(calendar.getTime());
    }
}

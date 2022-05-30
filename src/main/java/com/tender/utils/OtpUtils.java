package com.tender.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class OtpUtils {

    public static String get6DigitOtp() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public static long getPastSeconds(LocalDateTime createdAt) {
        ZonedDateTime createdAtZoned = ZonedDateTime.of(createdAt, ZoneId.systemDefault());
        ZonedDateTime currentDateTime = ZonedDateTime.now();
        return ChronoUnit.SECONDS.between(currentDateTime, createdAtZoned);
    }

    private OtpUtils() {}
}

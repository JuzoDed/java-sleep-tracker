package ru.yandex.practicum.sleeptracker;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class SleepingSession {
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final SleepQuality sleepQuality;

    private static final int HOUR_23 = 23;
    private static final int HOUR_22 = 22;
    private static final int HOUR_9 = 9;
    private static final int HOUR_7 = 7;
    private static final int MINUTE_0 = 0;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public SleepingSession(String startTime, String startDate, String endTime, String endDate, String sleepQuality) {
        LocalDate startDateParsed = LocalDate.parse(startDate, DATE_FORMATTER);
        LocalTime startTimeParsed = LocalTime.parse(startTime, TIME_FORMATTER);
        LocalDate endDateParsed = LocalDate.parse(endDate, DATE_FORMATTER);
        LocalTime endTimeParsed = LocalTime.parse(endTime, TIME_FORMATTER);

        this.startDateTime = LocalDateTime.of(startDateParsed, startTimeParsed);
        this.endDateTime = LocalDateTime.of(endDateParsed, endTimeParsed);
        this.sleepQuality = SleepQuality.valueOf(sleepQuality);

        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("Дата окончания не может быть раньше даты начала");
        }
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public LocalDate getStartDate() {
        return startDateTime.toLocalDate();
    }

    public LocalTime getStartTime() {
        return startDateTime.toLocalTime();
    }

    public LocalTime getEndTime() {
        return endDateTime.toLocalTime();
    }

    public LocalDate getEndDate() {
        return endDateTime.toLocalDate();
    }

    public SleepQuality getSleepQuality() {
        return sleepQuality;
    }

    public long getSleepDuration() {
        return Duration.between(startDateTime, endDateTime).toMinutes();
    }

    public boolean isNightSleep() {
        LocalTime startTime = getStartTime();
        LocalTime endTime = getEndTime();
        LocalDate startDate = getStartDate();
        LocalDate endDate = getEndDate();

        if (startDate.equals(endDate)) {
            return startTime.isBefore(LocalTime.of(6, 0)) &&
                    endTime.isAfter(LocalTime.MIDNIGHT);
        } else {
            return true;
        }
    }

    public Chronotype getChronotypeForNight() {
        if (!isNightSleep()) {
            return null;
        }

        LocalTime startTime = getStartTime();
        LocalTime endTime = getEndTime();

        if (startTime.isAfter(LocalTime.of(HOUR_23, MINUTE_0)) &&
                endTime.isAfter(LocalTime.of(HOUR_9, MINUTE_0))) {
            return Chronotype.OWL;
        } else if (startTime.isBefore(LocalTime.of(HOUR_22, MINUTE_0)) &&
                endTime.isBefore(LocalTime.of(HOUR_7, MINUTE_0))) {
            return Chronotype.LARK;
        } else {
            return Chronotype.DOVE;
        }
    }

    @Override
    public String toString() {
        return startDateTime.format(DATE_TIME_FORMATTER) + " - " +
                endDateTime.format(DATE_TIME_FORMATTER) + " (" + sleepQuality + ")";
    }
}
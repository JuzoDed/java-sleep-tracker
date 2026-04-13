package ru.yandex.practicum.sleeptracker.structures;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class SleepingSession {
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;
    private final SleepQuality sleepQuality;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private static final LocalTime NIGHT_START = LocalTime.MIDNIGHT;
    private static final LocalTime NIGHT_END = LocalTime.of(6, 0);

    private static final LocalTime LARK_SLEEP_START = LocalTime.of(22, 0);
    private static final LocalTime LARK_WAKE_END = LocalTime.of(7, 0);
    private static final LocalTime OWL_SLEEP_START = LocalTime.of(23, 0);
    private static final LocalTime OWL_WAKE_END = LocalTime.of(9, 0);

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
        LocalDate currentDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();

        while (!currentDate.isAfter(endDate)) {
            LocalDateTime nightStart = currentDate.atTime(NIGHT_START);
            LocalDateTime nightEnd = currentDate.atTime(NIGHT_END);

            if (endDateTime.isAfter(nightStart) && startDateTime.isBefore(nightEnd)) {
                return true;
            }

            currentDate = currentDate.plusDays(1);
        }

        return false;
    }

    public Chronotype getChronotypeForNight() {
        if (!isNightSleep()) {
            return null;
        }

        LocalTime startTime = getStartTime();
        LocalTime endTime = getEndTime();

        boolean isOwl = !startTime.isBefore(OWL_SLEEP_START) && !endTime.isBefore(OWL_WAKE_END);
        boolean isLark = startTime.isBefore(LARK_SLEEP_START) && endTime.isBefore(LARK_WAKE_END);

        if (isOwl) {
            return Chronotype.OWL;
        } else if (isLark) {
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
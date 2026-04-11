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
        LocalDateTime nightWindowStart = startDateTime.with(LocalTime.MIDNIGHT);
        LocalDateTime nightWindowEnd = startDateTime.with(LocalTime.of(6, 0));

        if (startDateTime.toLocalTime().isAfter(LocalTime.of(6, 0))) {
            nightWindowStart = nightWindowStart.plusDays(1);
            nightWindowEnd = nightWindowEnd.plusDays(1);
        }

        return !endDateTime.isBefore(nightWindowStart) && !startDateTime.isAfter(nightWindowEnd);
    }

    public Chronotype getChronotypeForNight() {
        if (!isNightSleep()) {
            return null;
        }

        LocalDateTime midPoint = startDateTime.plusSeconds(
                Duration.between(startDateTime, endDateTime).getSeconds() / 2
        );
        LocalTime midTime = midPoint.toLocalTime();

        if (midTime.isAfter(LocalTime.of(3, 0)) && midTime.isBefore(LocalTime.of(6, 0))) {
            return Chronotype.OWL;
        } else if (midTime.isAfter(LocalTime.of(22, 0)) || midTime.isBefore(LocalTime.of(3, 0))) {
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
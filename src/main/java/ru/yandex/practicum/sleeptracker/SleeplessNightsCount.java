package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SleeplessNightsCount implements SleepFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        LocalDate lastDate = sessions.stream()
                .map(SleepingSession::getEndDate)
                .max(LocalDate::compareTo)
                .orElseThrow();

        LocalDate startNight = calculateStartNight(sessions.get(0));

        long totalNights = lastDate.toEpochDay() - startNight.toEpochDay() + 1;

        long nightsWithSleep = sessions.stream()
                .filter(SleepingSession::isNightSleep)
                .map(this::getNightDate)
                .distinct()
                .count();

        long sleeplessNights = Math.max(0, totalNights - nightsWithSleep);

        return new SleepAnalysisResult("Количество бессонных ночей", sleeplessNights);
    }

    private LocalDate calculateStartNight(SleepingSession firstSession) {

        LocalDate sessionDate = firstSession.getStartDate();
        LocalTime sessionTime = firstSession.getStartTime();

        return sessionTime.isBefore(LocalTime.NOON)
                ? sessionDate.minusDays(1)
                : sessionDate;
    }

    private LocalDate getNightDate(SleepingSession session) {

        LocalDate sessionDate = session.getStartDate();
        LocalTime sessionTime = session.getStartTime();

        return sessionTime.isAfter(LocalTime.NOON)
                ? sessionDate
                : sessionDate.minusDays(1);
    }
}
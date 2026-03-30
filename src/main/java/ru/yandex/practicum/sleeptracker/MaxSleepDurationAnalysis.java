package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MaxSleepDurationAnalysis implements SleepFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        long maxDuration = sessions.stream()
                .mapToLong(SleepingSession::getSleepDuration)
                .max()
                .orElse(0);

        return new SleepAnalysisResult(
                "Максимальная продолжительность сна (минут)",
                maxDuration
        );
    }
}
package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class MinSleepDurationAnalysis implements SleepFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        long minDuration = sessions.stream()
                .mapToLong(SleepingSession::getSleepDuration)
                .min()
                .orElse(0);

        return new SleepAnalysisResult(
                "Минимальная продолжительность сна (минут)",
                minDuration
        );
    }
}
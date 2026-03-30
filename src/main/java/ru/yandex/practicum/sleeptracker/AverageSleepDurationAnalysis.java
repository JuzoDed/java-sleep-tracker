package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class AverageSleepDurationAnalysis implements SleepFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        double avgDuration = sessions.stream()
                .mapToLong(SleepingSession::getSleepDuration)
                .average()
                .orElse(0.0);

        return new SleepAnalysisResult(
                "Средняя продолжительность сна (минут)",
                String.format("%.1f", avgDuration)
        );
    }
}
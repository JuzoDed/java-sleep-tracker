package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.structures.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.structures.SleepingSession;

import java.util.List;

public class AverageSleepDurationAnalysis implements SleepFunction {

    private static final String DESCRIPTION = "Средняя продолжительность сна (минут)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        double avgDuration = sessions.stream()
                .mapToLong(SleepingSession::getSleepDuration)
                .average()
                .orElse(0.0);
        return new SleepAnalysisResult(DESCRIPTION, String.format("%.1f", avgDuration));
    }
}
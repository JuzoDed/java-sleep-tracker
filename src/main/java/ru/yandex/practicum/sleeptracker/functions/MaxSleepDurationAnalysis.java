package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.structures.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.structures.SleepingSession;

import java.util.List;

public class MaxSleepDurationAnalysis implements SleepFunction {

    private static final String DESCRIPTION = "Максимальная продолжительность сна (минут)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long maxDuration = sessions.stream()
                .mapToLong(SleepingSession::getSleepDuration)
                .max()
                .orElse(0);
        return new SleepAnalysisResult(DESCRIPTION, maxDuration);
    }
}
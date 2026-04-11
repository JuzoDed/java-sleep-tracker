package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.structures.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.structures.SleepingSession;

import java.util.List;

public class MinSleepDurationAnalysis implements SleepFunction {

    private static final String DESCRIPTION = "Минимальная продолжительность сна (минут)";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long minDuration = sessions.stream()
                .mapToLong(SleepingSession::getSleepDuration)
                .min()
                .orElse(0);
        return new SleepAnalysisResult(DESCRIPTION, minDuration);
    }
}
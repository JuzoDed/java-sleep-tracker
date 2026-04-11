package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.structures.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.structures.SleepingSession;

import java.util.List;

public class TotalSessionsAnalysis implements SleepFunction {

    private static final String DESCRIPTION = "Общее количество сессий сна";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult(DESCRIPTION, sessions.size());
    }
}
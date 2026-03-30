package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class TotalSessionsAnalysis implements SleepFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult(
                "Общее количество сессий сна",
                sessions.size()
        );
    }
}
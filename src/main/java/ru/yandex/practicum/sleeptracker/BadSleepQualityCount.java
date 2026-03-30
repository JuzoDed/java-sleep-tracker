package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class BadSleepQualityCount implements SleepFunction {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        long badCount = sessions.stream()
                .filter(s -> s.getSleepQuality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult(
                "Количество сессий с плохим качеством сна",
                badCount
        );
    }
}
package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.structures.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.structures.SleepQuality;
import ru.yandex.practicum.sleeptracker.structures.SleepingSession;

import java.util.List;

public class BadSleepQualityCount implements SleepFunction {

    private static final String DESCRIPTION = "Количество сессий с плохим качеством сна";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long badCount = sessions.stream()
                .filter(s -> s.getSleepQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult(DESCRIPTION, badCount);
    }
}
package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.structures.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.structures.SleepingSession;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
public interface SleepFunction extends Function<List<SleepingSession>, SleepAnalysisResult> {
}
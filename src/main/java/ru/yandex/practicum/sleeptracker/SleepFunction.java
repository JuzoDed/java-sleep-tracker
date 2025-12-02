package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

@FunctionalInterface
interface SleepFunction extends Function<List<SleepingSession>, SleepAnalysisResult> {
}
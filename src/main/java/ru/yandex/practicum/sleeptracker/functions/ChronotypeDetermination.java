package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.structures.Chronotype;
import ru.yandex.practicum.sleeptracker.structures.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.structures.SleepingSession;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeDetermination implements SleepFunction {

    private static final String DESCRIPTION = "Хронотип пользователя";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> stats = sessions.stream()
                .map(SleepingSession::getChronotypeForNight)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        long max = stats.values().stream()
                .max(Long::compareTo)
                .orElse(0L);

        long leaders = stats.values().stream()
                .filter(v -> v == max)
                .count();

        Chronotype resultChronotype;

        if (leaders > 1) {
            resultChronotype = Chronotype.DOVE;
        } else {
            resultChronotype = stats.entrySet().stream()
                    .filter(e -> e.getValue() == max)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(Chronotype.DOVE);
        }

        String result = String.format(
                "%s (сов: %d, жаворонков: %d, голубей: %d)",
                resultChronotype.getRussianName(),
                stats.getOrDefault(Chronotype.OWL, 0L),
                stats.getOrDefault(Chronotype.LARK, 0L),
                stats.getOrDefault(Chronotype.DOVE, 0L)
        );

        return new SleepAnalysisResult(DESCRIPTION, result);
    }
}
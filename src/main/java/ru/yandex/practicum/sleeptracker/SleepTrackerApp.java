package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SleepTrackerApp {
    private final List<SleepFunction> analysisFunctions;
    private static final String DEFAULT_LOG_PATH = "C:\\Users\\user\\IdeaProjects\\java-sleep-tracker\\src\\main\\resources\\sleep_log.txt";

    public SleepTrackerApp() {
        analysisFunctions = new ArrayList<>();
        initializeFunctions();
    }

    private void initializeFunctions() {
        analysisFunctions.add(sessions ->
                new SleepAnalysisResult("Общее количество сессий сна", sessions.size()));

        analysisFunctions.add(sessions -> {
            long minDuration = sessions.stream()
                    .mapToLong(SleepingSession::getSleepDuration)
                    .min()
                    .orElse(0);
            return new SleepAnalysisResult("Минимальная продолжительность сна (минут)", minDuration);
        });

        analysisFunctions.add(sessions -> {
            long maxDuration = sessions.stream()
                    .mapToLong(SleepingSession::getSleepDuration)
                    .max()
                    .orElse(0);
            return new SleepAnalysisResult("Максимальная продолжительность сна (минут)", maxDuration);
        });

        analysisFunctions.add(sessions -> {
            double avgDuration = sessions.stream()
                    .mapToLong(SleepingSession::getSleepDuration)
                    .average()
                    .orElse(0.0);
            return new SleepAnalysisResult("Средняя продолжительность сна (минут)",
                    String.format("%.1f", avgDuration));
        });

        analysisFunctions.add(sessions -> {
            long badCount = sessions.stream()
                    .filter(s -> s.getSleepQuality() == SleepQuality.BAD)
                    .count();
            return new SleepAnalysisResult("Количество сессий с плохим качеством сна", badCount);
        });

        analysisFunctions.add(new SleeplessNightsCount());
        analysisFunctions.add(new ChronotypeDetermination());
    }

    private static class SleeplessNightsCount implements SleepFunction {
        @Override
        public SleepAnalysisResult apply(List<SleepingSession> sessions) {
            if (sessions.isEmpty()) {
                return new SleepAnalysisResult("Количество бессонных ночей", 0);
            }

            LocalDate firstDate = sessions.stream()
                    .map(SleepingSession::getStartDate)
                    .min(LocalDate::compareTo)
                    .orElseThrow();

            LocalDate lastDate = sessions.stream()
                    .map(SleepingSession::getEndDate)
                    .max(LocalDate::compareTo)
                    .orElseThrow();

            LocalDate startNight = sessions.get(0).getStartTime()
                    .isBefore(LocalTime.NOON) ?
                    firstDate.minusDays(1) : firstDate;

            long nightsWithSleep = sessions.stream()
                    .filter(SleepingSession::isNightSleep)
                    .map(session -> {
                        LocalDate sleepDate = session.getStartDate();
                        LocalTime sleepTime = session.getStartTime();
                        return sleepTime.isAfter(LocalTime.NOON) ?
                                sleepDate : sleepDate.minusDays(1);
                    })
                    .distinct()
                    .count();

            long totalNights = Period.between(startNight, lastDate).getDays();
            long sleeplessNights = Math.max(0, totalNights - nightsWithSleep);

            return new SleepAnalysisResult("Количество бессонных ночей", sleeplessNights);
        }
    }

    private static class ChronotypeDetermination implements SleepFunction {
        @Override
        public SleepAnalysisResult apply(List<SleepingSession> sessions) {
            Map<Chronotype, Long> stats = sessions.stream()
                    .map(SleepingSession::getChronotypeForNight)
                    .filter(ct -> ct != null) // Игнорируем дневные сессии
                    .collect(Collectors.groupingBy(
                            ct -> ct,
                            Collectors.counting()
                    ));

            long owlNights = stats.getOrDefault(Chronotype.OWL, 0L);
            long larkNights = stats.getOrDefault(Chronotype.LARK, 0L);
            long doveNights = stats.getOrDefault(Chronotype.DOVE, 0L);

            String chronotype;
            if (owlNights > larkNights && owlNights > doveNights) {
                chronotype = Chronotype.OWL.getRussianName();
            } else if (larkNights > owlNights && larkNights > doveNights) {
                chronotype = Chronotype.LARK.getRussianName();
            } else {
                chronotype = Chronotype.DOVE.getRussianName();
            }

            String result = String.format("%s (сов: %d, жаворонков: %d, голубей: %d)",
                    chronotype, owlNights, larkNights, doveNights);

            return new SleepAnalysisResult("Хронотип пользователя", result);
        }
    }

    public void analyzeAndPrint(String filePath) throws IOException {
        LogReader logReader = new LogReader();
        List<SleepingSession> sessions = logReader.readLogFile(filePath);

        if (sessions.isEmpty()) {
            System.out.println("В файле не найдено записей о сне.");
            return;
        }

        System.out.println("Всего сессий сна: " + sessions.size());

        analysisFunctions.stream()
                .map(func -> func.apply(sessions))
                .forEach(result -> System.out.println("- " + result));
    }

    public static void main(String[] args) {
        String filePath = DEFAULT_LOG_PATH;
        SleepTrackerApp app = new SleepTrackerApp();

        try {
            app.analyzeAndPrint(filePath);
        } catch (IOException e) {
            System.out.println("Ошибка: Файл не найден по пути: " + filePath);
        } catch (Exception e) {
            System.out.println("Ошибка анализа: " + e.getMessage());
        }
    }
}
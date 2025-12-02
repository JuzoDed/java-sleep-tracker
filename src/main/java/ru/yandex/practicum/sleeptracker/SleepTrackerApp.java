package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SleepTrackerApp {
    private final List<SleepFunction> analysisFunctions;
    private static final String DEFAULT_LOG_PATH = "C:\\Users\\user\\IdeaProjects\\java-sleep-tracker\\src\\main\\resources\\sleep_log.txt";

    public SleepTrackerApp() {
        analysisFunctions = new ArrayList<>();
        initializeFunctions();
    }

    private void initializeFunctions() {
        // Добавляем все функции анализа
        analysisFunctions.add(new TotalSessionsCount());
        analysisFunctions.add(new MinSleepDuration());
        analysisFunctions.add(new MaxSleepDuration());
        analysisFunctions.add(new AvgSleepDuration());
        analysisFunctions.add(new BadQualitySessionsCount());
        analysisFunctions.add(new SleeplessNightsCount());
        analysisFunctions.add(new ChronotypeDetermination());
    }

    public void analyzeAndPrint(String filePath) throws IOException {
        LogReader logReader = new LogReader();
        List<SleepingSession> sessions = logReader.readLogFile(filePath);

        if (sessions.isEmpty()) {
            System.out.println("В файле не найдено записей о сне.");
            return;
        }

        System.out.println("Всего сессий сна: " + sessions.size());

        // Выполняем все функции анализа
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
            e.printStackTrace();
        }
    }
}
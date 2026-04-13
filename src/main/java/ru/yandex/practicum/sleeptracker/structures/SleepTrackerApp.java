package ru.yandex.practicum.sleeptracker.structures;

import ru.yandex.practicum.sleeptracker.functions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SleepTrackerApp {

    private final List<SleepFunction> analysisFunctions;
    private static final String DEFAULT_LOG_PATH = "src\\main\\resources\\sleep_log.txt";

    public SleepTrackerApp() {
        analysisFunctions = new ArrayList<>();
        initializeFunctions();
    }

    private void initializeFunctions() {
        analysisFunctions.add(new TotalSessionsAnalysis());
        analysisFunctions.add(new MinSleepDurationAnalysis());
        analysisFunctions.add(new MaxSleepDurationAnalysis());
        analysisFunctions.add(new AverageSleepDurationAnalysis());
        analysisFunctions.add(new BadSleepQualityCount());
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
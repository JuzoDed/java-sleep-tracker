package ru.yandex.practicum.sleeptracker.structures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogReader {
    public List<SleepingSession> readLogFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        try (Stream<String> lines = Files.lines(path)) {
            return lines
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::parseSleepingSession)
                    .collect(Collectors.toList());
        }
    }

    private SleepingSession parseSleepingSession(String line) {
        String[] parts = line.split(";");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Некорректный формат строки: " + line);
        }

        String[] startParts = parts[0].split(" ");
        String[] endParts = parts[1].split(" ");
        String quality = parts[2];

        return new SleepingSession(
                startParts[1], startParts[0],
                endParts[1], endParts[0],
                quality
        );
    }
}
package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleepTrackerAppTest {

    private SleepTrackerApp app;

    @BeforeEach
    void setUp() {
        app = new SleepTrackerApp();
    }

    @Test
    void testTotalSessionsCount() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession("22:00", "01.10.25", "06:00", "02.10.25", "GOOD"),
                new SleepingSession("23:30", "02.10.25", "07:30", "03.10.25", "NORMAL"),
                new SleepingSession("14:30", "03.10.25", "15:30", "03.10.25", "BAD")
        );

        long count = sessions.size();
        assertEquals(3, count);

        SleepingSession session = sessions.get(0);
        assertEquals(LocalDate.of(2025, 10, 1), session.getStartDate());
        assertEquals(SleepQuality.GOOD, session.getSleepQuality());
    }

    @Test
    void testSleepDurationCalculation() {
        // Тест на 8 часов сна (с 23:00 до 7:00)
        SleepingSession session = new SleepingSession(
                "23:00", "01.10.25",
                "07:00", "02.10.25",
                "GOOD"
        );

        assertEquals(480, session.getSleepDuration());

        SleepingSession daySession = new SleepingSession(
                "14:00", "01.10.25",
                "15:00", "01.10.25",
                "NORMAL"
        );

        assertEquals(60, daySession.getSleepDuration());
    }

    @Test
    void testIsNightSleep() {
        SleepingSession nightSession1 = new SleepingSession(
                "23:00", "01.10.25",
                "01:00", "02.10.25",
                "GOOD"
        );
        assertTrue(nightSession1.isNightSleep());

        SleepingSession nightSession2 = new SleepingSession(
                "02:00", "01.10.25",
                "05:00", "01.10.25",
                "GOOD"
        );
        assertTrue(nightSession2.isNightSleep());

        SleepingSession daySession = new SleepingSession(
                "14:00", "01.10.25",
                "16:00", "01.10.25",
                "NORMAL"
        );
        assertFalse(daySession.isNightSleep());
    }

    @Test
    void testChronotypeForNight() {
        SleepingSession owlSession = new SleepingSession(
                "23:30", "01.10.25",
                "09:30", "02.10.25",
                "GOOD"
        );
        assertEquals(Chronotype.OWL, owlSession.getChronotypeForNight());

        SleepingSession larkSession = new SleepingSession(
                "21:00", "01.10.25",
                "06:30", "02.10.25",
                "NORMAL"
        );
        assertEquals(Chronotype.LARK, larkSession.getChronotypeForNight());

        SleepingSession doveSession = new SleepingSession(
                "22:30", "01.10.25",
                "07:30", "02.10.25",
                "NORMAL"
        );
        assertEquals(Chronotype.DOVE, doveSession.getChronotypeForNight());

        SleepingSession daySession = new SleepingSession(
                "14:00", "01.10.25",
                "16:00", "01.10.25",
                "NORMAL"
        );
        assertNull(daySession.getChronotypeForNight());
    }

    @Test
    void testBadQualitySessions() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession("22:00", "01.10.25", "06:00", "02.10.25", "BAD"),
                new SleepingSession("23:00", "02.10.25", "07:00", "03.10.25", "GOOD"),
                new SleepingSession("00:30", "03.10.25", "05:30", "04.10.25", "BAD"),
                new SleepingSession("14:00", "04.10.25", "15:00", "04.10.25", "NORMAL")
        );

        long badCount = sessions.stream()
                .filter(s -> s.getSleepQuality() == SleepQuality.BAD)
                .count();

        assertEquals(2, badCount);
    }

    @Test
    void testSleepQualityEnum() {
        assertEquals(SleepQuality.GOOD, SleepQuality.valueOf("GOOD"));
        assertEquals(SleepQuality.NORMAL, SleepQuality.valueOf("NORMAL"));
        assertEquals(SleepQuality.BAD, SleepQuality.valueOf("BAD"));
    }

    @Test
    void testChronotypeNames() {
        assertEquals("Сова", Chronotype.OWL.getRussianName());
        assertEquals("Жаворонок", Chronotype.LARK.getRussianName());
        assertEquals("Голубь", Chronotype.DOVE.getRussianName());
    }

    @Test
    void testSleepAnalysisResult() {
        SleepAnalysisResult result = new SleepAnalysisResult("Тест", 42);
        assertEquals("Тест", result.getDescription());
        assertEquals(42, result.getValue());
        assertEquals("Тест: 42", result.toString());
    }

    @Test
    void testLogReaderParsing() {
        String testLine = "01.10.25 22:15;02.10.25 08:00;GOOD";
        String[] parts = testLine.split(";");

        assertEquals(3, parts.length);
        assertEquals("01.10.25 22:15", parts[0]);
        assertEquals("02.10.25 08:00", parts[1]);
        assertEquals("GOOD", parts[2]);

        String[] startParts = parts[0].split(" ");
        assertEquals("01.10.25", startParts[0]);
        assertEquals("22:15", startParts[1]);
    }

    @Test
    void testEdgeCases() {
        SleepingSession shortSession = new SleepingSession(
                "23:00", "01.10.25",
                "23:30", "01.10.25",
                "NORMAL"
        );
        assertEquals(30, shortSession.getSleepDuration());

        SleepingSession longSession = new SleepingSession(
                "22:00", "01.10.25",
                "08:00", "02.10.25",
                "GOOD"
        );
        assertEquals(600, longSession.getSleepDuration()); // 10 часов = 600 минут
    }
}
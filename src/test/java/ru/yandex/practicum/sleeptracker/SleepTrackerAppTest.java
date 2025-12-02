package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SleepTrackerAppTest {

    @Test
    void testTotalSessionsCount() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession("22:00", "01.10.25", "06:00", "02.10.25", "GOOD"),
                new SleepingSession("23:30", "02.10.25", "07:30", "03.10.25", "NORMAL")
        );

        TotalSessionsCount function = new TotalSessionsCount();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(2L, result.getValue());
    }

    @Test
    void testSleepDurationCalculation() {
        SleepingSession session = new SleepingSession(
                "23:00", "01.10.25",
                "07:00", "02.10.25",
                "GOOD"
        );
        assertEquals(480, session.getSleepDuration());
    }

    @Test
    void testChronotypeDetermination() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession("23:30", "01.10.25", "09:30", "02.10.25", "GOOD"),
                new SleepingSession("21:00", "02.10.25", "06:30", "03.10.25", "NORMAL"),
                new SleepingSession("22:30", "03.10.25", "07:30", "04.10.25", "NORMAL")
        );

        ChronotypeDetermination function = new ChronotypeDetermination();
        SleepAnalysisResult result = function.apply(sessions);

        assertTrue(result.toString().contains("Голубь"));
    }

    @Test
    void testBadQualitySessions() {
        List<SleepingSession> sessions = Arrays.asList(
                new SleepingSession("22:00", "01.10.25", "06:00", "02.10.25", "BAD"),
                new SleepingSession("23:00", "02.10.25", "07:00", "03.10.25", "GOOD"),
                new SleepingSession("00:30", "03.10.25", "05:30", "04.10.25", "BAD")
        );

        BadQualitySessionsCount function = new BadQualitySessionsCount();
        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(2L, result.getValue());
    }
}
package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.SleeplessNightsCount;
import ru.yandex.practicum.sleeptracker.structures.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.structures.SleepingSession;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleeplessNightsCountTest {

    private final SleeplessNightsCount function = new SleeplessNightsCount();

    @Test
    void testEmptySessions() {

        List<SleepingSession> sessions = List.of();

        SleepAnalysisResult result = function.apply(sessions);

        assertEquals(0, result.getValue());
    }

    @Test
    void testFirstSessionAfterMidnight() {

        List<SleepingSession> sessions = List.of(

                new SleepingSession("01:00","01.10.25","06:00","01.10.25","GOOD"),
                new SleepingSession("23:00","02.10.25","07:00","03.10.25","GOOD")
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertNotNull(result);
    }

    @Test
    void testCrossMonthLogging() {

        List<SleepingSession> sessions = List.of(

                new SleepingSession("23:00","30.09.25","07:00","01.10.25","GOOD"),
                new SleepingSession("23:30","02.10.25","06:30","03.10.25","GOOD")
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertNotNull(result);
    }
}
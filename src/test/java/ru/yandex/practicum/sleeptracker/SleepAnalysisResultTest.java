package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SleepAnalysisResultTest {

    @Test
    void testToString() {

        SleepAnalysisResult result =
                new SleepAnalysisResult("Тест", 42);

        assertEquals("Тест", result.getDescription());
        assertEquals(42, result.getValue());
        assertEquals("Тест: 42", result.toString());
    }
}
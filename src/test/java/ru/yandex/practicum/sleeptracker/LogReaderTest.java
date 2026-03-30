package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogReaderTest {

    @Test
    void testParsingLine() {

        String testLine = "01.10.25 22:15;02.10.25 08:00;GOOD";

        String[] parts = testLine.split(";");

        assertEquals(3, parts.length);
        assertEquals("GOOD", parts[2]);
    }
}
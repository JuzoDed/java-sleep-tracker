package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.structures.Chronotype;
import ru.yandex.practicum.sleeptracker.structures.SleepingSession;

import static org.junit.jupiter.api.Assertions.*;

class SleepingSessionTest {

    @Test
    void testSleepDurationNight() {

        SleepingSession session = new SleepingSession(
                "23:00", "01.10.25",
                "07:00", "02.10.25",
                "GOOD"
        );

        assertEquals(480, session.getSleepDuration());
    }

    @Test
    void testSleepDurationDay() {

        SleepingSession session = new SleepingSession(
                "14:00", "01.10.25",
                "15:00", "01.10.25",
                "NORMAL"
        );

        assertEquals(60, session.getSleepDuration());
    }

    @Test
    void testIsNightSleep() {

        SleepingSession session = new SleepingSession(
                "23:00", "01.10.25",
                "02:00", "02.10.25",
                "GOOD"
        );

        assertTrue(session.isNightSleep());
    }

    @Test
    void testIsDaySleep() {

        SleepingSession session = new SleepingSession(
                "14:00", "01.10.25",
                "16:00", "01.10.25",
                "GOOD"
        );

        assertFalse(session.isNightSleep());
    }

    @Test
    void testChronotypeOwl() {

        SleepingSession session = new SleepingSession(
                "23:30", "01.10.25",
                "09:30", "02.10.25",
                "GOOD"
        );

        assertEquals(Chronotype.OWL, session.getChronotypeForNight());
    }

    @Test
    void testChronotypeLark() {

        SleepingSession session = new SleepingSession(
                "21:00", "01.10.25",
                "06:00", "02.10.25",
                "GOOD"
        );

        assertEquals(Chronotype.LARK, session.getChronotypeForNight());
    }

    @Test
    void testChronotypeDove() {

        SleepingSession session = new SleepingSession(
                "22:30", "01.10.25",
                "07:30", "02.10.25",
                "NORMAL"
        );

        assertEquals(Chronotype.DOVE, session.getChronotypeForNight());
    }
}
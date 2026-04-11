package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.functions.ChronotypeDetermination;
import ru.yandex.practicum.sleeptracker.structures.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.structures.SleepingSession;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChronotypeDeterminationTest {

    private final ChronotypeDetermination function = new ChronotypeDetermination();

    @Test
    void testChronotypeTieReturnsDove() {

        List<SleepingSession> sessions = List.of(

                new SleepingSession("23:30","01.10.25","09:00","02.10.25","GOOD"), // owl
                new SleepingSession("23:30","02.10.25","09:00","03.10.25","GOOD"), // owl

                new SleepingSession("21:00","03.10.25","06:00","04.10.25","GOOD"), // lark
                new SleepingSession("21:00","04.10.25","06:00","05.10.25","GOOD")  // lark
        );

        SleepAnalysisResult result = function.apply(sessions);

        assertTrue(result.toString().contains("Голубь"));
    }
}
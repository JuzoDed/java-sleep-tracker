package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

class TotalSessionsCount implements SleepFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long count = sessions.stream().count();
        return new SleepAnalysisResult("Общее количество сессий сна", count);
    }
}

class MinSleepDuration implements SleepFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long minDuration = sessions.stream()
                .mapToLong(SleepingSession::getSleepDuration)
                .min()
                .orElse(0);
        return new SleepAnalysisResult("Минимальная продолжительность сна (минут)", minDuration);
    }
}

class MaxSleepDuration implements SleepFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long maxDuration = sessions.stream()
                .mapToLong(SleepingSession::getSleepDuration)
                .max()
                .orElse(0);
        return new SleepAnalysisResult("Максимальная продолжительность сна (минут)", maxDuration);
    }
}

class AvgSleepDuration implements SleepFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        double avgDuration = sessions.stream()
                .mapToLong(SleepingSession::getSleepDuration)
                .average()
                .orElse(0.0);
        return new SleepAnalysisResult("Средняя продолжительность сна (минут)",
                String.format("%.1f", avgDuration));
    }
}

class BadQualitySessionsCount implements SleepFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long badCount = sessions.stream()
                .filter(s -> s.getSleepQuality() == SleepQuality.BAD)
                .count();
        return new SleepAnalysisResult("Количество сессий с плохим качеством сна", badCount);
    }
}

class SleeplessNightsCount implements SleepFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult("Количество бессонных ночей", 0);
        }

        LocalDate firstDate = sessions.stream()
                .map(SleepingSession::getStartDate)
                .min(LocalDate::compareTo)
                .orElseThrow();

        LocalDate lastDate = sessions.stream()
                .map(SleepingSession::getEndDate)
                .max(LocalDate::compareTo)
                .orElseThrow();

        LocalDate startNight = firstDate.atTime(sessions.get(0).getStartTime())
                .isBefore(firstDate.atTime(LocalTime.NOON)) ?
                firstDate.minusDays(1) : firstDate;

        LocalDate endNight = lastDate;

        long nightsWithSleep = sessions.stream()
                .filter(SleepingSession::isNightSleep)
                .map(session -> {
                    LocalDate sleepDate = session.getStartDate();
                    LocalTime sleepTime = session.getStartTime();
                    // Если сон начался после полудня, это следующая ночь
                    return sleepTime.isAfter(LocalTime.NOON) ?
                            sleepDate : sleepDate.minusDays(1);
                })
                .distinct()
                .count();

        long totalNights = java.time.Period.between(startNight, endNight).getDays();
        long sleeplessNights = totalNights - nightsWithSleep;

        return new SleepAnalysisResult("Количество бессонных ночей", sleeplessNights);
    }
}

class ChronotypeDetermination implements SleepFunction {
    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long owlNights = sessions.stream()
                .map(SleepingSession::getChronotypeForNight)
                .filter(ct -> ct == Chronotype.OWL)
                .count();

        long larkNights = sessions.stream()
                .map(SleepingSession::getChronotypeForNight)
                .filter(ct -> ct == Chronotype.LARK)
                .count();

        long doveNights = sessions.stream()
                .map(SleepingSession::getChronotypeForNight)
                .filter(ct -> ct == Chronotype.DOVE)
                .count();

        Chronotype dominant;
        if (owlNights > larkNights && owlNights > doveNights) {
            dominant = Chronotype.OWL;
        } else if (larkNights > owlNights && larkNights > doveNights) {
            dominant = Chronotype.LARK;
        } else {
            dominant = Chronotype.DOVE;
        }

        String result = String.format("%s (сов: %d, жаворонков: %d, голубей: %d)",
                dominant.getRussianName(), owlNights, larkNights, doveNights);

        return new SleepAnalysisResult("Хронотип пользователя", result);
    }
}
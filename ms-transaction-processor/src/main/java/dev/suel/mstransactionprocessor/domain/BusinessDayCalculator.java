package dev.suel.mstransactionprocessor.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class BusinessDayCalculator {
    public static boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    public static LocalDate previousBusinessDay(LocalDate date) {
        LocalDate previous = date.minusDays(1);

        while (isWeekend(previous)) {
            previous = previous.minusDays(1);
        }

        return previous;
    }
}

package hu.david.worklog.service;

import hu.david.worklog.model.WorkLogEntry;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Locale;

public class WageCalculator {

    public static int calculateWage(WorkLogEntry entry) {
        LocalTime start = entry.getStartTime();
        LocalTime end = entry.getEndTime();
        int baseWage = entry.getHourlyWage();
        boolean outOfCounty = entry.isOutOfCounty();

        long totalMinutes = Duration.between(start, end).toMinutes();
        double adjustedHours = roundToNearestHalf(totalMinutes / 60.0);

        double regularHours = Math.min(adjustedHours, 8);
        double overtimeHours = Math.max(0, adjustedHours - 8);

        double effectiveBaseWage = outOfCounty ? baseWage * 1.4 : baseWage;

        double totalWage = regularHours * effectiveBaseWage + overtimeHours * effectiveBaseWage * 1.3;

        return (int) Math.round(totalWage);
    }

    private static double roundToNearestHalf(double hours) {
        int fullHours = (int) hours;
        double remainder = hours - fullHours;

        if (remainder < 0.25) {
            return fullHours;
        } else if (remainder < 0.75) {
            return fullHours + 0.5;
        } else {
            return fullHours + 1.0;
        }
    }

    public static String formatWage(int wage) {
        NumberFormat formatter = NumberFormat.getNumberInstance(new Locale("hu", "HU"));
        return formatter.format(wage) + " Ft";
    }
}

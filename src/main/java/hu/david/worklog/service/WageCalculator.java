package hu.david.worklog.service;

import hu.david.worklog.model.WorkLogEntry;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Locale;

public class WageCalculator {

    public static int calculateWage(WorkLogEntry entry) {
        // Minden mező String, parse-olni kell!
        LocalTime start = parseTime(entry.getStartTime());
        LocalTime end = parseTime(entry.getEndTime());
        int baseWage = parseIntSafe(entry.getCalculatedWage()); // vagy entry.getHourlyWage(), ha van ilyen meződ
        boolean outOfCounty = parseBooleanSafe(entry.getLocation()); // vagy entry.getOutOfCounty(), ha van ilyen meződ

        if (start == null || end == null) return 0;

        long totalMinutes = Duration.between(start, end).toMinutes();
        double adjustedHours = roundToNearestHalf(totalMinutes / 60.0);

        double regularHours = Math.min(adjustedHours, 8);
        double overtimeHours = Math.max(0, adjustedHours - 8);

        double effectiveBaseWage = outOfCounty ? baseWage * 1.4 : baseWage;

        double totalWage = regularHours * effectiveBaseWage + overtimeHours * effectiveBaseWage * 1.3;

        return (int) Math.round(totalWage);
    }

    // Hasznos segéd: parse string to LocalTime
    private static LocalTime parseTime(String value) {
        try {
            return LocalTime.parse(value);
        } catch (Exception e) {
            return null;
        }
    }

    // Hasznos segéd: parse int
    private static int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    // Hasznos segéd: parse boolean (ha pl. "igen"/"nem" vagy "true"/"false")
    private static boolean parseBooleanSafe(String value) {
        if (value == null) return false;
        String lower = value.toLowerCase();
        return lower.equals("true") || lower.equals("igen") || lower.equals("1");
    }

    public static double roundToNearestHalf(double hours) {
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

    public static double calculateAdjustedHours(double rawHours) {
        return roundToNearestHalf(rawHours);
    }
}

package hu.david.worklog.service;

import hu.david.worklog.model.WorkLogEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class WageCalculatorTest {

    private WorkLogEntry createEntry(double hours, int hourlyWage, boolean outOfCounty) {
        LocalTime start = LocalTime.of(8, 0);
        int minutes = (int) Math.round(hours * 60);
        LocalTime end = start.plusMinutes(minutes);
        return new WorkLogEntry(1, LocalDate.now(), start, end, "", outOfCounty, hourlyWage);
    }

    @Test
    public void testRegular8Hours() {
        WorkLogEntry entry = createEntry(8.0, 1875, false);
        assertEquals(15000, WageCalculator.calculateWage(entry));
    }

    @Test
    public void testOvertime() {
        WorkLogEntry entry = createEntry(10.0, 1875, false);
        assertEquals(19875, WageCalculator.calculateWage(entry));
    }

    @Test
    public void testOutOfCounty() {
        WorkLogEntry entry = createEntry(8.0, 1875, true);
        assertEquals(21000, WageCalculator.calculateWage(entry));
    }

    @Test
    public void testOutOfCountyWithOvertime() {
        WorkLogEntry entry = createEntry(10.0, 1875, true);
        // 8h regular: 8 * (1875 * 1.4) = 21000
        // 2h overtime: 2 * (1875 * 1.4 * 1.3) = 6825
        assertEquals(27825, WageCalculator.calculateWage(entry));
    }

    @Test
    public void testRoundDownBelowHalf() {
        WorkLogEntry entry = createEntry(7.2, 1875, false);
        assertEquals(13125, WageCalculator.calculateWage(entry));
    }

    @Test
    public void testRoundToHalfHour() {
        WorkLogEntry entry = createEntry(7.3, 1875, false);
        assertEquals((int) Math.round(7.5 * 1875), WageCalculator.calculateWage(entry));
    }

    @Test
    public void testRoundUpAboveThreeQuarters() {
        WorkLogEntry entry = createEntry(7.8, 1875, false);
        assertEquals(15000, WageCalculator.calculateWage(entry));
    }

    @Test
    public void testAdjustedHoursCalculation() {
        assertEquals(7.5, WageCalculator.calculateAdjustedHours(7.3));
        assertEquals(8.0, WageCalculator.calculateAdjustedHours(7.8));
        assertEquals(7.0, WageCalculator.calculateAdjustedHours(7.2));
    }
}

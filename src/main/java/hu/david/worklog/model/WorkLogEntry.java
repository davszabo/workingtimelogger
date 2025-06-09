package hu.david.worklog.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WorkLogEntry {
    private int id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    private boolean outOfCounty;
    private int hourlyWage;
    private List<String> locations = new ArrayList<>();

    public WorkLogEntry(int id, LocalDate date, LocalTime startTime, LocalTime endTime, String description, boolean outOfCounty, int hourlyWage) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.outOfCounty = outOfCounty;
        this.hourlyWage = hourlyWage;
    }

    public WorkLogEntry(LocalDate date, LocalTime startTime, LocalTime endTime, String description, boolean outOfCounty, int hourlyWage) {
        this(-1, date, startTime, endTime, description, outOfCounty, hourlyWage);
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOutOfCounty() {
        return outOfCounty;
    }

    public int getWage() {
        return hourlyWage;
    }

    public int getHourlyWage() {
        return hourlyWage;
    }

    public List<String> getLocations() {
        return locations == null || locations.isEmpty() ? null : locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public double getDurationHours() {
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes() / 60.0;
    }

    public String getLocation() {
        return locations == null || locations.isEmpty() ? "" : String.join(", ", locations);
    }

    @Override
    public String toString() {
        return date + ": " + description;
    }
}

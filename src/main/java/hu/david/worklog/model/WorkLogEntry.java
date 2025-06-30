package hu.david.worklog.model;

public class WorkLogEntry {
    private int id; // Egyedi azonosító, az adatbázisban is szerepel
    private String date;
    private String startTime;
    private String endTime;
    private String duration;
    private String location;
    private String description;
    private String calculatedWage;

    // Konstruktor minden mezővel
    public WorkLogEntry(int id, String date, String startTime, String endTime, String duration,
                        String location, String description, String calculatedWage) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.location = location;
        this.description = description;
        this.calculatedWage = calculatedWage;
    }

    // Getterek
    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDuration() {
        return duration;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getCalculatedWage() {
        return calculatedWage;
    }

    // Opcionális setter, ha később akarod beállítani az ID-t
    public void setId(int id) {
        this.id = id;
    }
}

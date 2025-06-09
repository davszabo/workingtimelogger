package hu.david.worklog.db;

import hu.david.worklog.model.WorkLogEntry;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:worklog.db";
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());

    /** Kapcsolódás az adatbázishoz */
    private static Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");  // Regisztráljuk az SQLite JDBC drivert
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver nem található!", e);
        }
        return DriverManager.getConnection("jdbc:sqlite:worklog.db");
    }

    /** Adatbázis inicializálása (ha még nem létezik) */
    public static void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS work_log (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT NOT NULL,
                start_time TEXT NOT NULL,
                end_time TEXT NOT NULL,
                description TEXT NOT NULL,
                out_of_county BOOLEAN NOT NULL,
                hourly_wage INTEGER NOT NULL
            );
        """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            LOGGER.log(Level.INFO, "Adatbázis létrehozva és inicializálva.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Hiba történt az adatbázis inicializálásakor.", e);
        }
    }

    /** Bejegyzés mentése az adatbázisba */
    public static void saveEntry(WorkLogEntry entry) {
        String sql = "INSERT INTO work_log (date, start_time, end_time, description, out_of_county, hourly_wage) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entry.getDate().toString());
            pstmt.setString(2, entry.getStartTime().toString());
            pstmt.setString(3, entry.getEndTime().toString());
            pstmt.setString(4, entry.getDescription());
            pstmt.setBoolean(5, entry.isOutOfCounty());
            pstmt.setInt(6, entry.getHourlyWage());

            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Bejegyzés sikeresen mentve: " + entry);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Hiba történt a bejegyzés mentésekor.", e);
        }
    }

    /** Bejegyzések betöltése az adatbázisból */
    public static List<WorkLogEntry> loadEntries() {
        List<WorkLogEntry> entriesList = new ArrayList<>();
        String sql = "SELECT * FROM work_log";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                WorkLogEntry entry = new WorkLogEntry(
                        rs.getInt("id"),
                        LocalDate.parse(rs.getString("date")),
                        LocalTime.parse(rs.getString("start_time")),
                        LocalTime.parse(rs.getString("end_time")),
                        rs.getString("description"),
                        rs.getBoolean("out_of_county"),
                        rs.getInt("hourly_wage")
                );
                entriesList.add(entry);
            }
            LOGGER.log(Level.INFO, "Bejegyzések betöltése sikeres.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Hiba történt a bejegyzések betöltésekor.", e);
        }
        return entriesList;
    }

    /** Bejegyzés törlése az adatbázisból */
    public static void deleteEntry(int id) {
        String sql = "DELETE FROM work_log WHERE id=?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            LOGGER.log(Level.INFO, "Bejegyzés törölve, ID: " + id);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Hiba történt a bejegyzés törlésekor.", e);
        }
    }
}
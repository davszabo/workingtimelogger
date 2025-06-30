package hu.david.worklog.util;

import hu.david.worklog.model.WorkLogEntry;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class CSVExporter {

    public static void export(ObservableList<WorkLogEntry> entries, File file) throws Exception {
        // BOM-os UTF-8
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            // Írd be a BOM-ot
            writer.write('\uFEFF');
            writer.write("Dátum;Kezdés;Befejezés;Munkaidő;Helyszínek;Leírás;Napi Bér\n");
            for (WorkLogEntry entry : entries) {
                String line = String.format("%s;%s;%s;%s;%s;%s;%s",
                        entry.getDate(),
                        entry.getStartTime(),
                        entry.getEndTime(),
                        entry.getDuration(),
                        entry.getLocation(),
                        escape(entry.getDescription()),
                        entry.getCalculatedWage()
                );
                writer.write(line);
                writer.write("\n");
            }
        }
    }

    private static String escape(String value) {
        if (value == null) return "";
        return value.replace(";", ",").replace("\n", " ").replace("\r", "");
    }
}

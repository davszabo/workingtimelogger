package hu.david.worklog.ui;

import hu.david.worklog.model.WorkLogEntry;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class EntryListCell extends ListCell<WorkLogEntry> {
    @Override
    protected void updateItem(WorkLogEntry entry, boolean empty) {
        super.updateItem(entry, empty);

        if (empty || entry == null) {
            setText(null);
            setGraphic(null);
        } else {
            String date = entry.getDate();
            String description = entry.getDescription();
            String location = entry.getLocation();
            String startTime = entry.getStartTime();
            String endTime = entry.getEndTime();
            String duration = entry.getDuration();
            String calculatedWage = entry.getCalculatedWage();

            Text dateText = new Text("Dátum: " + date);
            Text timeText = new Text("Idő: " + startTime + " - " + endTime + " (" + duration + ")");
            Text descText = new Text("Leírás: " + description);
            Text locationText = new Text("Helyszínek: " + (location == null || location.isEmpty() ? "nincs megadva" : location));
            Text wageText = new Text("Napi bér: " + calculatedWage + " Ft");

            VBox box = new VBox(3, dateText, timeText, descText, locationText, wageText);
            setGraphic(box);
        }
    }
}

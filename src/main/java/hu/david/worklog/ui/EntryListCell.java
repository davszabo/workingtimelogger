package hu.david.worklog.ui;

import hu.david.worklog.model.WorkLogEntry;
import hu.david.worklog.service.WageCalculator;
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
            String date = entry.getDate().toString();
            String description = entry.getDescription();
            String location = entry.getLocation();
            int wage = entry.getHourlyWage();
            int totalWage = WageCalculator.calculateWage(entry);

            Text dateText = new Text("Dátum: " + date);
            Text descText = new Text("Leírás: " + description);
            Text locationText = new Text("Helyszínek: " + (location.isEmpty() ? "nincs megadva" : location));
            Text wageText = new Text("Órabér: " + wage + " Ft");
            Text totalText = new Text("Teljes napi bér: " + totalWage + " Ft");

            VBox box = new VBox(3, dateText, descText, locationText, wageText, totalText);
            setGraphic(box);
        }
    }
}

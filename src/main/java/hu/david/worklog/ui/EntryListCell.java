package hu.david.worklog.ui;

import hu.david.worklog.model.WorkLogEntry;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * Egyedi listaelem-megjelenítő a WorkLogEntry objektumokhoz.
 */
public class EntryListCell extends ListCell<WorkLogEntry> {

    @Override
    protected void updateItem(WorkLogEntry entry, boolean empty) {
        super.updateItem(entry, empty);

        if (empty || entry == null) {
            setText(null);
            setGraphic(null);
        } else {
            Label dateLabel = new Label(entry.getDate().toString());
            dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label timeLabel = new Label(entry.getStartTime() + " - " + entry.getEndTime());
            Label locationLabel = new Label(entry.getLocation());

            VBox textBox = new VBox(dateLabel, timeLabel, locationLabel);
            textBox.setSpacing(4);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Label wageLabel = new Label(entry.getWage() + " Ft");
            wageLabel.setStyle("-fx-text-fill: green;");

            HBox cellBox = new HBox(textBox, spacer, wageLabel);
            cellBox.setPadding(new Insets(10));
            cellBox.setSpacing(10);

            setGraphic(cellBox);
        }
    }
}

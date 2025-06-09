package hu.david.worklog.ui;

import hu.david.worklog.db.DatabaseManager;
import hu.david.worklog.model.WorkLogEntry;
import hu.david.worklog.service.WageCalculator;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EntriesViewController {

    @FXML
    private TableView<WorkLogEntry> entriesTable;
    @FXML
    private TableColumn<WorkLogEntry, String> dateColumn;
    @FXML
    private TableColumn<WorkLogEntry, String> startTimeColumn;
    @FXML
    private TableColumn<WorkLogEntry, String> endTimeColumn;
    @FXML
    private TableColumn<WorkLogEntry, String> durationColumn;
    @FXML
    private TableColumn<WorkLogEntry, String> locationColumn;
    @FXML
    private TableColumn<WorkLogEntry, String> descriptionColumn;
    @FXML
    private TableColumn<WorkLogEntry, String> calculatedWageColumn;

    @FXML
    public void initialize() {
        dateColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));

        startTimeColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getStartTime().toString()));

        endTimeColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getEndTime().toString()));

        durationColumn.setCellValueFactory(cell -> {
            long minutes = Duration.between(
                    cell.getValue().getStartTime(), cell.getValue().getEndTime()).toMinutes();
            long hours = minutes / 60;
            long remMin = minutes % 60;
            return new ReadOnlyStringWrapper(String.format("%02d:%02d", hours, remMin));
        });

        locationColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getLocation()));

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        calculatedWageColumn.setCellValueFactory(cell -> {
            WorkLogEntry entry = cell.getValue();
            int wage = WageCalculator.calculateWage(entry);
            return new ReadOnlyStringWrapper(WageCalculator.formatWage(wage));
        });

        calculatedWageColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<WorkLogEntry, String> call(TableColumn<WorkLogEntry, String> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(item);
                            WorkLogEntry entry = getTableView().getItems().get(getIndex());
                            double duration = WageCalculator.roundToNearestHalf(entry.getDurationHours());
                            if (duration > 8.0) {
                                setTextFill(Color.RED);
                            } else {
                                setTextFill(Color.BLACK);
                            }
                        }
                    }
                };
            }
        });

        refreshEntries();
    }

    @FXML
    public void refreshEntries() {
        List<WorkLogEntry> entries = DatabaseManager.loadEntries();
        entriesTable.getItems().setAll(entries);
    }

    @FXML
    public void deleteSelectedEntries() {
        WorkLogEntry selected = entriesTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DatabaseManager.deleteEntry(selected.getId());
            refreshEntries();
        }
    }

    @FXML
    public void goBack() {
        ViewManager.loadContent("HomeView.fxml");
    }
}

package hu.david.worklog.ui;

import hu.david.worklog.db.DatabaseManager;
import hu.david.worklog.model.WorkLogEntry;
import hu.david.worklog.util.CSVExporter;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
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

    private ObservableList<WorkLogEntry> entries = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupColumns();
        loadEntries();
    }

    private void setupColumns() {
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));
        startTimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStartTime()));
        endTimeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndTime()));
        durationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDuration()));
        locationColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLocation()));
        descriptionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        calculatedWageColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCalculatedWage()));
    }

    private void loadEntries() {
        entries.clear();
        List<WorkLogEntry> loaded = DatabaseManager.loadEntries();
        entries.addAll(loaded);
        entriesTable.setItems(entries);
    }

    @FXML
    private void refreshEntries() {
        loadEntries();
        showAlert(Alert.AlertType.INFORMATION, "Frissítés", "A bejegyzések frissítve.");
    }

    @FXML
    private void deleteSelectedEntries() {
        WorkLogEntry selected = entriesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Nincs kiválasztva", "Válassz ki egy bejegyzést a törléshez!");
            return;
        }
        DatabaseManager.deleteEntry(selected.getId()); // vagy getDate(), ha id nincs!
        loadEntries();
        showAlert(Alert.AlertType.INFORMATION, "Törlés", "A kiválasztott bejegyzés törölve.");
    }

    @FXML
    private void goBack() {
        // Implementáld itt a visszalépést (scene switch, stb.)
    }

    @FXML
    private void handleExport() {
        if (entries.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Nincs exportálható adat", "Nincsenek bejegyzések az exportáláshoz!");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportálás CSV-be");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV fájlok", "*.csv"));
        fileChooser.setInitialFileName("worklog-export.csv");
        File file = fileChooser.showSaveDialog(entriesTable.getScene().getWindow());

        if (file != null) {
            try {
                CSVExporter.export(entries, file);
                showAlert(Alert.AlertType.INFORMATION, "Export sikeres", "A bejegyzések sikeresen exportálva:\n" + file.getAbsolutePath());
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Export hiba", "Nem sikerült exportálni a bejegyzéseket.\n" + e.getMessage());
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

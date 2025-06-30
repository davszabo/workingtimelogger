package hu.david.worklog.ui;

import hu.david.worklog.db.DatabaseManager;
import hu.david.worklog.model.WorkLogEntry;
import hu.david.worklog.service.WageCalculator;
import hu.david.worklog.util.CSVExporter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewEntriesController {

    private static final Logger LOGGER = Logger.getLogger(ViewEntriesController.class.getName());
    private final ObservableList<WorkLogEntry> entriesObservableList = FXCollections.observableArrayList();

    @FXML
    private ListView<WorkLogEntry> entriesList;
    @FXML
    private Button deleteSelectedButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button backButton;
    @FXML
    private Button exportButton; // <-- ÚJ

    @FXML
    public void initialize() {
        loadEntriesFromDatabase();
        configureListView();
    }


    private void loadEntriesFromDatabase() {
        entriesObservableList.clear();
        List<WorkLogEntry> entries = DatabaseManager.loadEntries();
        if (entries.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Információ", "Nincsenek mentett bejegyzések az adatbázisban.");
        } else {
            entriesObservableList.addAll(entries);
            entriesList.setItems(entriesObservableList);
        }
    }

    private void configureListView() {
        // Ide jön az egyedi cella-formázás, ha van
    }

    @FXML
    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportálás CSV-be");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV fájlok", "*.csv"));
        fileChooser.setInitialFileName("worklog-export.csv");
        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

        if (file != null) {
            try {
                CSVExporter.export(entriesObservableList, file);
                showAlert(Alert.AlertType.INFORMATION, "Export sikeres", "A bejegyzések sikeresen exportálva lettek:\n" + file.getAbsolutePath());
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Hiba az export során", e);
                showAlert(Alert.AlertType.ERROR, "Export hiba", "Nem sikerült exportálni a bejegyzéseket.");
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

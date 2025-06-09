package hu.david.worklog.ui;

import hu.david.worklog.db.DatabaseManager;
import hu.david.worklog.model.WorkLogEntry;
import hu.david.worklog.service.WageCalculator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Bejegyzések megtekintése
 */
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

    /**
     * Inicializálás: Betöltjük a bejegyzéseket és egyedi cellák beállítása
     */
    @FXML
    public void initialize() {
        loadEntriesFromDatabase();
        configureListView();
    }

    /**
     * Bejegyzések betöltése az adatbázisból
     */
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

    /**
     * Egyedi megjelenítés beállítása a ListView számára
     */
    private void configureListView() {
        entriesList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(WorkLogEntry entry, boolean empty) {
                super.updateItem(entry, empty);
                if (empty || entry == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Kalkulált bér formázása
                    String formattedWage = WageCalculator.formatWage(WageCalculator.calculateWage(entry));

                    // Ikon beállítása
                    ImageView icon = new ImageView(new Image(getClass().getResource("/icons/worklog.png").toExternalForm()));
                    icon.setFitWidth(24);
                    icon.setFitHeight(24);

                    // Szöveg formázása
                    Label entryLabel = new Label(entry.getDescription() + " - " + formattedWage);
                    HBox entryBox = new HBox(10, icon, entryLabel);

                    setGraphic(entryBox);
                }
            }
        });
    }

    /**
     * Kiválasztott bejegyzések törlése
     */
    @FXML
    private void deleteSelectedEntries() {
        WorkLogEntry selectedEntry = entriesList.getSelectionModel().getSelectedItem();

        if (selectedEntry == null) {
            showAlert(Alert.AlertType.ERROR, "Hiba", "Nincs kijelölt bejegyzés törléshez!");
            return;
        }

        DatabaseManager.deleteEntry(selectedEntry.getId());
        entriesObservableList.remove(selectedEntry);
        LOGGER.log(Level.INFO, "Bejegyzés törölve: " + selectedEntry.getId());

        showAlert(Alert.AlertType.INFORMATION, "Siker", "Bejegyzés sikeresen törölve!");
    }

    /**
     * Bejegyzések frissítése
     */
    @FXML
    private void refreshEntries() {
        loadEntriesFromDatabase();
        showAlert(Alert.AlertType.INFORMATION, "Siker", "Bejegyzések sikeresen frissítve!");
    }

    /**
     * Visszatérés a főnézethez
     */
    @FXML
    private void goBack() {
        try {
            System.out.println("Visszatérés a főnézetre...");
            ViewManager.loadContent("MainView.fxml"); // Győződj meg róla, hogy ez a fájl létezik!
        } catch (Exception e) {
            System.err.println("Hiba a visszalépéskor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Egyszerűsített figyelmeztető üzenetek
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
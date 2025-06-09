package hu.david.worklog.ui;

import hu.david.worklog.db.DatabaseManager;
import hu.david.worklog.model.WorkLogEntry;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

/**
 * Vezérlő a mentett bejegyzések listájához.
 */
public class EntriesViewController {

    @FXML
    private ListView<WorkLogEntry> entriesList;

    @FXML
    public void initialize() {
        entriesList.setCellFactory(list -> new EntryListCell());
        refreshEntries();
    }

    @FXML
    public void goBack() {
        ViewManager.loadContent("HomeView.fxml");
    }

    @FXML
    public void deleteSelectedEntries() {
        WorkLogEntry selected = entriesList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DatabaseManager.deleteEntry(selected.getId());
            refreshEntries();
        }
    }

    @FXML
    public void refreshEntries() {
        List<WorkLogEntry> entries = DatabaseManager.loadEntries();
        entriesList.getItems().setAll(entries);
    }
}

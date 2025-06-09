package hu.david.worklog.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

/**
 * A fő nézet vezérlője, kezeli a menügombokra érkező eseményeket,
 * és betölti a megfelelő al-nézeteket a contentArea-ba.
 */
public class MainViewController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        // Biztonságos betöltés az alkalmazás inicializációja után
        Platform.runLater(this::goHome);
    }

    @FXML
    public void goHome() {
        ViewManager.loadContent("HomeView.fxml");
    }

    @FXML
    public void showNewEntry() {
        ViewManager.loadContent("AddEntryView.fxml");
    }

    @FXML
    public void showEntries() {
        ViewManager.loadContent("EntriesView.fxml");
    }

    @FXML
    public void exitApplication() {
        System.exit(0);
    }
}

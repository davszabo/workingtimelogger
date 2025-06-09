package hu.david.worklog.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Nézetkezelő osztály */
public class ViewManager {
    private static final Logger LOGGER = Logger.getLogger(ViewManager.class.getName());
    private static Stage primaryStage;

    /** Az alkalmazás főablakának beállítása */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
        primaryStage.setMaximized(true); // Biztosítjuk a teljes képernyős ablakot
        primaryStage.setFullScreen(false); // Ne mozi módban fusson
    }

    /** Teljes nézet betöltése (főablak frissítése) */
    public static void loadView(String fxmlFile) {
        if (primaryStage == null) {
            LOGGER.log(Level.SEVERE, "Hiba: A primaryStage nincs beállítva!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource("/hu/david/worklog/ui/" + fxmlFile));
            Scene scene = new Scene(loader.load(), 1920, 1080);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.setFullScreen(false);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Hiba: Nem sikerült betölteni az FXML fájlt: " + fxmlFile, e);
        }
    }

    /** Csak a központi tartalom frissítése */
    public static void loadContent(String fxmlFile) {
        if (primaryStage == null) {
            LOGGER.log(Level.SEVERE, "Hiba: A primaryStage nincs beállítva!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource("/hu/david/worklog/ui/" + fxmlFile));
            Pane newContent = loader.load();

            StackPane contentArea = (StackPane) primaryStage.getScene().lookup("#contentArea");
            if (contentArea == null) {
                LOGGER.log(Level.SEVERE, "Hiba: Nem található a contentArea az FXML-ben!");
                return;
            }

            // Csak a központi tartalmat frissítjük, az oldalsáv fixen marad!
            contentArea.getChildren().setAll(newContent);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Hiba: Nem sikerült betölteni az FXML fájlt: " + fxmlFile, e);
        }
    }

    /** Az aktuális Stage objektum lekérdezése */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
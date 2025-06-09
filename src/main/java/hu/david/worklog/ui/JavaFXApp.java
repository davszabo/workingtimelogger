package hu.david.worklog.ui;

import hu.david.worklog.db.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** JavaFX alkalmazás főindító osztálya */
public class JavaFXApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Az adatbázis inicializálása
            DatabaseManager.initializeDatabase();

            // Főablak beállítása a ViewManager számára
            ViewManager.setPrimaryStage(primaryStage);

            // FXML betöltése a MainView nézethez
            FXMLLoader loader = new FXMLLoader(JavaFXApp.class.getResource("/hu/david/worklog/ui/MainView.fxml"));
            Scene scene = new Scene(loader.load(), 1920, 1080);  // FullHD méret
            primaryStage.setTitle("WorkLog – Munkaidő Kezelés");
            primaryStage.setScene(scene);

            primaryStage.setMaximized(true);  // Ablak maximális méretűvé tétele
            primaryStage.setFullScreen(false);  // Biztosítsuk, hogy ne mozi módban fusson

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Hiba az alkalmazás indításakor: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
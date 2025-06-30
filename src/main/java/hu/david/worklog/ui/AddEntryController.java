package hu.david.worklog.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import hu.david.worklog.model.WorkLogEntry;
import hu.david.worklog.db.DatabaseManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Új munkanapló bejegyzés hozzáadása */
public class AddEntryController {

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Integer> startHourCombo;
    @FXML private ComboBox<Integer> startMinuteCombo;
    @FXML private ComboBox<Integer> endHourCombo;
    @FXML private ComboBox<Integer> endMinuteCombo;
    @FXML private TextField hourlyWageField;
    @FXML private CheckBox outOfCountyCheckBox;
    @FXML private TextArea descriptionField;
    @FXML private Button addLocationButton;
    @FXML private Button deleteLocationButton;
    @FXML private TextField locationField;
    @FXML private ListView<String> locationListView;

    private final ObservableList<String> locations = FXCollections.observableArrayList();

    /** Inicializálás: Órák és percek feltöltése */
    @FXML
    private void initialize() {
        // Órák 0-23 között, Integer típussal
        List<Integer> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(i);
        }
        startHourCombo.getItems().addAll(hours);
        endHourCombo.getItems().addAll(hours);

        // Percek 0-59 között, Integer típussal
        List<Integer> minutes = new ArrayList<>();
        for (int i = 0; i <= 59; i++) {
            minutes.add(i);
        }
        startMinuteCombo.getItems().addAll(minutes);
        endMinuteCombo.getItems().addAll(minutes);

        locationListView.setItems(locations);
    }

    /** Helyszín hozzáadása */
    @FXML
    private void addLocation() {
        String location = locationField.getText().trim();
        if (location.isEmpty()) {
            showAlert("Hiba", "A helyszín nem lehet üres!");
            return;
        }
        locations.add(location);
        locationField.clear();
    }

    /** Helyszín törlése */
    @FXML
    private void deleteLocation() {
        String selectedLocation = locationListView.getSelectionModel().getSelectedItem();
        if (selectedLocation == null) {
            showAlert("Hiba", "Nincs kijelölt helyszín törléshez!");
            return;
        }
        locations.remove(selectedLocation);
    }

    /** Bejegyzés mentése */
    @FXML
    private void saveEntry() {
        LocalDate date = datePicker.getValue();
        Integer startHour = startHourCombo.getValue();
        Integer startMinute = startMinuteCombo.getValue();
        Integer endHour = endHourCombo.getValue();
        Integer endMinute = endMinuteCombo.getValue();
        String hourlyWageText = hourlyWageField.getText();
        boolean outOfCounty = outOfCountyCheckBox.isSelected();
        String description = descriptionField.getText();

        if (date == null) {
            showAlert("Hiba", "Kérlek válassz dátumot!");
            return;
        }
        if (startHour == null || startMinute == null || endHour == null || endMinute == null) {
            showAlert("Hiba", "Kérlek válassz kezdő és befejező időpontot!");
            return;
        }
        if (hourlyWageText == null || hourlyWageText.isEmpty()) {
            showAlert("Hiba", "Az órabér mező nem lehet üres!");
            return;
        }
        if (description == null || description.isEmpty()) {
            showAlert("Hiba", "A munkaleírás mező nem lehet üres!");
            return;
        }

        try {
            int hourlyWage = Integer.parseInt(hourlyWageText);
            LocalTime startTime = LocalTime.of(startHour, startMinute);
            LocalTime endTime = LocalTime.of(endHour, endMinute);

            // --- ÚJ: duration kiszámítása ---
            long durationMinutes = java.time.Duration.between(startTime, endTime).toMinutes();
            String duration = (durationMinutes / 60) + ":" + String.format("%02d", (durationMinutes % 60));

            // --- ÚJ: locationok összevonása ---
            String locationsString = String.join(";", locations);

            // --- ÚJ: napi bér kiszámítása ---
            double hoursDecimal = durationMinutes / 60.0;
            double wageValue = hoursDecimal * hourlyWage * (outOfCounty ? 1.4 : 1.0);
            String calculatedWage = String.valueOf((int)Math.round(wageValue));

            // --- Az új WorkLogEntry példányosítás ---
            WorkLogEntry entry = new WorkLogEntry(
                    0,                      // új bejegyzésnél ID: 0
                    date.toString(),        // dátum szövegként
                    startTime.toString(),   // kezdés szövegként
                    endTime.toString(),     // befejezés szövegként
                    duration,               // időtartam "óó:pp" formában
                    locationsString,        // helyszínek
                    description,            // leírás
                    calculatedWage          // napi bér szövegként
            );

            DatabaseManager.saveEntry(entry);
            showAlert("Siker", "Bejegyzés mentve!");
            clearForm();
        } catch (NumberFormatException e) {
            showAlert("Hiba", "Az órabér csak szám lehet!");
        }
    }

    /** Visszatérés a főnézethez */
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

    /** Egyszerűsített figyelmeztető üzenetek */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /** Űrlap törlése */
    private void clearForm() {
        datePicker.setValue(null);
        List.of(startHourCombo, startMinuteCombo, endHourCombo, endMinuteCombo).forEach(combo -> combo.setValue(null));
        hourlyWageField.clear();
        outOfCountyCheckBox.setSelected(false);
        descriptionField.clear();
        locations.clear();
    }
}
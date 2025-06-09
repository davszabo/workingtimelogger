package hu.david.worklog.ui;

import hu.david.worklog.db.DatabaseManager;
import hu.david.worklog.model.WorkLogEntry;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AddEntryControllerTest {

    @BeforeEach
    void setup() {
        new File("worklog.db").delete();
        DatabaseManager.initializeDatabase();
        // initialize JavaFX toolkit
        new JFXPanel();
    }

    @AfterEach
    void cleanup() {
        new File("worklog.db").delete();
    }

    @Test
    void testSaveEntryCopiesLocations() throws Exception {
        AddEntryController controller = new AddEntryController();
        controller.datePicker = new DatePicker(LocalDate.of(2025, 1, 1));
        controller.startHourCombo = new ComboBox<>();
        controller.startHourCombo.setValue(8);
        controller.startMinuteCombo = new ComboBox<>();
        controller.startMinuteCombo.setValue(0);
        controller.endHourCombo = new ComboBox<>();
        controller.endHourCombo.setValue(16);
        controller.endMinuteCombo = new ComboBox<>();
        controller.endMinuteCombo.setValue(0);
        controller.hourlyWageField = new TextField("1000");
        controller.outOfCountyCheckBox = new CheckBox();
        controller.outOfCountyCheckBox.setSelected(false);
        controller.descriptionField = new TextArea("Test");

        Field locationsField = AddEntryController.class.getDeclaredField("locations");
        locationsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        ObservableList<String> locations = (ObservableList<String>) locationsField.get(controller);
        locations.addAll("Budapest", "Gyor");

        controller.saveEntry();

        List<WorkLogEntry> entries = DatabaseManager.loadEntries();
        assertEquals(1, entries.size());
        WorkLogEntry entry = entries.get(0);
        assertNotNull(entry.getLocations());
        assertEquals(List.of("Budapest", "Gyor"), entry.getLocations());
    }
}

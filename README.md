# WorkLog 📅

Munkaidő naplózó JavaFX alkalmazás. / A JavaFX application for work time logging.

---

## 🌟 Funkciók / Features

### Magyarul

* Munkaidő bejegyzések mentése: dátum, kezdés, befejezés, szünetek, megjegyzések
* Bejegyzések listázása, megjelenítése
* Fizetés kiszámítása órabér alapján
* JavaFX felhasználói felület, FXML-el
* SQLite lokális adatbázis

### English

* Save work time entries: date, start/end time, break duration, comments
* List and view previous entries
* Wage calculation based on hourly rate
* JavaFX user interface with FXML
* Local SQLite database backend

---

## 🚀 Indítás / Run

### Magyarul

1. Ellenőrizd, hogy Java 17 telepítve van
2. Nyisd meg az alkalmazást IntelliJ IDEA vagy bármely Maven-kompatibilis IDE segítségével
3. Futtasd a `JavaFXApp` osztályt, vagy használd az alábbi parancsot:

### English

1. Ensure Java 17 is installed
2. Open the project with IntelliJ IDEA or another Maven-compatible IDE
3. Run the `JavaFXApp` class or use the command:

```bash
mvn clean javafx:run
```

---

## 📚 Technológiák / Technologies

* Java 17
* JavaFX 21 (FXML)
* SQLite JDBC
* Maven
* JUnit 5

---

## 📂 Adatbázis / Database

### Magyarul

Az alkalmazás az első futtatáskor automatikusan létrehozza a `worklog.db` SQLite adatbázist.
Fő tábla: `entries`

* Mezők: `id`, `date`, `start`, `end`, `breakMinutes`, `comment`

### English

The application creates a `worklog.db` SQLite database on first run.
Main table: `entries`

* Fields: `id`, `date`, `start`, `end`, `breakMinutes`, `comment`

---

## 🔧 Fejlesztési struktúra / Project Structure

### Csomagok / Packages

* `model`: `WorkLogEntry` - adatmodellek / data models
* `db`: `DatabaseManager` - adatbázis kapcsolat / DB handling
* `service`: `WageCalculator` - fizetés számítás / wage computation
* `ui`: JavaFX vezérlők és főalkalmazás / JavaFX controllers & app entry

---

## 🔍 Tesztelés / Testing

```bash
mvn test
```

JUnit 5 tesztek elérhetők a `src/test/java` alatt. / JUnit 5 test cases under `src/test/java`


## 🙌 Közreműködők / Contributors

* David (fejlesztő / developer)

---

## ✨ Licenc / License

### Magyarul

Ez a projekt az **GNU Affero General Public License v3.0** (AGPLv3) alatt érhető el. Ez azt jelenti, hogy:

* Szabadon másolhatod, módosíthatod, terjesztheted.
* Ha online szolgáltatásként használod (pl. webalkalmazásként), akkor a forráskódot is elérhetővé kell tenned a felhasználók számára.
* A forráskód minden módosított változata is ugyanezen licenc alatt kell, hogy maradjon.

### English

This project is licensed under the **GNU Affero General Public License v3.0** (AGPLv3). This means:

* You may copy, modify, and distribute the software freely.
* If you run the software as a service over a network (e.g. web app), you must make the source code available to users.
* All modified versions must also remain under this same license.

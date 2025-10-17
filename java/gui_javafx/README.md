JavaFX GUI scaffold

But: This is a minimal JavaFX scaffold that integrates with your existing Java code (magasin, FileManager, models).

Requirements:
- JDK 17+ (or 11 with JavaFX libs separately)
- JavaFX SDK (if using modular approach) or use OpenJFX via Maven/Gradle

Files included:
- src/gui_javafx/Main.java - JavaFX application launcher
- src/gui_javafx/MainController.java - basic controller skeleton
- src/gui_javafx/MainView.fxml - simple FXML UI with a TableView placeholder

How to run (non-modular simple approach using OpenJFX jars):
1. Install JavaFX SDK and set PATH_TO_FX to its lib folder.
2. Compile:
   javac --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -d out src/gui_javafx/*.java
3. Run:
   java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml -cp out gui_javafx.Main

If you use Maven/Gradle, add OpenJFX dependencies instead.

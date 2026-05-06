package it.unicam.cs.mpgc.rpg126114;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX entry point of <em>Lo Speziale</em>.
 */
public class LoSpezialeApp extends Application {

    private static final String TITLE = "Lo Speziale";

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new StackPane(new Label(TITLE)), 960, 600);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }
}

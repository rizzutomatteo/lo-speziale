package it.unicam.cs.mpgc.rpg126114;

import javafx.application.Application;

/**
 * Launcher of the application.
 *
 * <p>The entry point is kept separate from the {@link javafx.application.Application}
 * subclass so the program also starts when the JavaFX modules are provided on the
 * class path instead of the module path.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Application.launch(LoSpezialeApp.class, args);
    }
}

package it.unicam.cs.mpgc.rpg126114.gui;

import it.unicam.cs.mpgc.rpg126114.game.Game;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Controller of the main menu: start a new game, resume a saved one or quit.
 */
public final class MainMenuController {

    private final GameUi ui;

    @FXML
    private Button resumeButton;

    public MainMenuController(GameUi ui) {
        this.ui = ui;
    }

    @FXML
    private void initialize() {
        resumeButton.setDisable(!ui.getRepository().exists());
    }

    @FXML
    private void onNewGame() {
        ui.showNewGame();
    }

    @FXML
    private void onResume() {
        ui.getRepository().load().ifPresent(state -> {
            ui.setGame(new Game(ui.getContent(), state, ui.getRandom()));
            ui.showVillage();
        });
    }

    @FXML
    private void onQuit() {
        Platform.exit();
    }
}

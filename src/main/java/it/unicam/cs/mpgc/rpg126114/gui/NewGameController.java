package it.unicam.cs.mpgc.rpg126114.gui;

import it.unicam.cs.mpgc.rpg126114.game.Game;
import it.unicam.cs.mpgc.rpg126114.game.GameFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Controller of the new-game screen: names the apothecary and begins.
 */
public final class NewGameController {

    private final GameUi ui;

    @FXML
    private TextField nameField;

    @FXML
    private Button startButton;

    public NewGameController(GameUi ui) {
        this.ui = ui;
    }

    @FXML
    private void initialize() {
        startButton.disableProperty().bind(nameField.textProperty().isEmpty());
    }

    @FXML
    private void onStart() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            return;
        }
        Game game = GameFactory.newGame(ui.getContent(), ui.getRandom(), name);
        ui.setGame(game);
        ui.showVillage();
    }

    @FXML
    private void onBack() {
        ui.showMainMenu();
    }
}

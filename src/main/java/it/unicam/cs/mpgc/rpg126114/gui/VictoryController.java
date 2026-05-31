package it.unicam.cs.mpgc.rpg126114.gui;

import it.unicam.cs.mpgc.rpg126114.game.GameState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller of the victory screen, shown once the apothecary becomes a Luminare.
 */
public final class VictoryController {

    private final GameUi ui;

    @FXML
    private Label summaryLabel;

    public VictoryController(GameUi ui) {
        this.ui = ui;
    }

    @FXML
    private void initialize() {
        GameState state = ui.getGame().getState();
        summaryLabel.setText("La tua fama si è levata fino al rango di Luminare. Hai guarito "
                + state.getPatientsHealed() + " malati in " + state.getClock().getDay()
                + " giorni, e il tuo nome sarà ricordato a lungo.");
    }

    @FXML
    private void onMenu() {
        ui.getRepository().delete();
        ui.setGame(null);
        ui.showMainMenu();
    }
}

package it.unicam.cs.mpgc.rpg126114.gui;

import it.unicam.cs.mpgc.rpg126114.game.Game;
import it.unicam.cs.mpgc.rpg126114.model.world.Region;
import it.unicam.cs.mpgc.rpg126114.model.world.Village;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller of the travel screen: lists the reachable villages with the days each
 * journey costs and sends the apothecary on the road.
 */
public final class TravelController {

    private final GameUi ui;

    @FXML
    private Label hereLabel;

    @FXML
    private VBox destinationsBox;

    public TravelController(GameUi ui) {
        this.ui = ui;
    }

    @FXML
    private void initialize() {
        Game game = ui.getGame();
        hereLabel.setText("Parti da " + game.getCurrentVillage().getName()
                + " · " + game.getState().getClock().describe());
        game.destinations().forEach(village -> destinationsBox.getChildren().add(buildCard(village)));
    }

    private Node buildCard(Village village) {
        Game game = ui.getGame();
        int days = game.travelDaysTo(village.getId());
        Region region = game.getWorld().getRegion(village.getRegionId());

        Label name = UiComponents.label(village.getName(), "subheading");
        Label regionLabel = UiComponents.label(region.getName(), "muted");
        Label description = UiComponents.label(village.getDescription(), "text");
        description.setWrapText(true);

        Label cost = UiComponents.label(days == 1 ? "1 giorno di cammino" : days + " giorni di cammino", "muted");
        Button leave = new Button("Parti");
        leave.getStyleClass().add("primary-button");
        leave.setOnAction(event -> {
            game.travelTo(village.getId());
            ui.showVillage();
        });
        HBox footer = new HBox(10, cost, UiComponents.horizontalSpacer(), leave);
        footer.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(6, name, regionLabel, description, footer);
        card.getStyleClass().add("card");
        return card;
    }

    @FXML
    private void onBack() {
        ui.showVillage();
    }
}

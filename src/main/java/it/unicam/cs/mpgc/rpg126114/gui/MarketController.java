package it.unicam.cs.mpgc.rpg126114.gui;

import it.unicam.cs.mpgc.rpg126114.game.Game;
import it.unicam.cs.mpgc.rpg126114.game.Market;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Ingredient;
import it.unicam.cs.mpgc.rpg126114.model.economy.Coins;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller of the market: shows the ingredients a village sells and lets the
 * apothecary buy them.
 */
public final class MarketController {

    private final GameUi ui;

    @FXML
    private Label coinsLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private VBox waresBox;

    public MarketController(GameUi ui) {
        this.ui = ui;
    }

    @FXML
    private void initialize() {
        refresh();
    }

    private void refresh() {
        Game game = ui.getGame();
        coinsLabel.setText(game.getApothecary().getPurse().toString());
        waresBox.getChildren().clear();
        game.marketWares().forEach(ingredient -> waresBox.getChildren().add(buildRow(ingredient)));
    }

    private Node buildRow(Ingredient ingredient) {
        Game game = ui.getGame();
        Coins price = game.priceOf(ingredient);

        Label name = UiComponents.label(ingredient.getName(), "subheading");
        Label details = UiComponents.label(
                ingredient.getDescription() + "  ·  " + ingredient.getRarity().getDisplayName(), "muted");
        details.setWrapText(true);
        VBox info = new VBox(2, name, details);
        HBox.setHgrow(info, javafx.scene.layout.Priority.ALWAYS);

        Label priceLabel = UiComponents.label(price.toString(), "text");
        Button buy = new Button("Compra");
        buy.getStyleClass().add("primary-button");
        buy.setOnAction(event -> {
            Market.PurchaseResult result = game.buy(ingredient.getId());
            messageLabel.setText(result.message());
            refresh();
        });

        HBox row = new HBox(14, info, priceLabel, buy);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("card");
        return row;
    }

    @FXML
    private void onBack() {
        ui.showVillage();
    }
}

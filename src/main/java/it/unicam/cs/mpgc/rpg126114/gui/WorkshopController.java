package it.unicam.cs.mpgc.rpg126114.gui;

import it.unicam.cs.mpgc.rpg126114.content.ContentRepository;
import it.unicam.cs.mpgc.rpg126114.game.Brewer;
import it.unicam.cs.mpgc.rpg126114.game.Game;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Recipe;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Remedy;
import it.unicam.cs.mpgc.rpg126114.model.progression.SkillType;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller of the workshop: brews known recipes into remedies and shows what the
 * apothecary carries.
 */
public final class WorkshopController {

    private final GameUi ui;

    @FXML
    private Label distillationLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private VBox recipesBox;

    @FXML
    private VBox inventoryBox;

    public WorkshopController(GameUi ui) {
        this.ui = ui;
    }

    @FXML
    private void initialize() {
        refresh();
    }

    private void refresh() {
        Game game = ui.getGame();
        distillationLabel.setText("Distillazione "
                + game.getApothecary().getSkill(SkillType.DISTILLAZIONE).getLevel());
        recipesBox.getChildren().clear();
        game.knownRecipes().forEach(recipe -> recipesBox.getChildren().add(buildRecipeCard(recipe)));
        renderInventory();
    }

    private Node buildRecipeCard(Recipe recipe) {
        Game game = ui.getGame();
        ContentRepository content = game.getContent();
        Remedy remedy = content.getRemedy(recipe.getRemedyId());

        Label name = UiComponents.label(remedy.getName(), "subheading");
        Label effects = UiComponents.label(remedy.getEffectSummary(), "muted");
        String ingredients = recipe.getIngredients().entrySet().stream()
                .map(entry -> content.getIngredient(entry.getKey()).getName() + " ×" + entry.getValue())
                .collect(Collectors.joining(", "));
        Label needs = UiComponents.label("Occorrono: " + ingredients, "text");
        needs.setWrapText(true);

        Label requirement = UiComponents.tag(recipe.getRequiredSkill().getDisplayName()
                + " " + recipe.getRequiredLevel());
        Button brew = new Button("Prepara");
        brew.getStyleClass().add("primary-button");
        brew.setOnAction(event -> {
            Brewer.BrewResult result = game.brew(recipe);
            messageLabel.setText(result.message());
            refresh();
        });
        HBox footer = new HBox(10, requirement, UiComponents.horizontalSpacer(), brew);
        footer.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(6, name, effects, needs, footer);
        card.getStyleClass().add("card");
        return card;
    }

    private void renderInventory() {
        Game game = ui.getGame();
        inventoryBox.getChildren().clear();
        inventoryBox.getChildren().add(UiComponents.label("Ingredienti", "subheading"));
        Map<String, Integer> ingredients = game.getApothecary().getInventory().getIngredients().asMap();
        if (ingredients.isEmpty()) {
            inventoryBox.getChildren().add(UiComponents.label("Nessun ingrediente.", "muted"));
        } else {
            ingredients.forEach((id, quantity) ->
                    inventoryBox.getChildren().add(itemLine(game.getContent().getIngredient(id).getName(), quantity)));
        }
        inventoryBox.getChildren().add(UiComponents.label("Rimedi", "subheading"));
        Map<String, Integer> remedies = game.getApothecary().getInventory().getRemedies().asMap();
        if (remedies.isEmpty()) {
            inventoryBox.getChildren().add(UiComponents.label("Nessun rimedio.", "muted"));
        } else {
            remedies.forEach((id, quantity) ->
                    inventoryBox.getChildren().add(itemLine(game.getContent().getRemedy(id).getName(), quantity)));
        }
    }

    private Node itemLine(String name, int quantity) {
        Label nameLabel = UiComponents.label(name, "text");
        Label quantityLabel = UiComponents.label("×" + quantity, "muted");
        HBox line = new HBox(8, nameLabel, UiComponents.horizontalSpacer(), quantityLabel);
        line.setAlignment(Pos.CENTER_LEFT);
        return line;
    }

    @FXML
    private void onBack() {
        ui.showVillage();
    }
}

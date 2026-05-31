package it.unicam.cs.mpgc.rpg126114.gui;

import it.unicam.cs.mpgc.rpg126114.game.Game;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.model.character.Patient;
import it.unicam.cs.mpgc.rpg126114.model.world.Village;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller of the village hub: shows where the apothecary is, who needs help and
 * the ways to act — treat a patient, brew, trade, travel or save.
 */
public final class VillageController {

    private final GameUi ui;

    @FXML
    private Label villageName;

    @FXML
    private Label villageDescription;

    @FXML
    private Label dayLabel;

    @FXML
    private Label coinsLabel;

    @FXML
    private Label reputationLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private VBox patientsBox;

    public VillageController(GameUi ui) {
        this.ui = ui;
    }

    @FXML
    private void initialize() {
        if (ui.getGame().isWon()) {
            ui.showVictory();
            return;
        }
        render();
    }

    private void render() {
        Game game = ui.getGame();
        Village village = game.getCurrentVillage();
        villageName.setText(village.getName());
        villageDescription.setText(village.getDescription());
        dayLabel.setText(game.getState().getClock().describe());
        coinsLabel.setText(game.getApothecary().getPurse().toString());
        reputationLabel.setText(game.getApothecary().getReputation().getTier().getDisplayName());

        patientsBox.getChildren().clear();
        if (game.getWaitingPatients().isEmpty()) {
            patientsBox.getChildren().add(
                    UiComponents.label("Nessun malato attende qui. Forse altrove hanno bisogno di te.", "muted"));
        } else {
            game.getWaitingPatients().forEach(patient ->
                    patientsBox.getChildren().add(buildPatientCard(patient)));
        }
    }

    private Node buildPatientCard(Patient patient) {
        Game game = ui.getGame();
        Affliction affliction = patient.getAffliction();

        Label name = UiComponents.label(patient.getName() + ", " + patient.getAge() + " anni", "subheading");
        HBox head = new HBox(10, name, UiComponents.horizontalSpacer(),
                UiComponents.severityBadge(affliction.getSeverity()));
        head.setAlignment(Pos.CENTER_LEFT);

        FlowPane symptoms = new FlowPane(8, 8);
        if (affliction.getActiveSymptoms().isEmpty()) {
            symptoms.getChildren().add(UiComponents.tag("nessun sintomo evidente"));
        } else {
            affliction.getActiveSymptoms().forEach(symptom ->
                    symptoms.getChildren().add(UiComponents.tag(symptom.getName())));
        }

        Button visit = new Button("Visita");
        visit.getStyleClass().add("primary-button");
        visit.setOnAction(event -> ui.showTreatment(game.startTreatment(patient)));
        HBox footer = new HBox(visit);
        footer.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(8, head, symptoms, footer);
        card.getStyleClass().add("card");
        return card;
    }

    @FXML
    private void onWorkshop() {
        ui.showWorkshop();
    }

    @FXML
    private void onMarket() {
        ui.showMarket();
    }

    @FXML
    private void onTravel() {
        ui.showTravel();
    }

    @FXML
    private void onSave() {
        ui.getRepository().save(ui.getGame().getState());
        statusLabel.setText("Partita salvata.");
    }

    @FXML
    private void onMenu() {
        ui.showMainMenu();
    }
}

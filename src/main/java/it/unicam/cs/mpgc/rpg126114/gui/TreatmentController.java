package it.unicam.cs.mpgc.rpg126114.gui;

import it.unicam.cs.mpgc.rpg126114.game.TreatmentReport;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Severity;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Remedy;
import it.unicam.cs.mpgc.rpg126114.model.character.Patient;
import it.unicam.cs.mpgc.rpg126114.model.humor.Humor;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentAction;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentObserver;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentOutcome;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentSession;
import it.unicam.cs.mpgc.rpg126114.model.treatment.action.BloodlettingAction;
import it.unicam.cs.mpgc.rpg126114.model.treatment.action.ComfortAction;
import it.unicam.cs.mpgc.rpg126114.model.treatment.action.ExamineAction;
import it.unicam.cs.mpgc.rpg126114.model.treatment.action.WaitAction;
import it.unicam.cs.mpgc.rpg126114.model.treatment.action.ApplyRemedyAction;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Controller of the treatment screen: the turn-based heart of the game.
 *
 * <p>It registers as an observer of the {@link TreatmentSession}, so a single
 * {@link #refresh()} repaints the clinical picture, the available moves and the
 * journal after each turn. The humours stay hidden until the patient is examined,
 * which is what turns the screen into a real diagnostic puzzle.
 */
public final class TreatmentController implements TreatmentObserver {

    private static final double TRACK_WIDTH = 200;
    private static final int SCALE_MAX = 10;

    private final GameUi ui;
    private final TreatmentSession session;
    private boolean concluded;

    @FXML private Label patientHeader;
    @FXML private Label severityLabel;
    @FXML private Label turnLabel;
    @FXML private Label patienceLabel;
    @FXML private Label diagnosisHint;
    @FXML private VBox humoursBox;
    @FXML private HBox toxicityBox;
    @FXML private FlowPane symptomsPane;
    @FXML private VBox actionsBox;
    @FXML private VBox remediesBox;
    @FXML private ListView<String> journalList;
    @FXML private StackPane outcomeOverlay;
    @FXML private Label outcomeTitle;
    @FXML private Label outcomeMessage;

    public TreatmentController(GameUi ui, TreatmentSession session) {
        this.ui = ui;
        this.session = session;
    }

    @FXML
    private void initialize() {
        session.addObserver(this);
        buildStandardActions();
        refresh();
    }

    @Override
    public void onUpdate(TreatmentSession updated, String message) {
        refresh();
    }

    private void buildStandardActions() {
        actionsBox.getChildren().setAll(
                actionButton(new ExamineAction()),
                actionButton(new ComfortAction()),
                actionButton(new BloodlettingAction()),
                actionButton(new WaitAction()));
    }

    private Button actionButton(TreatmentAction action) {
        Button button = new Button(action.getName());
        button.getStyleClass().add("button");
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(event -> perform(action));
        return button;
    }

    private void perform(TreatmentAction action) {
        if (!session.isOver()) {
            session.perform(action);
        }
    }

    private void refresh() {
        Patient patient = session.getPatient();
        patientHeader.setText(patient.getName() + ", " + patient.getAge() + " anni — da " + patient.getOrigin());

        Severity severity = session.getAffliction().getSeverity();
        severityLabel.setText(severity.getDisplayName());
        severityLabel.getStyleClass().setAll("badge", UiComponents.severityStyleClass(severity));
        turnLabel.setText("Turno " + session.getTurn());
        patienceLabel.setText("Pazienza " + Math.max(0, session.getPatience()));
        diagnosisHint.setText(session.isDiagnosed()
                ? "Hai letto i suoi umori: correggi gli eccessi riportandoli all'equilibrio."
                : "Esamina il paziente per leggerne gli umori.");

        renderHumours();
        renderToxicity();
        renderSymptoms();
        renderRemedies();
        renderJournal();

        if (session.isOver()) {
            concludeOnce();
        }
    }

    private void renderHumours() {
        humoursBox.getChildren().clear();
        for (Humor humor : Humor.values()) {
            humoursBox.getChildren().add(humourRow(humor));
        }
    }

    private Node humourRow(Humor humor) {
        Label name = UiComponents.label(humor.getDisplayName(), "humour-name");
        name.setMinWidth(94);

        StackPane track = bar();
        Region ideal = new Region();
        ideal.getStyleClass().add("bar-ideal");
        ideal.setMinSize(2, 16);
        ideal.setMaxSize(2, 16);
        StackPane.setAlignment(ideal, Pos.CENTER);

        Label value;
        if (session.isDiagnosed()) {
            int level = session.getAffliction().getBalance().level(humor);
            track.getChildren().add(fill(barStyleClass(humor), level));
            value = UiComponents.label(Integer.toString(level), "text");
        } else {
            value = UiComponents.label("?", "muted");
        }
        track.getChildren().add(ideal);
        value.setMinWidth(22);

        HBox row = new HBox(10, name, track, value);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void renderToxicity() {
        toxicityBox.getChildren().clear();
        int toxicity = session.getAffliction().getToxicity();
        Label name = UiComponents.label("Tossicità", "humour-name");
        name.setMinWidth(94);
        StackPane track = bar();
        track.getChildren().add(fill("toxicity-fill", toxicity));
        Label value = UiComponents.label(Integer.toString(toxicity), "text");
        value.setMinWidth(22);
        toxicityBox.getChildren().addAll(name, track, value);
    }

    private StackPane bar() {
        StackPane track = new StackPane();
        track.getStyleClass().add("humour-track");
        track.setMinSize(TRACK_WIDTH, 16);
        track.setMaxSize(TRACK_WIDTH, 16);
        track.setAlignment(Pos.CENTER_LEFT);
        return track;
    }

    private Region fill(String styleClass, int level) {
        Region fill = new Region();
        fill.getStyleClass().add(styleClass);
        double width = TRACK_WIDTH * Math.min(level, SCALE_MAX) / SCALE_MAX;
        fill.setMinSize(width, 16);
        fill.setMaxSize(width, 16);
        StackPane.setAlignment(fill, Pos.CENTER_LEFT);
        return fill;
    }

    private void renderSymptoms() {
        symptomsPane.getChildren().clear();
        var symptoms = session.getAffliction().getActiveSymptoms();
        if (symptoms.isEmpty()) {
            symptomsPane.getChildren().add(UiComponents.tag("nessun sintomo"));
        } else {
            symptoms.forEach(symptom -> symptomsPane.getChildren().add(UiComponents.tag(symptom.getName())));
        }
    }

    private void renderRemedies() {
        remediesBox.getChildren().clear();
        var remedies = session.getApothecary().getInventory().getRemedies().asMap();
        if (remedies.isEmpty()) {
            remediesBox.getChildren().add(UiComponents.label("Nessun rimedio in borsa.", "muted"));
            return;
        }
        remedies.forEach((id, quantity) -> {
            Remedy remedy = ui.getContent().getRemedy(id);
            Button button = new Button(remedy.getName() + "  ×" + quantity);
            button.getStyleClass().add("button");
            button.setMaxWidth(Double.MAX_VALUE);
            button.setTooltip(new Tooltip(remedy.getEffectSummary()));
            button.setOnAction(event -> perform(new ApplyRemedyAction(remedy)));
            remediesBox.getChildren().add(button);
        });
    }

    private void renderJournal() {
        journalList.getItems().setAll(session.getJournal());
        if (!session.getJournal().isEmpty()) {
            journalList.scrollTo(session.getJournal().size() - 1);
        }
    }

    private void concludeOnce() {
        if (concluded) {
            return;
        }
        concluded = true;
        actionsBox.setDisable(true);
        remediesBox.setDisable(true);
        TreatmentReport report = ui.getGame().concludeTreatment(session);
        outcomeTitle.setText(titleFor(report.outcome()));
        outcomeMessage.setText(report.message());
        outcomeOverlay.setVisible(true);
    }

    private static String titleFor(TreatmentOutcome outcome) {
        return switch (outcome) {
            case GUARITO -> "Guarito!";
            case DECEDUTO -> "Perduto";
            case ABBANDONATO -> "Abbandonato";
            case IN_CORSO -> "";
        };
    }

    private static String barStyleClass(Humor humor) {
        return switch (humor) {
            case SANGUE -> "bar-sangue";
            case FLEMMA -> "bar-flemma";
            case BILE_GIALLA -> "bar-bile-gialla";
            case BILE_NERA -> "bar-bile-nera";
        };
    }

    @FXML
    private void onContinue() {
        if (ui.getGame().isWon()) {
            ui.showVictory();
        } else {
            ui.showVillage();
        }
    }
}

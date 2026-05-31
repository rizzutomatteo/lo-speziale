package it.unicam.cs.mpgc.rpg126114.gui;

import it.unicam.cs.mpgc.rpg126114.content.ContentRepository;
import it.unicam.cs.mpgc.rpg126114.game.Game;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentSession;
import it.unicam.cs.mpgc.rpg126114.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg126114.util.RandomSource;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Hosts the single window and drives navigation between screens.
 *
 * <p>Every screen is an FXML view paired with a controller built here with a
 * reference to this object, so the controllers stay thin and reach the game state,
 * the content and the save through one well-known collaborator. Keeping all the
 * navigation in one place means a screen never needs to know how another is built.
 */
public final class GameUi {

    private static final String VIEW = "/it/unicam/cs/mpgc/rpg126114/view/";

    private final Stage stage;
    private final Scene scene;
    private final ContentRepository content;
    private final GameRepository repository;
    private final RandomSource random;
    private Game game;

    public GameUi(Stage stage, ContentRepository content, GameRepository repository, RandomSource random) {
        this.stage = stage;
        this.content = content;
        this.repository = repository;
        this.random = random;
        this.scene = new Scene(new StackPane(), 1000, 660);
        scene.getStylesheets().add(resource("style.css"));
        stage.setScene(scene);
        stage.setTitle("Lo Speziale");
        stage.setMinWidth(920);
        stage.setMinHeight(620);
    }

    public ContentRepository getContent() {
        return content;
    }

    public GameRepository getRepository() {
        return repository;
    }

    public RandomSource getRandom() {
        return random;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Stage getStage() {
        return stage;
    }

    public void showMainMenu() {
        swap("MainMenu.fxml", new MainMenuController(this));
    }

    public void showNewGame() {
        swap("NewGame.fxml", new NewGameController(this));
    }

    public void showVillage() {
        swap("Village.fxml", new VillageController(this));
    }

    public void showTreatment(TreatmentSession session) {
        swap("Treatment.fxml", new TreatmentController(this, session));
    }

    public void showWorkshop() {
        swap("Workshop.fxml", new WorkshopController(this));
    }

    public void showMarket() {
        swap("Market.fxml", new MarketController(this));
    }

    public void showTravel() {
        swap("Travel.fxml", new TravelController(this));
    }

    public void showVictory() {
        swap("Victory.fxml", new VictoryController(this));
    }

    private void swap(String fxml, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW + fxml));
            loader.setController(controller);
            Parent root = loader.load();
            scene.setRoot(root);
        } catch (IOException e) {
            throw new IllegalStateException("cannot load view " + fxml, e);
        }
    }

    private String resource(String name) {
        return getClass().getResource(VIEW + name).toExternalForm();
    }
}

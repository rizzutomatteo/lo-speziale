package it.unicam.cs.mpgc.rpg126114;

import it.unicam.cs.mpgc.rpg126114.content.ContentRepository;
import it.unicam.cs.mpgc.rpg126114.content.json.JsonContentLoader;
import it.unicam.cs.mpgc.rpg126114.gui.GameUi;
import it.unicam.cs.mpgc.rpg126114.persistence.FileGameRepository;
import it.unicam.cs.mpgc.rpg126114.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg126114.util.DefaultRandomSource;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX entry point of <em>Lo Speziale</em>.
 *
 * <p>It wires the few collaborators the game needs — the content read from the
 * bundled JSON, the file-based save and a source of randomness — into the
 * {@link GameUi} and opens on the main menu.
 */
public class LoSpezialeApp extends Application {

    @Override
    public void start(Stage stage) {
        ContentRepository content = new JsonContentLoader().load();
        GameRepository repository = new FileGameRepository(content);
        GameUi ui = new GameUi(stage, content, repository, new DefaultRandomSource());
        ui.showMainMenu();
        stage.show();
    }
}

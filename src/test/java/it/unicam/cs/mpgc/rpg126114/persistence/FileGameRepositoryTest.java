package it.unicam.cs.mpgc.rpg126114.persistence;

import it.unicam.cs.mpgc.rpg126114.content.ContentRepository;
import it.unicam.cs.mpgc.rpg126114.content.json.JsonContentLoader;
import it.unicam.cs.mpgc.rpg126114.game.Game;
import it.unicam.cs.mpgc.rpg126114.game.GameFactory;
import it.unicam.cs.mpgc.rpg126114.game.GameState;
import it.unicam.cs.mpgc.rpg126114.model.economy.Coins;
import it.unicam.cs.mpgc.rpg126114.util.DefaultRandomSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileGameRepositoryTest {

    private static final ContentRepository CONTENT = new JsonContentLoader().load();

    @Test
    void savingThenLoadingRestoresTheGame(@TempDir Path directory) {
        Game game = GameFactory.newGame(CONTENT, new DefaultRandomSource(5), "Orsola");
        game.getApothecary().earn(Coins.of(13));
        game.getApothecary().train(it.unicam.cs.mpgc.rpg126114.model.progression.SkillType.DISTILLAZIONE, 60);
        game.getState().recordHealed();

        GameRepository repository = new FileGameRepository(CONTENT, directory.resolve("partita.json"));
        assertFalse(repository.exists());

        repository.save(game.getState());
        assertTrue(repository.exists());

        GameState loaded = repository.load().orElseThrow();
        assertEquals("Orsola", loaded.getApothecary().getName());
        assertEquals(game.getApothecary().getPurse(), loaded.getApothecary().getPurse());
        assertEquals(1, loaded.getPatientsHealed());
        assertEquals(game.getCurrentVillage().getId(), loaded.getCurrentVillageId());
        assertEquals(game.getApothecary().getSkill(
                        it.unicam.cs.mpgc.rpg126114.model.progression.SkillType.DISTILLAZIONE).getLevel(),
                loaded.getApothecary().getSkill(
                        it.unicam.cs.mpgc.rpg126114.model.progression.SkillType.DISTILLAZIONE).getLevel());
    }

    @Test
    void loadingWithoutASaveReturnsEmpty(@TempDir Path directory) {
        GameRepository repository = new FileGameRepository(CONTENT, directory.resolve("none.json"));
        assertTrue(repository.load().isEmpty());
    }
}

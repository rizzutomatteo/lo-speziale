package it.unicam.cs.mpgc.rpg126114.persistence;

import it.unicam.cs.mpgc.rpg126114.game.GameState;

import java.util.Optional;

/**
 * Stores and retrieves a single saved game.
 *
 * <p>The game depends on this abstraction, not on any particular storage: the
 * bundled {@link FileGameRepository} keeps the save on disk, but a different
 * implementation could use a database or the cloud without any change elsewhere.
 */
public interface GameRepository {

    /** Whether a saved game is currently stored. */
    boolean exists();

    /**
     * Writes the given state, replacing any previously saved game.
     *
     * @throws PersistenceException if the state cannot be written
     */
    void save(GameState state);

    /**
     * Reads the saved game, if there is one.
     *
     * @throws PersistenceException if a save exists but cannot be read
     */
    Optional<GameState> load();

    /** Removes the saved game, if any. */
    void delete();
}

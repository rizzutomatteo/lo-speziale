package it.unicam.cs.mpgc.rpg126114.content;

/**
 * Loads the static game content from some source.
 *
 * <p>It is the abstraction that decouples the game from where its content lives;
 * {@link it.unicam.cs.mpgc.rpg126114.content.json.JsonContentLoader} reads it from
 * bundled JSON resources.
 */
public interface ContentLoader {

    /**
     * Loads and returns the content.
     *
     * @throws ContentException if the content cannot be loaded or is inconsistent
     */
    ContentRepository load();
}

package it.unicam.cs.mpgc.rpg126114.content;

/**
 * Raised when the static game content cannot be loaded or is inconsistent, for
 * instance when a recipe refers to a remedy that does not exist.
 */
public class ContentException extends RuntimeException {

    public ContentException(String message) {
        super(message);
    }

    public ContentException(String message, Throwable cause) {
        super(message, cause);
    }
}

package it.unicam.cs.mpgc.rpg126114.persistence;

/**
 * Raised when a game cannot be saved or a saved game cannot be read back.
 */
public class PersistenceException extends RuntimeException {

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}

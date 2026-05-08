package it.unicam.cs.mpgc.rpg126114.model;

/**
 * Anything that owns a stable identifier. Identifiers are the basis for the
 * equality of domain entities and for the references stored inside a saved game.
 */
public interface Identifiable {

    String getId();
}

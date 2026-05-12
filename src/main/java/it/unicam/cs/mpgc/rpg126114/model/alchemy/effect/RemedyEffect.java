package it.unicam.cs.mpgc.rpg126114.model.alchemy.effect;

import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;

/**
 * A single thing a remedy does to a body, expressed as a strategy.
 *
 * <p>Modelling effects behind one interface keeps remedies open to extension: a
 * new kind of effect is a new implementation, and neither the remedies that carry
 * it nor the engine that applies it need to change.
 */
public interface RemedyEffect {

    /**
     * Applies this effect to the given affliction.
     */
    void applyTo(Affliction affliction);

    /**
     * A short, human-readable label describing the effect, shown on remedies in
     * the user interface.
     */
    String describe();
}

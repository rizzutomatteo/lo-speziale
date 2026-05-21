package it.unicam.cs.mpgc.rpg126114.model.treatment;

/**
 * A single move the apothecary can make during a treatment, in the spirit of the
 * Command pattern.
 *
 * <p>Each move is reified as an object, so the set of available moves can grow
 * without the session having to know about any of them in particular.
 */
public interface TreatmentAction {

    /** The label shown for this move in the interface. */
    String getName();

    /** Carries the move out against the given treatment context. */
    void perform(TreatmentContext context);
}

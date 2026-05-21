package it.unicam.cs.mpgc.rpg126114.model.treatment;

/**
 * Notified whenever a treatment advances, so an interface can keep itself in step
 * with the engine without the engine knowing anything about the interface.
 */
public interface TreatmentObserver {

    void onUpdate(TreatmentSession session, String message);
}

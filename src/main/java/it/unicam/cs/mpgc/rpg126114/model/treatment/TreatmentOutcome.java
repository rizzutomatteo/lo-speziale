package it.unicam.cs.mpgc.rpg126114.model.treatment;

/**
 * How a treatment ends, or that it is still under way.
 */
public enum TreatmentOutcome {

    IN_CORSO("In corso"),
    GUARITO("Guarito"),
    DECEDUTO("Deceduto"),
    ABBANDONATO("Abbandonato");

    private final String displayName;

    TreatmentOutcome(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** Whether the treatment has concluded, one way or another. */
    public boolean isTerminal() {
        return this != IN_CORSO;
    }
}

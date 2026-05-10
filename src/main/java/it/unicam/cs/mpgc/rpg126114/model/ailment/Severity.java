package it.unicam.cs.mpgc.rpg126114.model.ailment;

/**
 * Qualitative gravity of an affliction, derived from a numeric score so the user
 * interface can speak in words rather than numbers.
 */
public enum Severity {

    LIEVE("Lieve"),
    MODERATA("Moderata"),
    GRAVE("Grave"),
    CRITICA("Critica");

    private final String displayName;

    Severity(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Maps a gravity score to the matching severity band.
     */
    public static Severity fromScore(int score) {
        if (score <= 3) {
            return LIEVE;
        }
        if (score <= 8) {
            return MODERATA;
        }
        if (score <= 14) {
            return GRAVE;
        }
        return CRITICA;
    }

    /**
     * Whether this severity is at least as grave as {@code other}.
     */
    public boolean isAtLeast(Severity other) {
        return ordinal() >= other.ordinal();
    }
}

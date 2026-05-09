package it.unicam.cs.mpgc.rpg126114.model.humor;

/**
 * The four humors of classical and medieval medicine. Keeping a body's humors in
 * balance is the central concern of every treatment performed in the game.
 */
public enum Humor {

    SANGUE("Sangue", "caldo e umido"),
    FLEMMA("Flemma", "freddo e umido"),
    BILE_GIALLA("Bile Gialla", "caldo e secco"),
    BILE_NERA("Bile Nera", "freddo e secco");

    private final String displayName;
    private final String quality;

    Humor(String displayName, String quality) {
        this.displayName = displayName;
        this.quality = quality;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** The pair of elemental qualities traditionally associated with the humor. */
    public String getQuality() {
        return quality;
    }
}

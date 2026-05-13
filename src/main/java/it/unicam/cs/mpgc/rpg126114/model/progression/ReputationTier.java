package it.unicam.cs.mpgc.rpg126114.model.progression;

/**
 * The standing an apothecary holds in the eyes of the people, derived from
 * reputation points.
 */
public enum ReputationTier {

    SCONOSCIUTO("Sconosciuto", 0),
    PRATICANTE("Praticante", 15),
    GUARITORE("Guaritore", 40),
    LUMINARE("Luminare", 80);

    private final String displayName;
    private final int requiredPoints;

    ReputationTier(String displayName, int requiredPoints) {
        this.displayName = displayName;
        this.requiredPoints = requiredPoints;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    /**
     * The highest tier whose threshold is met by the given points.
     */
    public static ReputationTier from(int points) {
        ReputationTier current = SCONOSCIUTO;
        for (ReputationTier tier : values()) {
            if (points >= tier.requiredPoints) {
                current = tier;
            }
        }
        return current;
    }
}

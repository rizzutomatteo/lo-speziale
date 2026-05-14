package it.unicam.cs.mpgc.rpg126114.model.alchemy;

/**
 * How hard an ingredient is to come by. Rarity raises the price a market asks for
 * the ingredient.
 */
public enum Rarity {

    COMUNE("Comune", 1.0),
    NON_COMUNE("Non comune", 1.6),
    RARO("Raro", 2.6),
    LEGGENDARIO("Leggendario", 4.5);

    private final String displayName;
    private final double priceMultiplier;

    Rarity(String displayName, double priceMultiplier) {
        this.displayName = displayName;
        this.priceMultiplier = priceMultiplier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }
}

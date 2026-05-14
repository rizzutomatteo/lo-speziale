package it.unicam.cs.mpgc.rpg126114.model.alchemy;

import it.unicam.cs.mpgc.rpg126114.model.AbstractEntity;
import it.unicam.cs.mpgc.rpg126114.model.economy.Coins;
import it.unicam.cs.mpgc.rpg126114.model.humor.Humor;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.Optional;

/**
 * A raw substance an apothecary buys, gathers and combines into remedies.
 *
 * <p>An ingredient may lean toward one {@link Humor}; this is a hint for the
 * apothecary rather than a mechanical effect, since what a remedy actually does is
 * decided by its recipe.
 */
public final class Ingredient extends AbstractEntity {

    private final String name;
    private final String description;
    private final Rarity rarity;
    private final Coins basePrice;
    private final Humor affinity;

    public Ingredient(String id, String name, String description,
                      Rarity rarity, Coins basePrice, Humor affinity) {
        super(id);
        this.name = Preconditions.requireNonBlank(name, "name");
        this.description = Preconditions.requireNonBlank(description, "description");
        this.rarity = Preconditions.requireNonNull(rarity, "rarity");
        this.basePrice = Preconditions.requireNonNull(basePrice, "basePrice");
        this.affinity = affinity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Rarity getRarity() {
        return rarity;
    }

    public Coins getBasePrice() {
        return basePrice;
    }

    /** The humor this ingredient is traditionally associated with, if any. */
    public Optional<Humor> getAffinity() {
        return Optional.ofNullable(affinity);
    }
}

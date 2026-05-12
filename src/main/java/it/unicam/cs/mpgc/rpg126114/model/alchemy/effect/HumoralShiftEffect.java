package it.unicam.cs.mpgc.rpg126114.model.alchemy.effect;

import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.model.humor.Humor;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

/**
 * Moves a single humor up or down. This is the corrective heart of most remedies:
 * applied wisely it restores balance, applied carelessly it deepens the imbalance.
 */
public final class HumoralShiftEffect implements RemedyEffect {

    private final Humor humor;
    private final int amount;

    public HumoralShiftEffect(Humor humor, int amount) {
        this.humor = Preconditions.requireNonNull(humor, "humor");
        Preconditions.require(amount != 0, "amount must not be zero");
        this.amount = amount;
    }

    public Humor getHumor() {
        return humor;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public void applyTo(Affliction affliction) {
        affliction.shiftHumor(humor, amount);
    }

    @Override
    public String describe() {
        return humor.getDisplayName() + " " + (amount > 0 ? "+" : "") + amount;
    }
}

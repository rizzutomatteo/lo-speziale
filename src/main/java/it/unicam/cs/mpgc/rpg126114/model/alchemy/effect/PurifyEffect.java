package it.unicam.cs.mpgc.rpg126114.model.alchemy.effect;

import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

/**
 * Draws toxic residue out of the body. Gentle purifying remedies undo the harm
 * left by harsher ones without touching the humoral balance.
 */
public final class PurifyEffect implements RemedyEffect {

    private final int amount;

    public PurifyEffect(int amount) {
        this.amount = Preconditions.requireInRange(amount, 1, 10, "amount");
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public void applyTo(Affliction affliction) {
        affliction.reduceToxicity(amount);
    }

    @Override
    public String describe() {
        return "Tossicità -" + amount;
    }
}

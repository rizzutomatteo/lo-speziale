package it.unicam.cs.mpgc.rpg126114.model.alchemy.effect;

import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

/**
 * Leaves a toxic residue behind. Potent remedies tend to carry one, which is what
 * makes overtreating a patient dangerous in its own right.
 */
public final class ToxicityEffect implements RemedyEffect {

    private final int amount;

    public ToxicityEffect(int amount) {
        this.amount = Preconditions.requireInRange(amount, 1, 10, "amount");
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public void applyTo(Affliction affliction) {
        affliction.addToxicity(amount);
    }

    @Override
    public String describe() {
        return "Tossicità +" + amount;
    }
}

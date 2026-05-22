package it.unicam.cs.mpgc.rpg126114.model.treatment.action;

import it.unicam.cs.mpgc.rpg126114.model.alchemy.Remedy;
import it.unicam.cs.mpgc.rpg126114.model.inventory.Stockpile;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentAction;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentContext;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

/**
 * Administers a remedy from the apothecary's bag, applying its effects to the
 * patient and consuming one dose.
 */
public final class ApplyRemedyAction implements TreatmentAction {

    private final Remedy remedy;

    public ApplyRemedyAction(Remedy remedy) {
        this.remedy = Preconditions.requireNonNull(remedy, "remedy");
    }

    @Override
    public String getName() {
        return "Somministra: " + remedy.getName();
    }

    @Override
    public void perform(TreatmentContext context) {
        Stockpile remedies = context.getApothecary().getInventory().getRemedies();
        Preconditions.require(remedies.has(remedy.getId(), 1),
                "the apothecary has no dose of " + remedy.getName());
        remedies.remove(remedy.getId(), 1);
        remedy.applyTo(context.getAffliction());
        context.log("Somministri " + remedy.getName() + " (" + remedy.getEffectSummary() + ").");
    }
}

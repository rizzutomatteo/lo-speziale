package it.unicam.cs.mpgc.rpg126114.model.treatment.action;

import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentAction;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentContext;

/**
 * Reassures the patient. It changes no humour, but buys time by restoring some of
 * the patient's patience and easing the strain of harsher remedies.
 */
public final class ComfortAction implements TreatmentAction {

    private static final int PATIENCE_RESTORED = 2;

    @Override
    public String getName() {
        return "Conforta";
    }

    @Override
    public void perform(TreatmentContext context) {
        context.restorePatience(PATIENCE_RESTORED);
        context.getAffliction().reduceToxicity(1);
        context.log("Conforti " + context.getPatient().getName()
                + ", che ritrova un poco di serenità.");
    }
}

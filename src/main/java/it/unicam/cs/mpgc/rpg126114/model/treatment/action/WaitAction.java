package it.unicam.cs.mpgc.rpg126114.model.treatment.action;

import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentAction;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentContext;

/**
 * Lets a turn pass without intervening, to watch how the illness develops. Useful
 * once the body has been set on the road to recovery.
 */
public final class WaitAction implements TreatmentAction {

    @Override
    public String getName() {
        return "Attendi";
    }

    @Override
    public void perform(TreatmentContext context) {
        context.log("Attendi e osservi l'evolversi del male.");
    }
}

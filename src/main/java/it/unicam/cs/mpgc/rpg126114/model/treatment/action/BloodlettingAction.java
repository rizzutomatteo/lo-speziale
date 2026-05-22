package it.unicam.cs.mpgc.rpg126114.model.treatment.action;

import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.model.humor.Humor;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentAction;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentContext;

/**
 * The classic, blunt instrument of medieval medicine: draws blood to reduce the
 * sanguine humour. It helps when blood runs in excess and harms when it does not,
 * and always carries a risk of leaving the patient weaker.
 */
public final class BloodlettingAction implements TreatmentAction {

    private static final int BLOOD_DRAWN = 3;
    private static final int BASE_TOXICITY = 2;
    private static final double COMPLICATION_CHANCE = 0.3;

    @Override
    public String getName() {
        return "Salasso";
    }

    @Override
    public void perform(TreatmentContext context) {
        Affliction affliction = context.getAffliction();
        affliction.shiftHumor(Humor.SANGUE, -BLOOD_DRAWN);
        affliction.addToxicity(BASE_TOXICITY);
        if (context.getRandom().chance(COMPLICATION_CHANCE)) {
            affliction.addToxicity(BASE_TOXICITY);
            context.log("Pratichi un salasso, ma la ferita s'infiamma e il paziente si indebolisce.");
        } else {
            context.log("Pratichi un salasso: il sangue defluisce e con esso parte dell'eccesso.");
        }
    }
}

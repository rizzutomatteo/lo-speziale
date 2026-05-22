package it.unicam.cs.mpgc.rpg126114.model.treatment.action;

import it.unicam.cs.mpgc.rpg126114.model.humor.Humor;
import it.unicam.cs.mpgc.rpg126114.model.progression.SkillType;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentAction;
import it.unicam.cs.mpgc.rpg126114.model.treatment.TreatmentContext;

/**
 * Studies the patient, revealing their humours and pointing at the one most out of
 * balance. Examining sharpens the apothecary's eye for diagnosis.
 */
public final class ExamineAction implements TreatmentAction {

    private static final int DIAGNOSIS_EXPERIENCE = 8;

    @Override
    public String getName() {
        return "Esamina";
    }

    @Override
    public void perform(TreatmentContext context) {
        context.revealDiagnosis();
        context.grantExperience(SkillType.DIAGNOSI, DIAGNOSIS_EXPERIENCE);
        Humor worst = context.getAffliction().getBalance().mostImbalanced();
        context.log("Esamini il paziente con cura: l'umore più alterato sembra essere "
                + worst.getDisplayName() + ".");
    }
}

package it.unicam.cs.mpgc.rpg126114.model.treatment;

import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.model.character.Apothecary;
import it.unicam.cs.mpgc.rpg126114.model.character.Patient;
import it.unicam.cs.mpgc.rpg126114.model.progression.SkillType;
import it.unicam.cs.mpgc.rpg126114.util.RandomSource;

/**
 * The narrow view of a treatment that {@link TreatmentAction actions} are allowed
 * to work on.
 *
 * <p>Exposing a context rather than the whole {@link TreatmentSession} keeps the
 * actions honest: they can read the patient and apothecary and use the few
 * treatment resources offered here, but they cannot drive the turn loop or decide
 * the outcome, which remain the session's responsibility.
 */
public interface TreatmentContext {

    Patient getPatient();

    Affliction getAffliction();

    Apothecary getApothecary();

    RandomSource getRandom();

    /** Appends a line to the treatment journal. */
    void log(String message);

    /** Awards experience in a skill to the apothecary. */
    void grantExperience(SkillType skill, int amount);

    /** Restores some of the patient's dwindling patience. */
    void restorePatience(int amount);

    /** Marks the patient's humours as read, so the interface may show them. */
    void revealDiagnosis();
}

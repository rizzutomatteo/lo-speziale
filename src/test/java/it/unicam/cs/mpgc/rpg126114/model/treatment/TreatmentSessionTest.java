package it.unicam.cs.mpgc.rpg126114.model.treatment;

import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Ailment;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Symptom;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Remedy;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.effect.HumoralShiftEffect;
import it.unicam.cs.mpgc.rpg126114.model.character.Apothecary;
import it.unicam.cs.mpgc.rpg126114.model.character.Patient;
import it.unicam.cs.mpgc.rpg126114.model.humor.Humor;
import it.unicam.cs.mpgc.rpg126114.model.humor.HumoralBalance;
import it.unicam.cs.mpgc.rpg126114.model.treatment.action.ApplyRemedyAction;
import it.unicam.cs.mpgc.rpg126114.model.treatment.action.WaitAction;
import it.unicam.cs.mpgc.rpg126114.util.DefaultRandomSource;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreatmentSessionTest {

    private static Patient patientWith(Ailment ailment) {
        return new Patient("p1", "Bianca", 40, "Valmorigen", new Affliction(ailment));
    }

    private static Ailment mildMelancholy() {
        Symptom gloom = new Symptom("malinconia", "Malinconia", "Sguardo cupo e svogliato",
                Humor.BILE_NERA, 2);
        HumoralBalance signature = HumoralBalance.of(Map.of(Humor.BILE_NERA, 8));
        return new Ailment("atrabile", "Atrabile", "Eccesso di bile nera",
                signature, List.of(gloom), 2);
    }

    private static Ailment severePlague() {
        Symptom fever = new Symptom("febbre", "Febbre", "Pelle ardente", Humor.SANGUE, 3);
        Symptom torpor = new Symptom("torpore", "Torpore", "Membra pesanti", Humor.BILE_NERA, 3);
        HumoralBalance signature = HumoralBalance.of(Map.of(Humor.SANGUE, 9, Humor.BILE_NERA, 9));
        return new Ailment("moria", "Moria Cinerea", "Un male oscuro e violento",
                signature, List.of(fever, torpor), 4);
    }

    @Test
    void aCorrectiveRemedyCuresThePatient() {
        Patient patient = patientWith(mildMelancholy());
        Apothecary apothecary = new Apothecary("Speziale");
        Remedy decoction = new Remedy("decotto", "Decotto di genziana", "Tempera la bile nera",
                List.of(new HumoralShiftEffect(Humor.BILE_NERA, -3)));
        apothecary.getInventory().addRemedy(decoction.getId(), 1);

        TreatmentSession session = new TreatmentSession(patient, apothecary, new DefaultRandomSource(1));
        session.perform(new ApplyRemedyAction(decoction));

        assertEquals(TreatmentOutcome.GUARITO, session.getOutcome());
    }

    @Test
    void aNeglectedSevereAilmentKillsThePatient() {
        Patient patient = patientWith(severePlague());
        Apothecary apothecary = new Apothecary("Speziale");
        TreatmentSession session = new TreatmentSession(patient, apothecary, new DefaultRandomSource(1));

        while (!session.isOver() && session.getTurn() < 20) {
            session.perform(new WaitAction());
        }

        assertTrue(session.isOver());
        assertEquals(TreatmentOutcome.DECEDUTO, session.getOutcome());
    }

    @Test
    void aFinishedTreatmentIgnoresFurtherActions() {
        Patient patient = patientWith(mildMelancholy());
        Apothecary apothecary = new Apothecary("Speziale");
        Remedy decoction = new Remedy("decotto", "Decotto", "Tempera la bile nera",
                List.of(new HumoralShiftEffect(Humor.BILE_NERA, -3)));
        apothecary.getInventory().addRemedy(decoction.getId(), 1);

        TreatmentSession session = new TreatmentSession(patient, apothecary, new DefaultRandomSource(1));
        session.perform(new ApplyRemedyAction(decoction));
        int turnsAtCure = session.getTurn();
        session.perform(new WaitAction());

        assertEquals(turnsAtCure, session.getTurn());
    }
}

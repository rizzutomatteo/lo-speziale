package it.unicam.cs.mpgc.rpg126114.model.alchemy.effect;

import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Ailment;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Symptom;
import it.unicam.cs.mpgc.rpg126114.model.humor.Humor;
import it.unicam.cs.mpgc.rpg126114.model.humor.HumoralBalance;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RemedyEffectTest {

    private static Affliction subject() {
        Symptom symptom = new Symptom("s", "Sintomo", "descrizione", Humor.SANGUE, 2);
        Ailment ailment = new Ailment("a", "Male", "descrizione",
                HumoralBalance.of(Map.of(Humor.SANGUE, 8)), List.of(symptom), 2);
        return new Affliction(ailment);
    }

    @Test
    void humoralShiftMovesTheTargetedHumor() {
        Affliction affliction = subject();
        new HumoralShiftEffect(Humor.SANGUE, -3).applyTo(affliction);
        assertEquals(5, affliction.getBalance().level(Humor.SANGUE));
    }

    @Test
    void toxicityEffectAddsResidue() {
        Affliction affliction = subject();
        new ToxicityEffect(3).applyTo(affliction);
        assertEquals(3, affliction.getToxicity());
    }

    @Test
    void purifyEffectDrawsToxicityOut() {
        Affliction affliction = subject();
        affliction.addToxicity(5);
        new PurifyEffect(2).applyTo(affliction);
        assertEquals(3, affliction.getToxicity());
    }
}

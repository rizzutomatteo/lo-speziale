package it.unicam.cs.mpgc.rpg126114.model.ailment;

import it.unicam.cs.mpgc.rpg126114.model.humor.Humor;
import it.unicam.cs.mpgc.rpg126114.model.humor.HumoralBalance;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AfflictionTest {

    private static Affliction sanguineFever() {
        Symptom fever = new Symptom("febbre", "Febbre", "Pelle ardente", Humor.SANGUE, 3);
        Ailment ailment = new Ailment("febbre", "Febbre", "Eccesso di sangue",
                HumoralBalance.of(Map.of(Humor.SANGUE, 8)), List.of(fever), 3);
        return new Affliction(ailment);
    }

    @Test
    void symptomsManifestAndFadeWithTheHumoralState() {
        Affliction affliction = sanguineFever();
        assertFalse(affliction.getActiveSymptoms().isEmpty());

        affliction.shiftHumor(Humor.SANGUE, -3); // back to the ideal
        assertTrue(affliction.getActiveSymptoms().isEmpty());
    }

    @Test
    void aBalancedCleanBodyCountsAsCured() {
        Affliction affliction = sanguineFever();
        assertFalse(affliction.isCured(1));

        affliction.shiftHumor(Humor.SANGUE, -3);
        assertTrue(affliction.isCured(1));
    }

    @Test
    void toxicityNeverFallsBelowZero() {
        Affliction affliction = sanguineFever();
        affliction.addToxicity(3);
        affliction.reduceToxicity(10);
        assertEquals(0, affliction.getToxicity());
    }

    @Test
    void recoveringMovesEveryHumorTowardTheIdeal() {
        Affliction affliction = sanguineFever();
        affliction.recover(1);
        assertEquals(7, affliction.getBalance().level(Humor.SANGUE));
    }
}

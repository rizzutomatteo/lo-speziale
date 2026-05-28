package it.unicam.cs.mpgc.rpg126114.game;

import it.unicam.cs.mpgc.rpg126114.content.ContentRepository;
import it.unicam.cs.mpgc.rpg126114.content.json.JsonContentLoader;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Recipe;
import it.unicam.cs.mpgc.rpg126114.util.DefaultRandomSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    private static final ContentRepository CONTENT = new JsonContentLoader().load();

    private Game newGame() {
        return GameFactory.newGame(CONTENT, new DefaultRandomSource(7), "Aldina");
    }

    @Test
    void aNewGameStartsWithWaitingPatientsAndMoney() {
        Game game = newGame();
        assertEquals("valmorigen", game.getCurrentVillage().getId());
        assertFalse(game.getWaitingPatients().isEmpty());
        assertTrue(game.getApothecary().getPurse().getAmount() > 0);
    }

    @Test
    void travellingSpendsDaysAndRefreshesPatients() {
        Game game = newGame();
        int before = game.getState().getClock().getDay();
        String destination = game.destinations().get(0).getId();
        int days = game.travelDaysTo(destination);
        game.travelTo(destination);

        assertEquals(destination, game.getCurrentVillage().getId());
        assertEquals(before + days, game.getState().getClock().getDay());
    }

    @Test
    void brewingConsumesIngredientsAndYieldsARemedy() {
        Game game = newGame();
        Recipe willowInfusion = CONTENT.getRecipe("r_salice");
        int dosesBefore = game.getApothecary().getInventory().getRemedies().count("infuso_salice");

        Brewer.BrewResult result = game.brew(willowInfusion);

        assertTrue(result.success(), result.message());
        assertEquals(dosesBefore + 1,
                game.getApothecary().getInventory().getRemedies().count("infuso_salice"));
        assertEquals(0, game.getApothecary().getInventory().getIngredients().count("salice"));
    }

    @Test
    void treatingAWaitingPatientReachesAConclusion() {
        Game game = newGame();
        var patient = game.getWaitingPatients().get(0);
        var session = game.startTreatment(patient);
        while (!session.isOver()) {
            session.perform(new it.unicam.cs.mpgc.rpg126114.model.treatment.action.WaitAction());
        }
        TreatmentReport report = game.concludeTreatment(session);
        assertTrue(report.outcome().isTerminal());
        assertFalse(game.getWaitingPatients().contains(patient));
    }
}

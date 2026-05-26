package it.unicam.cs.mpgc.rpg126114.content;

import it.unicam.cs.mpgc.rpg126114.content.json.JsonContentLoader;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Remedy;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.effect.HumoralShiftEffect;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonContentLoaderTest {

    private static final ContentRepository CONTENT = new JsonContentLoader().load();

    @Test
    void everySectionIsLoaded() {
        assertFalse(CONTENT.getSymptoms().isEmpty());
        assertFalse(CONTENT.getAilments().isEmpty());
        assertFalse(CONTENT.getIngredients().isEmpty());
        assertFalse(CONTENT.getRemedies().isEmpty());
        assertFalse(CONTENT.getRecipes().isEmpty());
        assertFalse(CONTENT.getWorldMap().getVillages().isEmpty());
    }

    @Test
    void recipesReferenceExistingRemediesAndIngredients() {
        CONTENT.getRecipes().forEach(recipe -> {
            assertEquals(recipe.getRemedyId(), CONTENT.getRemedy(recipe.getRemedyId()).getId());
            recipe.getIngredients().keySet()
                    .forEach(ingredientId -> CONTENT.getIngredient(ingredientId));
        });
    }

    @Test
    void polymorphicEffectsAreParsedThroughTheAdapter() {
        Remedy elixir = CONTENT.getRemedy("elisir_argento");
        assertEquals(3, elixir.getEffects().size());
        assertInstanceOf(HumoralShiftEffect.class, elixir.getEffects().get(0));
    }

    @Test
    void theWorldIsConnectedWithTravelCosts() {
        assertEquals(2, CONTENT.getWorldMap().travelDays("valmorigen", "fontechiara"));
        assertTrue(CONTENT.getWorldMap().villagesReachableFrom("valmorigen").size() >= 1);
    }
}

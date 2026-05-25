package it.unicam.cs.mpgc.rpg126114.content;

import it.unicam.cs.mpgc.rpg126114.model.ailment.Ailment;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Symptom;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Ingredient;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Recipe;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Remedy;
import it.unicam.cs.mpgc.rpg126114.model.world.WorldMap;

import java.util.List;

/**
 * Read-only access to all the static content of the game.
 *
 * <p>The rest of the game depends on this interface rather than on any particular
 * source of data, so the JSON files could be replaced by a database or a remote
 * service without touching the game logic.
 */
public interface ContentRepository {

    Symptom getSymptom(String id);

    List<Symptom> getSymptoms();

    Ailment getAilment(String id);

    List<Ailment> getAilments();

    Ingredient getIngredient(String id);

    List<Ingredient> getIngredients();

    Remedy getRemedy(String id);

    List<Remedy> getRemedies();

    Recipe getRecipe(String id);

    List<Recipe> getRecipes();

    WorldMap getWorldMap();
}

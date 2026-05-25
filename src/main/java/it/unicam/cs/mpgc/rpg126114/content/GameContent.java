package it.unicam.cs.mpgc.rpg126114.content;

import it.unicam.cs.mpgc.rpg126114.model.Registry;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Ailment;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Symptom;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Ingredient;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Recipe;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Remedy;
import it.unicam.cs.mpgc.rpg126114.model.world.WorldMap;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.Collection;
import java.util.List;

/**
 * Immutable, in-memory implementation of {@link ContentRepository}, backed by one
 * {@link Registry} per kind of content plus the world map.
 */
public final class GameContent implements ContentRepository {

    private final Registry<Symptom> symptoms;
    private final Registry<Ailment> ailments;
    private final Registry<Ingredient> ingredients;
    private final Registry<Remedy> remedies;
    private final Registry<Recipe> recipes;
    private final WorldMap worldMap;

    public GameContent(Collection<Symptom> symptoms, Collection<Ailment> ailments,
                       Collection<Ingredient> ingredients, Collection<Remedy> remedies,
                       Collection<Recipe> recipes, WorldMap worldMap) {
        this.symptoms = new Registry<>(symptoms);
        this.ailments = new Registry<>(ailments);
        this.ingredients = new Registry<>(ingredients);
        this.remedies = new Registry<>(remedies);
        this.recipes = new Registry<>(recipes);
        this.worldMap = Preconditions.requireNonNull(worldMap, "worldMap");
    }

    @Override
    public Symptom getSymptom(String id) {
        return symptoms.get(id);
    }

    @Override
    public List<Symptom> getSymptoms() {
        return symptoms.all();
    }

    @Override
    public Ailment getAilment(String id) {
        return ailments.get(id);
    }

    @Override
    public List<Ailment> getAilments() {
        return ailments.all();
    }

    @Override
    public Ingredient getIngredient(String id) {
        return ingredients.get(id);
    }

    @Override
    public List<Ingredient> getIngredients() {
        return ingredients.all();
    }

    @Override
    public Remedy getRemedy(String id) {
        return remedies.get(id);
    }

    @Override
    public List<Remedy> getRemedies() {
        return remedies.all();
    }

    @Override
    public Recipe getRecipe(String id) {
        return recipes.get(id);
    }

    @Override
    public List<Recipe> getRecipes() {
        return recipes.all();
    }

    @Override
    public WorldMap getWorldMap() {
        return worldMap;
    }
}

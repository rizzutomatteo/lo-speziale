package it.unicam.cs.mpgc.rpg126114.game;

import it.unicam.cs.mpgc.rpg126114.content.ContentRepository;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Recipe;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Remedy;
import it.unicam.cs.mpgc.rpg126114.model.character.Apothecary;
import it.unicam.cs.mpgc.rpg126114.model.inventory.Stockpile;
import it.unicam.cs.mpgc.rpg126114.model.progression.Skill;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.Map;

/**
 * Turns ingredients into remedies according to a recipe.
 *
 * <p>The brewer checks that the apothecary knows the recipe, is skilled enough and
 * holds the ingredients; only then does it consume them, hand over the remedy and
 * award distillation experience. Failures are reported, not thrown, so the
 * interface can show a gentle message.
 */
public final class Brewer {

    private final ContentRepository content;

    public Brewer(ContentRepository content) {
        this.content = Preconditions.requireNonNull(content, "content");
    }

    public BrewResult brew(Apothecary apothecary, Recipe recipe) {
        Preconditions.requireNonNull(apothecary, "apothecary");
        Preconditions.requireNonNull(recipe, "recipe");

        if (!apothecary.getRecipeBook().knows(recipe.getId())) {
            return BrewResult.failure("Non conosci ancora questa ricetta.");
        }
        Skill skill = apothecary.getSkill(recipe.getRequiredSkill());
        if (skill.getLevel() < recipe.getRequiredLevel()) {
            return BrewResult.failure("Ti serve " + recipe.getRequiredSkill().getDisplayName()
                    + " di livello " + recipe.getRequiredLevel() + ".");
        }
        Stockpile ingredients = apothecary.getInventory().getIngredients();
        for (Map.Entry<String, Integer> required : recipe.getIngredients().entrySet()) {
            if (!ingredients.has(required.getKey(), required.getValue())) {
                return BrewResult.failure("Ingredienti insufficienti.");
            }
        }

        for (Map.Entry<String, Integer> required : recipe.getIngredients().entrySet()) {
            ingredients.remove(required.getKey(), required.getValue());
        }
        Remedy remedy = content.getRemedy(recipe.getRemedyId());
        apothecary.getInventory().addRemedy(remedy.getId(), 1);
        apothecary.train(recipe.getRequiredSkill(), recipe.getRewardExperience());
        return BrewResult.success("Hai preparato: " + remedy.getName() + ".");
    }

    /**
     * The outcome of an attempt to brew, with a message for the player.
     */
    public record BrewResult(boolean success, String message) {

        static BrewResult success(String message) {
            return new BrewResult(true, message);
        }

        static BrewResult failure(String message) {
            return new BrewResult(false, message);
        }
    }
}

package it.unicam.cs.mpgc.rpg126114.model.alchemy;

import it.unicam.cs.mpgc.rpg126114.model.AbstractEntity;
import it.unicam.cs.mpgc.rpg126114.model.progression.SkillType;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.Map;

/**
 * The formula that turns a set of ingredients into a {@link Remedy}.
 *
 * <p>A recipe lists the ingredients it consumes (by id, with quantities), the
 * remedy it yields and the distillation skill the apothecary needs to attempt it.
 * It is pure data: the act of brewing belongs to a dedicated service.
 */
public final class Recipe extends AbstractEntity {

    private final String name;
    private final String description;
    private final Map<String, Integer> ingredients;
    private final String remedyId;
    private final SkillType requiredSkill;
    private final int requiredLevel;
    private final int rewardExperience;

    public Recipe(String id, String name, String description, Map<String, Integer> ingredients,
                  String remedyId, SkillType requiredSkill, int requiredLevel, int rewardExperience) {
        super(id);
        this.name = Preconditions.requireNonBlank(name, "name");
        this.description = Preconditions.requireNonBlank(description, "description");
        this.ingredients = Map.copyOf(Preconditions.requireNonNull(ingredients, "ingredients"));
        Preconditions.require(!this.ingredients.isEmpty(), "a recipe needs at least one ingredient");
        this.remedyId = Preconditions.requireNonBlank(remedyId, "remedyId");
        this.requiredSkill = Preconditions.requireNonNull(requiredSkill, "requiredSkill");
        this.requiredLevel = Preconditions.requireInRange(requiredLevel, 1, 10, "requiredLevel");
        this.rewardExperience = Math.max(0, rewardExperience);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /** The required ingredients as a map from ingredient id to quantity. */
    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public String getRemedyId() {
        return remedyId;
    }

    public SkillType getRequiredSkill() {
        return requiredSkill;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public int getRewardExperience() {
        return rewardExperience;
    }
}

package it.unicam.cs.mpgc.rpg126114.model.character;

import it.unicam.cs.mpgc.rpg126114.model.economy.Coins;
import it.unicam.cs.mpgc.rpg126114.model.inventory.Inventory;
import it.unicam.cs.mpgc.rpg126114.model.inventory.RecipeBook;
import it.unicam.cs.mpgc.rpg126114.model.progression.Reputation;
import it.unicam.cs.mpgc.rpg126114.model.progression.Skill;
import it.unicam.cs.mpgc.rpg126114.model.progression.SkillType;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * The player character: a travelling apothecary with three skills, a reputation, a
 * purse, an inventory and a book of known recipes.
 *
 * <p>The class guards its own invariants — a skill always exists for every
 * {@link SkillType}, money never goes negative — so callers manipulate the
 * apothecary through intention-revealing methods rather than raw fields.
 */
public final class Apothecary extends AbstractCharacter {

    private final Map<SkillType, Skill> skills;
    private final Reputation reputation;
    private Coins purse;
    private final Inventory inventory;
    private final RecipeBook recipeBook;

    public Apothecary(String name) {
        this("speziale", name, freshSkills(), new Reputation(), Coins.zero(),
                new Inventory(), new RecipeBook());
    }

    public Apothecary(String id, String name, Map<SkillType, Skill> skills, Reputation reputation,
                      Coins purse, Inventory inventory, RecipeBook recipeBook) {
        super(id, name);
        this.skills = new EnumMap<>(Preconditions.requireNonNull(skills, "skills"));
        for (SkillType type : SkillType.values()) {
            Preconditions.require(this.skills.containsKey(type), "missing skill: " + type);
        }
        this.reputation = Preconditions.requireNonNull(reputation, "reputation");
        this.purse = Preconditions.requireNonNull(purse, "purse");
        this.inventory = Preconditions.requireNonNull(inventory, "inventory");
        this.recipeBook = Preconditions.requireNonNull(recipeBook, "recipeBook");
    }

    private static Map<SkillType, Skill> freshSkills() {
        EnumMap<SkillType, Skill> map = new EnumMap<>(SkillType.class);
        for (SkillType type : SkillType.values()) {
            map.put(type, new Skill(type));
        }
        return map;
    }

    public Skill getSkill(SkillType type) {
        return skills.get(Preconditions.requireNonNull(type, "type"));
    }

    public Map<SkillType, Skill> getSkills() {
        return Collections.unmodifiableMap(skills);
    }

    public void train(SkillType type, int experience) {
        getSkill(type).gainExperience(experience);
    }

    public Reputation getReputation() {
        return reputation;
    }

    public Coins getPurse() {
        return purse;
    }

    public boolean canAfford(Coins price) {
        return purse.canAfford(price);
    }

    public void earn(Coins amount) {
        purse = purse.plus(amount);
    }

    /**
     * Spends money from the purse.
     *
     * @throws IllegalArgumentException if the purse cannot cover the price
     */
    public void spend(Coins price) {
        purse = purse.minus(price);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public RecipeBook getRecipeBook() {
        return recipeBook;
    }

    @Override
    public String getRole() {
        return "Speziale";
    }
}

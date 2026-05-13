package it.unicam.cs.mpgc.rpg126114.model.progression;

import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

/**
 * A single craft together with the apothecary's current mastery of it.
 *
 * <p>Experience accumulates toward the next level; the amount required grows with
 * the level, so early progress is quick and later progress is hard-won.
 */
public final class Skill {

    public static final int MAX_LEVEL = 10;

    private final SkillType type;
    private int level;
    private int experience;

    public Skill(SkillType type) {
        this(type, 1, 0);
    }

    public Skill(SkillType type, int level, int experience) {
        this.type = Preconditions.requireNonNull(type, "type");
        this.level = Preconditions.requireInRange(level, 1, MAX_LEVEL, "level");
        this.experience = Math.max(0, experience);
    }

    public SkillType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    /**
     * Experience needed to advance from the current level to the next one.
     */
    public int getExperienceForNextLevel() {
        return 50 * level;
    }

    public boolean isMaxed() {
        return level >= MAX_LEVEL;
    }

    /**
     * Adds experience, raising the level as many times as the accumulated total
     * allows. Experience stops accumulating once the maximum level is reached.
     */
    public void gainExperience(int amount) {
        Preconditions.require(amount >= 0, "amount must not be negative");
        if (isMaxed()) {
            return;
        }
        experience += amount;
        while (!isMaxed() && experience >= getExperienceForNextLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
        }
        if (isMaxed()) {
            experience = 0;
        }
    }
}

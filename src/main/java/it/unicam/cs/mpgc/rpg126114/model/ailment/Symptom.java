package it.unicam.cs.mpgc.rpg126114.model.ailment;

import it.unicam.cs.mpgc.rpg126114.model.AbstractEntity;
import it.unicam.cs.mpgc.rpg126114.model.humor.Humor;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

/**
 * An observable sign of illness, such as a fever or a cough.
 *
 * <p>Every symptom is tied to the {@link Humor} whose imbalance produces it: this
 * is what turns observation into diagnosis, because reading the symptoms hints at
 * the humor that must be corrected.
 */
public final class Symptom extends AbstractEntity {

    private final String name;
    private final String description;
    private final Humor humor;
    private final int gravity;

    public Symptom(String id, String name, String description, Humor humor, int gravity) {
        super(id);
        this.name = Preconditions.requireNonBlank(name, "name");
        this.description = Preconditions.requireNonBlank(description, "description");
        this.humor = Preconditions.requireNonNull(humor, "humor");
        this.gravity = Preconditions.requireInRange(gravity, 1, 5, "gravity");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /** The humor whose imbalance causes this symptom to manifest. */
    public Humor getHumor() {
        return humor;
    }

    /** How much this symptom adds to the overall gravity of an affliction. */
    public int getGravity() {
        return gravity;
    }
}

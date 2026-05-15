package it.unicam.cs.mpgc.rpg126114.model.alchemy;

import it.unicam.cs.mpgc.rpg126114.model.AbstractEntity;
import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.effect.RemedyEffect;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A prepared remedy: a named bundle of {@link RemedyEffect effects} that an
 * apothecary administers to a patient.
 *
 * <p>A remedy does not know how it was brewed; it only knows what it does, which
 * is what the treatment engine cares about.
 */
public final class Remedy extends AbstractEntity {

    private final String name;
    private final String description;
    private final List<RemedyEffect> effects;

    public Remedy(String id, String name, String description, List<RemedyEffect> effects) {
        super(id);
        this.name = Preconditions.requireNonBlank(name, "name");
        this.description = Preconditions.requireNonBlank(description, "description");
        this.effects = List.copyOf(Preconditions.requireNonNull(effects, "effects"));
        Preconditions.require(!this.effects.isEmpty(), "a remedy must have at least one effect");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<RemedyEffect> getEffects() {
        return effects;
    }

    /**
     * Administers the remedy by applying each of its effects, in order, to the
     * given affliction.
     */
    public void applyTo(Affliction affliction) {
        effects.forEach(effect -> effect.applyTo(affliction));
    }

    /**
     * A short, comma-separated summary of the effects, for display.
     */
    public String getEffectSummary() {
        return effects.stream()
                .map(RemedyEffect::describe)
                .collect(Collectors.joining(", "));
    }
}

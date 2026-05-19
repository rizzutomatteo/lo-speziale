package it.unicam.cs.mpgc.rpg126114.model.world;

import it.unicam.cs.mpgc.rpg126114.model.AbstractEntity;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

/**
 * A part of the realm that groups several villages and gives them a shared
 * character, such as the marshlands or the highlands.
 */
public final class Region extends AbstractEntity {

    private final String name;
    private final String description;

    public Region(String id, String name, String description) {
        super(id);
        this.name = Preconditions.requireNonBlank(name, "name");
        this.description = Preconditions.requireNonBlank(description, "description");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

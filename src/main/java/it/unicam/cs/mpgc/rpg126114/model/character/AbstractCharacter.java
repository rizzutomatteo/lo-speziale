package it.unicam.cs.mpgc.rpg126114.model.character;

import it.unicam.cs.mpgc.rpg126114.model.AbstractEntity;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

/**
 * Base class for the people in the game. Every character has an id and a name; the
 * role each one plays is left to the subclass, which is the one piece of behaviour
 * that genuinely differs between, say, a patient and the apothecary.
 */
public abstract class AbstractCharacter extends AbstractEntity {

    private final String name;

    protected AbstractCharacter(String id, String name) {
        super(id);
        this.name = Preconditions.requireNonBlank(name, "name");
    }

    public String getName() {
        return name;
    }

    /**
     * A short label for the kind of person this is, used for display.
     */
    public abstract String getRole();
}

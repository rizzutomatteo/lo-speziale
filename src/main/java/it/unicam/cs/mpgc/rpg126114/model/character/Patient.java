package it.unicam.cs.mpgc.rpg126114.model.character;

import it.unicam.cs.mpgc.rpg126114.model.ailment.Affliction;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

/**
 * A sick person seeking the apothecary's help, carrying the {@link Affliction} to
 * be treated.
 */
public final class Patient extends AbstractCharacter {

    private final int age;
    private final String origin;
    private final Affliction affliction;

    public Patient(String id, String name, int age, String origin, Affliction affliction) {
        super(id, name);
        this.age = Preconditions.requireInRange(age, 1, 120, "age");
        this.origin = Preconditions.requireNonBlank(origin, "origin");
        this.affliction = Preconditions.requireNonNull(affliction, "affliction");
    }

    public int getAge() {
        return age;
    }

    /** The place the patient comes from, shown when they introduce themselves. */
    public String getOrigin() {
        return origin;
    }

    public Affliction getAffliction() {
        return affliction;
    }

    @Override
    public String getRole() {
        return "Paziente";
    }
}

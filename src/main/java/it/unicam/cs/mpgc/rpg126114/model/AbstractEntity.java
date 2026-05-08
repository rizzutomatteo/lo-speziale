package it.unicam.cs.mpgc.rpg126114.model;

import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.Objects;

/**
 * Base class for domain entities identified by a stable id.
 *
 * <p>Two entities are equal when they share the same concrete type and the same
 * id, so an entity keeps its identity regardless of any mutable state it carries.
 * Centralising this here spares every entity from re-implementing
 * {@code equals}/{@code hashCode}.
 */
public abstract class AbstractEntity implements Identifiable {

    private final String id;

    protected AbstractEntity(String id) {
        this.id = Preconditions.requireNonBlank(id, "id");
    }

    @Override
    public final String getId() {
        return id;
    }

    @Override
    public final boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        return id.equals(((AbstractEntity) other).id);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + id + "]";
    }
}

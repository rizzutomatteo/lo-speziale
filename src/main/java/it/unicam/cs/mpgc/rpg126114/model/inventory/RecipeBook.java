package it.unicam.cs.mpgc.rpg126114.model.inventory;

import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The set of recipes an apothecary has learned. New recipes are discovered through
 * play; what is not in the book cannot be brewed.
 */
public final class RecipeBook {

    private final Set<String> known = new LinkedHashSet<>();

    public RecipeBook() {
    }

    public RecipeBook(Collection<String> recipeIds) {
        known.addAll(Preconditions.requireNonNull(recipeIds, "recipeIds"));
    }

    /**
     * Learns a recipe.
     *
     * @return {@code true} if it was not already known
     */
    public boolean learn(String recipeId) {
        return known.add(Preconditions.requireNonBlank(recipeId, "recipeId"));
    }

    public boolean knows(String recipeId) {
        return known.contains(recipeId);
    }

    public Set<String> all() {
        return Collections.unmodifiableSet(known);
    }

    public int size() {
        return known.size();
    }
}

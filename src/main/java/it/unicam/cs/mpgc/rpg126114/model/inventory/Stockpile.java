package it.unicam.cs.mpgc.rpg126114.model.inventory;

import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A counted collection of items referenced by id.
 *
 * <p>The same structure backs the apothecary's ingredients and remedies as well
 * as the stock a market offers, so the counting and quantity checks live in one
 * place. Insertion order is preserved to keep listings stable in the interface.
 */
public final class Stockpile {

    private final Map<String, Integer> counts = new LinkedHashMap<>();

    public Stockpile() {
    }

    /**
     * Builds a stockpile from an existing id-to-quantity map, ignoring
     * non-positive quantities. Useful when restoring a saved game.
     */
    public Stockpile(Map<String, Integer> initial) {
        Preconditions.requireNonNull(initial, "initial");
        initial.forEach((id, quantity) -> {
            if (quantity != null && quantity > 0) {
                counts.put(id, quantity);
            }
        });
    }

    public void add(String id, int quantity) {
        Preconditions.requireNonBlank(id, "id");
        Preconditions.require(quantity > 0, "quantity must be positive");
        counts.merge(id, quantity, Integer::sum);
    }

    /**
     * Removes {@code quantity} units of an item.
     *
     * @throws IllegalArgumentException if fewer than {@code quantity} are held
     */
    public void remove(String id, int quantity) {
        Preconditions.require(quantity > 0, "quantity must be positive");
        int current = count(id);
        Preconditions.require(current >= quantity, "not enough of " + id + " in stock");
        if (current == quantity) {
            counts.remove(id);
        } else {
            counts.put(id, current - quantity);
        }
    }

    public int count(String id) {
        return counts.getOrDefault(id, 0);
    }

    public boolean has(String id, int quantity) {
        return count(id) >= quantity;
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    /** An unmodifiable view of the held quantities, by item id. */
    public Map<String, Integer> asMap() {
        return Collections.unmodifiableMap(counts);
    }
}

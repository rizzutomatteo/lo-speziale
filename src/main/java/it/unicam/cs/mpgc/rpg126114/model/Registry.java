package it.unicam.cs.mpgc.rpg126114.model;

import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * An immutable, id-keyed collection of {@link Identifiable} items.
 *
 * <p>Being generic, the same lookup serves every kind of content — ingredients,
 * remedies, ailments and so on — without duplicating the indexing and the
 * not-found handling for each one.
 *
 * @param <T> the type of item held
 */
public final class Registry<T extends Identifiable> {

    private final Map<String, T> byId;

    public Registry(Collection<? extends T> items) {
        Preconditions.requireNonNull(items, "items");
        LinkedHashMap<String, T> map = new LinkedHashMap<>();
        for (T item : items) {
            Preconditions.require(!map.containsKey(item.getId()), "duplicate id: " + item.getId());
            map.put(item.getId(), item);
        }
        this.byId = Collections.unmodifiableMap(map);
    }

    /**
     * Returns the item with the given id.
     *
     * @throws IllegalArgumentException if no such item exists
     */
    public T get(String id) {
        T item = byId.get(id);
        Preconditions.require(item != null, "unknown id: " + id);
        return item;
    }

    public Optional<T> find(String id) {
        return Optional.ofNullable(byId.get(id));
    }

    public boolean contains(String id) {
        return byId.containsKey(id);
    }

    public List<T> all() {
        return List.copyOf(byId.values());
    }

    public int size() {
        return byId.size();
    }
}

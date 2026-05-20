package it.unicam.cs.mpgc.rpg126114.model.world;

import it.unicam.cs.mpgc.rpg126114.model.Identifiable;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The realm the apothecary travels through: its regions, its villages and the
 * number of days it takes to move between them.
 *
 * <p>The map is immutable content. Pairs of villages without an explicit route are
 * still reachable, at a default cost, so the world stays fully connected.
 */
public final class WorldMap {

    public static final int DEFAULT_TRAVEL_DAYS = 3;

    private final Map<String, Region> regions;
    private final Map<String, Village> villages;
    private final Map<String, Integer> routeDays;

    public WorldMap(Collection<Region> regions, Collection<Village> villages,
                    Map<String, Integer> routeDays) {
        this.regions = index(regions);
        this.villages = index(villages);
        this.routeDays = Map.copyOf(Preconditions.requireNonNull(routeDays, "routeDays"));
        Preconditions.require(!this.villages.isEmpty(), "the world must have at least one village");
    }

    private static <T extends Identifiable> Map<String, T> index(Collection<T> items) {
        Preconditions.requireNonNull(items, "items");
        LinkedHashMap<String, T> byId = new LinkedHashMap<>();
        for (T item : items) {
            byId.put(item.getId(), item);
        }
        return byId;
    }

    public List<Region> getRegions() {
        return List.copyOf(regions.values());
    }

    public Region getRegion(String id) {
        Region region = regions.get(id);
        Preconditions.require(region != null, "unknown region: " + id);
        return region;
    }

    public List<Village> getVillages() {
        return List.copyOf(villages.values());
    }

    public Village getVillage(String id) {
        Village village = villages.get(id);
        Preconditions.require(village != null, "unknown village: " + id);
        return village;
    }

    /**
     * Days needed to travel from one village to another; zero when they coincide.
     */
    public int travelDays(String from, String to) {
        getVillage(from);
        getVillage(to);
        if (from.equals(to)) {
            return 0;
        }
        return routeDays.getOrDefault(routeKey(from, to), DEFAULT_TRAVEL_DAYS);
    }

    /**
     * Every village the apothecary could move to from the given one.
     */
    public List<Village> villagesReachableFrom(String from) {
        getVillage(from);
        return villages.values().stream()
                .filter(village -> !village.getId().equals(from))
                .toList();
    }

    /**
     * Canonical, order-independent key for the route between two villages.
     */
    public static String routeKey(String a, String b) {
        return a.compareTo(b) <= 0 ? a + "|" + b : b + "|" + a;
    }
}

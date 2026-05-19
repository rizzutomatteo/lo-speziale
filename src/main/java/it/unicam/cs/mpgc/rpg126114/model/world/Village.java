package it.unicam.cs.mpgc.rpg126114.model.world;

import it.unicam.cs.mpgc.rpg126114.model.AbstractEntity;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.List;

/**
 * A place the apothecary can travel to.
 *
 * <p>Each village belongs to a region, sells a fixed catalogue of ingredients at
 * its market and tends to suffer from a handful of local ailments, which is what
 * gives the patients found there their flavour.
 */
public final class Village extends AbstractEntity {

    private final String name;
    private final String description;
    private final String regionId;
    private final List<String> marketIngredients;
    private final List<String> localAilments;

    public Village(String id, String name, String description, String regionId,
                   List<String> marketIngredients, List<String> localAilments) {
        super(id);
        this.name = Preconditions.requireNonBlank(name, "name");
        this.description = Preconditions.requireNonBlank(description, "description");
        this.regionId = Preconditions.requireNonBlank(regionId, "regionId");
        this.marketIngredients = List.copyOf(Preconditions.requireNonNull(marketIngredients, "marketIngredients"));
        this.localAilments = List.copyOf(Preconditions.requireNonNull(localAilments, "localAilments"));
        Preconditions.require(!this.localAilments.isEmpty(), "a village must have at least one local ailment");
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRegionId() {
        return regionId;
    }

    /** The ids of the ingredients the village market offers. */
    public List<String> getMarketIngredients() {
        return marketIngredients;
    }

    /** The ids of the ailments that commonly afflict people here. */
    public List<String> getLocalAilments() {
        return localAilments;
    }
}

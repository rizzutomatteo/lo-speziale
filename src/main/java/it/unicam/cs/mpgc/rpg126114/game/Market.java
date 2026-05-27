package it.unicam.cs.mpgc.rpg126114.game;

import it.unicam.cs.mpgc.rpg126114.content.ContentRepository;
import it.unicam.cs.mpgc.rpg126114.model.alchemy.Ingredient;
import it.unicam.cs.mpgc.rpg126114.model.character.Apothecary;
import it.unicam.cs.mpgc.rpg126114.model.economy.Coins;
import it.unicam.cs.mpgc.rpg126114.model.progression.SkillType;
import it.unicam.cs.mpgc.rpg126114.model.world.Village;
import it.unicam.cs.mpgc.rpg126114.util.Preconditions;

import java.util.List;

/**
 * Sells the ingredients a village offers, at a price that scales with their
 * rarity. Buying ingredients sharpens the apothecary's herbalism.
 */
public final class Market {

    private static final int HERBALISM_EXPERIENCE = 4;

    private final ContentRepository content;

    public Market(ContentRepository content) {
        this.content = Preconditions.requireNonNull(content, "content");
    }

    public List<Ingredient> waresOf(Village village) {
        return village.getMarketIngredients().stream().map(content::getIngredient).toList();
    }

    public Coins priceOf(Ingredient ingredient) {
        return ingredient.getBasePrice().scaledBy(ingredient.getRarity().getPriceMultiplier());
    }

    public PurchaseResult buy(Apothecary apothecary, Village village, String ingredientId) {
        Preconditions.requireNonNull(apothecary, "apothecary");
        Preconditions.requireNonNull(village, "village");
        if (!village.getMarketIngredients().contains(ingredientId)) {
            return PurchaseResult.failure("Qui non si vende questo ingrediente.");
        }
        Ingredient ingredient = content.getIngredient(ingredientId);
        Coins price = priceOf(ingredient);
        if (!apothecary.canAfford(price)) {
            return PurchaseResult.failure("Non hai abbastanza fiorini.");
        }
        apothecary.spend(price);
        apothecary.getInventory().addIngredient(ingredientId, 1);
        apothecary.train(SkillType.ERBORISTERIA, HERBALISM_EXPERIENCE);
        return PurchaseResult.success("Hai acquistato " + ingredient.getName() + " per " + price + ".");
    }

    /**
     * The outcome of an attempt to buy, with a message for the player.
     */
    public record PurchaseResult(boolean success, String message) {

        static PurchaseResult success(String message) {
            return new PurchaseResult(true, message);
        }

        static PurchaseResult failure(String message) {
            return new PurchaseResult(false, message);
        }
    }
}

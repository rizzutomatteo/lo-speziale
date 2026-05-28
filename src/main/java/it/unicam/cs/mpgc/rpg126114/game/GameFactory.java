package it.unicam.cs.mpgc.rpg126114.game;

import it.unicam.cs.mpgc.rpg126114.content.ContentRepository;
import it.unicam.cs.mpgc.rpg126114.model.character.Apothecary;
import it.unicam.cs.mpgc.rpg126114.model.economy.Coins;
import it.unicam.cs.mpgc.rpg126114.model.inventory.Inventory;
import it.unicam.cs.mpgc.rpg126114.model.inventory.RecipeBook;
import it.unicam.cs.mpgc.rpg126114.model.world.GameClock;
import it.unicam.cs.mpgc.rpg126114.util.RandomSource;

import java.util.List;

/**
 * Builds the starting situation of a new playthrough: a novice apothecary with a
 * little money, a handful of basic recipes and remedies, and a place to begin.
 *
 * <p>This is the one spot that decides how a game opens, so the opening scenario
 * can be changed here without affecting the rest of the game.
 */
public final class GameFactory {

    private static final String STARTING_VILLAGE = "valmorigen";
    private static final List<String> STARTING_RECIPES =
            List.of("r_salice", "r_camomilla", "r_menta", "r_genziana", "r_carbone");

    private GameFactory() {
    }

    public static Game newGame(ContentRepository content, RandomSource random, String apothecaryName) {
        Apothecary apothecary = new Apothecary(apothecaryName);
        apothecary.earn(Coins.of(40));

        RecipeBook book = apothecary.getRecipeBook();
        STARTING_RECIPES.forEach(book::learn);

        Inventory inventory = apothecary.getInventory();
        inventory.addRemedy("infuso_salice", 1);
        inventory.addRemedy("tisana_camomilla", 1);
        inventory.addRemedy("decotto_genziana", 1);
        inventory.addRemedy("cataplasma_menta", 1);
        inventory.addRemedy("polvere_carbone", 1);
        inventory.addIngredient("salice", 2);
        inventory.addIngredient("camomilla", 2);
        inventory.addIngredient("menta", 2);
        inventory.addIngredient("genziana", 1);
        inventory.addIngredient("miele", 3);
        inventory.addIngredient("carbone", 2);

        GameState state = new GameState(apothecary, new GameClock(), STARTING_VILLAGE);
        return new Game(content, state, random);
    }
}

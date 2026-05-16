package it.unicam.cs.mpgc.rpg126114.model.inventory;

/**
 * What an apothecary carries: a stock of ingredients and a stock of prepared
 * remedies, kept separate because they are used in different ways.
 */
public final class Inventory {

    private final Stockpile ingredients;
    private final Stockpile remedies;

    public Inventory() {
        this(new Stockpile(), new Stockpile());
    }

    public Inventory(Stockpile ingredients, Stockpile remedies) {
        this.ingredients = ingredients;
        this.remedies = remedies;
    }

    public Stockpile getIngredients() {
        return ingredients;
    }

    public Stockpile getRemedies() {
        return remedies;
    }

    public void addIngredient(String ingredientId, int quantity) {
        ingredients.add(ingredientId, quantity);
    }

    public void addRemedy(String remedyId, int quantity) {
        remedies.add(remedyId, quantity);
    }
}

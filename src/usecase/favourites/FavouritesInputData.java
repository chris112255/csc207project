package usecase.favourites;

import entity.Recipe;

/**
 * The Input Data for the Favourites Use Case.
 */
public class FavouritesInputData {

    private final Recipe recipe;

    public FavouritesInputData(Recipe recipe) {
        this.recipe = recipe;
    }

    Recipe getRecipe() {
        return recipe;
    }
}
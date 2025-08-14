package usecase.favourites;

import entity.Recipe;

/**
 * DAO for the Favourites Use Case.
 */
public interface FavouritesUserDataAccessInterface {

    /**
     * Checks if the given recipe name exists.
     * @param recipeName the recipe name to look for
     * @return true if a recipe with the given name exists; false otherwise
     */
    boolean existsByName(String recipeName);

    /**
     * Saves the recipe.
     * @param recipe the recipe to save
     */
    void save(Recipe recipe);

    /**
     * Removes the recipe.
     * @param recipe the recipe to remove
     */
    void remove(Recipe recipe);
}
package interface_adapter.favourites;

import entity.Recipe;
import usecase.favourites.FavouritesInputBoundary;
import usecase.favourites.FavouritesInputData;

/**
 * The controller for the Favourites Use Case.
 */
public class FavouritesController {

    private final FavouritesInputBoundary favouritesUseCaseInteractor;

    public FavouritesController(FavouritesInputBoundary favouritesUseCaseInteractor) {
        this.favouritesUseCaseInteractor = favouritesUseCaseInteractor;
    }

    /**
     * Executes the Add to Favourites Use Case.
     * @param recipe the recipe to add to favourites
     */
    public void executeAdd(Recipe recipe) {
        final FavouritesInputData favouritesInputData = new FavouritesInputData(recipe);
        favouritesUseCaseInteractor.executeAdd(favouritesInputData);
    }

    /**
     * Executes the Remove from Favourites Use Case.
     * @param recipe the recipe to remove from favourites
     */
    public void executeRemove(Recipe recipe) {
        final FavouritesInputData favouritesInputData = new FavouritesInputData(recipe);
        favouritesUseCaseInteractor.executeRemove(favouritesInputData);
    }
}
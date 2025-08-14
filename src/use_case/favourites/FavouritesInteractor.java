package use_case.favourites;

import entity.Recipe;

/**
 * Favourites Interactor.
 */
public class FavouritesInteractor implements FavouritesInputBoundary {
    private final FavouritesUserDataAccessInterface userDataAccessObject;
    private final FavouritesOutputBoundary favouritesPresenter;

    public FavouritesInteractor(FavouritesUserDataAccessInterface favouritesDataAccessInterface,
                                FavouritesOutputBoundary favouritesOutputBoundary) {
        this.userDataAccessObject = favouritesDataAccessInterface;
        this.favouritesPresenter = favouritesOutputBoundary;
    }

    @Override
    public void executeAdd(FavouritesInputData favouritesInputData) {
        final Recipe recipe = favouritesInputData.getRecipe();
        if (userDataAccessObject.existsByName(recipe.getName())) {
            favouritesPresenter.prepareFailView("Recipe already in favourites: " + recipe.getName());
        }
        else {
            userDataAccessObject.save(recipe);
            final FavouritesOutputData favouritesOutputData = new FavouritesOutputData(recipe.getName(), false);
            favouritesPresenter.prepareSuccessView(favouritesOutputData);
        }
    }

    @Override
    public void executeRemove(FavouritesInputData favouritesInputData) {
        final Recipe recipe = favouritesInputData.getRecipe();
        if (!userDataAccessObject.existsByName(recipe.getName())) {
            favouritesPresenter.prepareFailView("Recipe not in favourites: " + recipe.getName());
        }
        else {
            userDataAccessObject.remove(recipe);
            final FavouritesOutputData favouritesOutputData = new FavouritesOutputData(recipe.getName(), false);
            favouritesPresenter.prepareSuccessView(favouritesOutputData);
        }
    }
}
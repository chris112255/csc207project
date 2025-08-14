package interface_adapter.favourites;

import use_case.favourites.FavouritesOutputBoundary;
import use_case.favourites.FavouritesOutputData;

/**
 * The Presenter for the Favourites Use Case.
 */
public class FavouritesPresenter implements FavouritesOutputBoundary {

    private final FavouritesViewModel favouritesViewModel;

    public FavouritesPresenter(FavouritesViewModel favouritesViewModel) {
        this.favouritesViewModel = favouritesViewModel;
    }

    @Override
    public void prepareSuccessView(FavouritesOutputData response) {
        final FavouritesState favouritesState = favouritesViewModel.getState();
        favouritesState.setMessage("Recipe operation successful: " + response.getRecipeName());
        favouritesState.setFavouritesError(null);
        this.favouritesViewModel.setState(favouritesState);
        this.favouritesViewModel.firePropertyChanged();

    }

    @Override
    public void prepareFailView(String error) {
        final FavouritesState favouritesState = favouritesViewModel.getState();
        favouritesState.setFavouritesError(error);
        favouritesState.setMessage(null);
        favouritesViewModel.setState(favouritesState);
        favouritesViewModel.firePropertyChanged();
    }
}
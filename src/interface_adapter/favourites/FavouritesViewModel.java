package interface_adapter.favourites;

import interface_adapter.ViewModel;

/**
 * The View Model for the Favourites Use Case.
 */
public class FavouritesViewModel extends ViewModel<FavouritesState> {

    public FavouritesViewModel() {
        super("favourites");
        setState(new FavouritesState());
    }
}
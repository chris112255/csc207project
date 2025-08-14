package interface_adapter.favourites;

/**
 * The state for the Favourites View Model.
 */
public class FavouritesState {
    private String message = "";
    private String favouritesError;

    public String getMessage() {
        return message;
    }

    public String getFavouritesError() {
        return favouritesError;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFavouritesError(String favouritesError) {
        this.favouritesError = favouritesError;
    }
}
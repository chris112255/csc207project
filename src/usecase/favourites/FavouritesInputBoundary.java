package usecase.favourites;

/**
 * Input Boundary for actions which are related to favourites.
 */
public interface FavouritesInputBoundary {

    /**
     * Executes the add to favourites use case.
     * @param favouritesInputData the input data
     */
    void executeAdd(FavouritesInputData favouritesInputData);

    /**
     * Executes the remove from favourites use case.
     * @param favouritesInputData the input data
     */
    void executeRemove(FavouritesInputData favouritesInputData);
}
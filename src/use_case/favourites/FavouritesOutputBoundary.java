package use_case.favourites;

/**
 * The output boundary for the Favourites Use Case.
 */
public interface FavouritesOutputBoundary {
    /**
     * Prepares the success view for the Favourites Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(FavouritesOutputData outputData);

    /**
     * Prepares the failure view for the Favourites Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
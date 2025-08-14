package use_case.favourites;

/**
 * Output Data for the Favourites Use Case.
 */
public class FavouritesOutputData {

    private final String recipeName;
    private final boolean useCaseFailed;

    public FavouritesOutputData(String recipeName, boolean useCaseFailed) {
        this.recipeName = recipeName;
        this.useCaseFailed = useCaseFailed;
    }

    public String getRecipeName() {
        return recipeName;
    }
}
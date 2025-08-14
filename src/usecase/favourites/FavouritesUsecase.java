package usecase.favourites;

import api.EdamamRecipeSearchGateway;
import entity.Recipe;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 Manages saving and loading favourite recipes locally using file storage
 */
public class FavouritesUsecase {
    private static final String favouritesFile = "favourites.txt";
    private List<Recipe> favourites;

    public FavouritesUsecase() {
        this.favourites = new ArrayList<>();
        loadfavourites();
    }

    /**
    Add a recipe to favourites and save to file
     */
    public void addToFavourites(Recipe recipe) {
        // Check if recipe is already in favourites
        for (Recipe favourite : favourites) {
            if (favourite.getName().equals(recipe.getName()) &&
                    favourite.getSourceUrl().equals(recipe.getSourceUrl())) {
                System.out.println("Recipe already in favourites: " + recipe.getName());
                return;
            }
        }

        favourites.add(recipe);
        savefavourites();
        System.out.println("Added to favourites: " + recipe.getName());
    }

    /**
    Remove a recipe from favourites
     */
    public void removeFromFavourites(Recipe recipe) {
        favourites.removeIf(favourite ->
                favourite.getName().equals(recipe.getName()) &&
                        favourite.getSourceUrl().equals(recipe.getSourceUrl())
        );
        savefavourites();
        System.out.println("Removed from favourites: " + recipe.getName());
    }

    /**
    Get all favourite recipes
     */
    public List<Recipe> getFavourites() {
        return new ArrayList<>(favourites);
    }

    /**
    Check if a recipe is already in favourites
     */
    public boolean isFavourite(Recipe recipe) {
        return favourites.stream().anyMatch(favourite ->
                favourite.getName().equals(recipe.getName()) &&
                        favourite.getSourceUrl().equals(recipe.getSourceUrl())
        );
    }

    /**
    Save favourites to local file
     */
    private void savefavourites() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(favouritesFile))) {
            for (Recipe recipe : favourites) {
                writer.println(recipe.getName() + "|" + recipe.getUri());
            }
        } catch (IOException e) {
            System.err.println("Error saving favourites: " + e.getMessage());
        }
    }

    /**
    Load favourites from local file
     */
    private void loadfavourites() {
        File file = new File(favouritesFile);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 3);

                EdamamRecipeSearchGateway gateway = new EdamamRecipeSearchGateway();

                if (parts.length == 2) {
                    String title = parts[0];
                    String uri = parts[1];
                    Recipe recipe = gateway.recipeLookup(uri);

                    favourites.add(recipe);
                }
            }
        } catch (IOException e) {
        }
    }
}
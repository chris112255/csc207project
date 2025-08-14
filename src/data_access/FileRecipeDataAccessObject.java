package data_access;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import api.EdamamRecipeSearchGateway;
import entity.Recipe;
import use_case.favourites.FavouritesUserDataAccessInterface;

/**
 * DAO for recipe data implemented using a File to persist the data.
 */
public class FileRecipeDataAccessObject implements FavouritesUserDataAccessInterface {

    private static final String FAVOURITES_FILE = "favourites.txt";
    private final List<Recipe> favourites = new ArrayList<Recipe>();
    private final EdamamRecipeSearchGateway gateway = new EdamamRecipeSearchGateway();

    public FileRecipeDataAccessObject() {
        loadFavourites();
    }

    @Override
    public boolean existsByName(String recipeName) {
        for (Recipe recipe : favourites) {
            if (recipe.getName().equals(recipeName)) {
                return true;
            }
        }
        return false;
    }

    public List<Recipe> getAllFavourites() {
        return new ArrayList<>(favourites);
    }

    public boolean existsByUri(String uri) {
        for (Recipe recipe : favourites) {
            if (recipe.getUri().equals(uri)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void save(Recipe recipe) {
        System.out.println("FileRecipeDataAccessObject.save() called with: " + recipe.getName());
        favourites.add(recipe);
        saveFavourites();
        System.out.println("Recipe saved. Total favourites: " + favourites.size());
    }

    @Override
    public void remove(Recipe recipe) {
        for (int i = 0; i < favourites.size(); i++) {
            Recipe favourite = favourites.get(i);
            if (favourite.getName().equals(recipe.getName()) &&
                    favourite.getSourceUrl().equals(recipe.getSourceUrl())) {
                favourites.remove(i);
                break;
            }
        }
        saveFavourites();
    }

    private void saveFavourites() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(FAVOURITES_FILE));
            for (Recipe recipe : favourites) {
                writer.println(recipe.getName() + "|" + recipe.getUri());
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving favourites: " + e.getMessage());
        }
    }

    private void loadFavourites() {
        File file = new File(FAVOURITES_FILE);
        if (!file.exists()) {
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    String title = parts[0];
                    String uri = parts[1];
                    Recipe recipe = gateway.recipeLookup(uri);
                    if (recipe != null) {
                        favourites.add(recipe);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error loading favourites: " + e.getMessage());
        }
    }
}
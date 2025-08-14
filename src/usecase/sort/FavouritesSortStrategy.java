package usecase.sort;

import entity.Recipe;
import usecase.favourites.FavouritesUsecase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavouritesSortStrategy implements SortStrategy {

    private FavouritesUsecase favouritesUsecase = new FavouritesUsecase();

    @Override
    public void sort(List<Recipe> recipes) {
        ArrayList<Recipe> favs = (ArrayList<Recipe>) favouritesUsecase.getFavourites();
        HashMap<String, Integer> cuisineCounter = new HashMap<>();
        HashMap<String, Integer> mainIngredientCounter = new HashMap<>();
        HashMap<String, Integer> dietCounter = new HashMap<>();
        HashMap<String, Integer> ingredientsCounter = new HashMap<>();
        HashMap<String, Integer> dishTypeCounter = new HashMap<>();
        for (Recipe r : favs) {
            String cuisine = r.getCuisineType();
            String mainIngredient = r.getMainIngredient();
            List<String> dietTypes = r.getDietType();
            List<String> ingredients = r.getIngredients();
            String dishType = r.getDishType();
            if (cuisineCounter.containsKey(cuisine)) {
                cuisineCounter.put(cuisine, cuisineCounter.get(cuisine) + 1);
            }
            else {
                cuisineCounter.put(cuisine, 1);
            }
            if (mainIngredientCounter.containsKey(mainIngredient)) {
                mainIngredientCounter.put(mainIngredient, mainIngredientCounter.get(mainIngredient) + 1);
            }
            else {
                mainIngredientCounter.put(mainIngredient, 1);
            }
            if (dishTypeCounter.containsKey(dishType)) {
                dietCounter.put(dishType, dietCounter.get(dishType) + 1);
            }
            else {
                dietCounter.put(dishType, 1);
            }
            for (String d : dietTypes) {
                if (dietCounter.containsKey(d)) {
                    dietCounter.put(d, dietCounter.get(d) + 1);
                }
                else {
                    dietCounter.put(d, 1);
                }
            }
            for (String i : ingredients) {
                if (ingredientsCounter.containsKey(i)) {
                    ingredientsCounter.put(i, ingredientsCounter.get(i) + 1);
                }
                else {
                    ingredientsCounter.put(i, 1);
                }
            }
        }
        HashMap<Recipe, Integer> recipeScores = new HashMap<>();
        for (Recipe r : recipes) {
            Integer recipeScore = 0;
            if (cuisineCounter.get(r.getCuisineType()) != null) {
                recipeScore += cuisineCounter.get(r.getCuisineType()) * 10;
            }
            if (mainIngredientCounter.get(r.getMainIngredient()) != null) {
                recipeScore += mainIngredientCounter.get(r.getMainIngredient()) * 12;
            }
            if (dishTypeCounter.get(r.getDishType()) != null) {
                recipeScore += dishTypeCounter.get(r.getDishType()) * 6;
            }
            for (String d : r.getDietType()) {
                if (dietCounter.containsKey(d)) {
                    recipeScore += dietCounter.get(d) * 8;
                }
            }
            for (String i : r.getIngredients()) {
                if (ingredientsCounter.containsKey(i)) {
                    recipeScore += ingredientsCounter.get(i) * 4;
                }
            }
            recipeScores.put(r, recipeScore);
        }
        List<Map.Entry<Recipe, Integer>> recipesByFavs = new ArrayList<>(recipeScores.entrySet());
        recipesByFavs.sort(Map.Entry.comparingByValue());
        List sortedRecipes = new ArrayList();
        for (Map.Entry<Recipe, Integer> entry : recipesByFavs) {
            sortedRecipes.add(entry.getKey());
        }
        recipes.clear();
        recipes.addAll(sortedRecipes);
    }

    @Override
    public void setTestCriteria(Object testCriteria) {
        if (testCriteria instanceof FavouritesUsecase) {
            this.favouritesUsecase = (FavouritesUsecase) testCriteria;
        }
    }
}

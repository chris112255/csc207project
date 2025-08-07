package usecase.sort;

import entity.Recipe;
import usecase.FavouritesUsecase;

import java.util.*;
import java.util.Map.Entry;

public class RecipeSorterUseCase {

    private final String sortingCriteria;
    FavouritesUsecase favouritesUsecase = new FavouritesUsecase();

    public RecipeSorterUseCase(String sortingCriteria) {
        this.sortingCriteria = sortingCriteria;
    }

    public void sortRecipes(List<Recipe> recipes) {
        if ("ingredients".equals(sortingCriteria)) {
            Collections.sort(recipes, Comparator.comparing(Recipe::getIngredientCount));
        }
        else if ("time".equals(sortingCriteria)) {
            Collections.sort(recipes, Comparator.comparing(Recipe::getPrepTime));
        }
        else if ("caloriesAscending".equals(sortingCriteria)) {
            Collections.sort(recipes, Comparator.comparing(Recipe::getNutriCalories));
        }
        else if ("caloriesDescending".equals(sortingCriteria)) {
            Collections.sort(recipes, Comparator.comparing(Recipe::getNutriCalories).reversed());
        }
        else if ("proteinAscending".equals(sortingCriteria)) {
            Collections.sort(recipes, Comparator.comparing(Recipe::getNutriProtein));
        }
        else if ("proteinDescending".equals(sortingCriteria)) {
            Collections.sort(recipes, Comparator.comparing(Recipe::getNutriProtein).reversed());
        }
        else if ("fatAscending".equals(sortingCriteria)) {
            Collections.sort(recipes, Comparator.comparing(Recipe::getNutriFat));
        }
        else if ("fatDescending".equals(sortingCriteria)) {
            Collections.sort(recipes, Comparator.comparing(Recipe::getNutriFat).reversed());
        }
        else if ("sugarAscending".equals(sortingCriteria)) {
            Collections.sort(recipes, Comparator.comparing(Recipe::getNutriSugar));
        }
        else if ("sugarDescending".equals(sortingCriteria)) {
            Collections.sort(recipes, Comparator.comparing(Recipe::getNutriSugar).reversed());
        }
        else if ("sodiumAscending".equals(sortingCriteria)) {
            Collections.sort(recipes, Comparator.comparing(Recipe::getNutriSodium));
        }
        else if ("sodiumDescending".equals(sortingCriteria)) {
            Collections.sort(recipes, Comparator.comparing(Recipe::getNutriSodium).reversed());
        }
        else if (sortingCriteria.startsWith("type")) {
            List<Recipe> sorted = typeSorter(recipes, sortingCriteria.substring(4));
            recipes.clear();
            recipes.addAll(sorted);
        }
        else if ("favs".equals(sortingCriteria)) {
            List<Recipe> sorted = favSorter(recipes);
            recipes.clear();
            recipes.addAll(sorted);
        }
    }

    private List<Recipe> typeSorter(List<Recipe> recipes, String type) {
        ArrayList<Recipe> priority = new ArrayList<>();
        ArrayList<Recipe> nonPriority = new ArrayList<>();
        for (Recipe r : recipes) {
            if (r.getDishType().equals(type) || r.getMealType().equals(type) ||
                    r.getCuisineType().equals(type) || r.getDietType().equals(type)) {
                priority.add(r);
            }
            else {nonPriority.add(r);}
        }
        priority.addAll(nonPriority);
        return priority;
    }

    private List<Recipe> favSorter(List<Recipe> recipes) {
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
            recipeScore += cuisineCounter.get(r.getCuisineType()) * 10;
            recipeScore += mainIngredientCounter.get(r.getMainIngredient()) * 12;
            recipeScore += dishTypeCounter.get(r.getDishType()) * 6;
            for (String d : r.getDietType()) {
                recipeScore += dietCounter.get(d) * 8;
            }
            for (String i : r.getIngredients()) {
                recipeScore += ingredientsCounter.get(i);
            }
            recipeScores.put(r, recipeScore);
        }
        List<Entry<Recipe, Integer>> recipesByFavs = new ArrayList<>(recipeScores.entrySet());
        recipesByFavs.sort(Map.Entry.comparingByValue());
        List sortedRecipes = new ArrayList();
        for (Map.Entry<Recipe, Integer> entry : recipesByFavs) {
            sortedRecipes.add(entry.getKey());
        }
        return sortedRecipes;
    }
}

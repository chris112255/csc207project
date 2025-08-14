package use_case.sort;

import entity.Recipe;
import use_case.favourites.FavouritesUsecase;

import java.util.*;
import java.util.Map.Entry;

public class RecipeSorterUseCase {

    private final String sortingCriteria;
    public FavouritesUsecase favouritesUsecase = new FavouritesUsecase();
    private final Map<String, SortStrategy> strategies = new HashMap<>();

    public RecipeSorterUseCase(String sortingCriteria) {

        this.sortingCriteria = sortingCriteria;
        strategies.put("ingredients", new IngredientCountSortStrategy());
        strategies.put("time", new TimeSortStrategy());
        strategies.put("caloriesAscending", new CaloriesAscendingSortStrategy());
        strategies.put("caloriesDescending", new CaloriesDescendingSortStrategy());
        strategies.put("proteinAscending", new ProteinAscendingSortStrategy());
        strategies.put("proteinDescending", new ProteinDescendingSortStrategy());
        strategies.put("fatAscending", new FatAscendingSortStrategy());
        strategies.put("fatDescending", new FatDescendingSortStrategy());
        strategies.put("sugarAscending", new SugarAscendingSortStrategy());
        strategies.put("sugarDescending", new SugarDescendingSortStrategy());
        strategies.put("sodiumAscending", new SodiumAscendingSortStrategy());
        strategies.put("sodiumDescending", new SodiumDescendingSortStrategy());
    }

    public void sortRecipes(List<Recipe> recipes) {
        SortStrategy strategy = strategies.get(sortingCriteria);
        if (strategy != null) {
            strategy.sort(recipes);
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
        System.out.println("=== Sorting by type: " + type + " ===");

        ArrayList<Recipe> priority = new ArrayList<>();
        ArrayList<Recipe> nonPriority = new ArrayList<>();

        for (Recipe r : recipes) {
            boolean matches = type.equalsIgnoreCase(r.getDishType())
                    || type.equalsIgnoreCase(r.getMealType())
                    || type.equalsIgnoreCase(r.getCuisineType())
                    || r.getDietType().stream().anyMatch(d -> d.equalsIgnoreCase(type));

            System.out.println(r.getName() +
                    " | Dish: " + r.getDishType() +
                    " | Meal: " + r.getMealType() +
                    " | Cuisine: " + r.getCuisineType() +
                    " | Diets: " + r.getDietType() +
                    " | Matches: " + matches);

            if (matches) priority.add(r);
            else nonPriority.add(r);
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
        List<Entry<Recipe, Integer>> recipesByFavs = new ArrayList<>(recipeScores.entrySet());
        recipesByFavs.sort(Map.Entry.comparingByValue());
        List sortedRecipes = new ArrayList();
        for (Map.Entry<Recipe, Integer> entry : recipesByFavs) {
            sortedRecipes.add(entry.getKey());
        }
        return sortedRecipes;
    }
}

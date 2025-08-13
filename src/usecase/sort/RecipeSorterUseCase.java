package usecase.sort;

import entity.Recipe;

import java.util.*;

public class RecipeSorterUseCase {

    private final String sortingCriteria;
    public final Map<String, SortStrategy> strategies = new HashMap<>();

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
        strategies.put("favs", new FavouritesSortStrategy());
    }

    public void sortRecipes(List<Recipe> recipes) {
        SortStrategy strategy = strategies.get(sortingCriteria);
        if (strategy != null) {
            strategy.sort(recipes);
        }
    }
}

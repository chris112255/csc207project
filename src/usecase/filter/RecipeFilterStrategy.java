package usecase.filter;

import entity.Recipe;
import java.util.List;
import java.util.Map;

/**
 * Strategy interface for filtering recipes.
 */
public interface RecipeFilterStrategy {
    List<Recipe> applyFilters(List<Recipe> recipes, Map<String, String> filters);
}
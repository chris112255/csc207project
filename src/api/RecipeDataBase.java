package api;

import entity.Recipe;
import java.util.List;
import java.util.Map;

public interface RecipeDataBase {
    List<Recipe> searchRecipes(Map<String, String> filters);
    void saveRecipe(Recipe recipe);
    List<Recipe> getSavedRecipes();
}

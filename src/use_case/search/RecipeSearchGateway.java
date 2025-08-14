// usecase/search/RecipeSearchGateway.java
package use_case.search;

import entity.Recipe;
import java.util.List;
import java.util.Map;

public interface RecipeSearchGateway {
    List<Recipe> searchRecipes(Map<String, String> filters) throws Exception;

    String fetchRecipes(Map<String, String> filters);
}

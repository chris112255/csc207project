package use_case.search;

import entity.Recipe;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchRecipesUseCase {

    private final RecipeSearchGateway gateway;

    public SearchRecipesUseCase(RecipeSearchGateway gateway) {
        this.gateway = gateway;
    }

    public List<Recipe> execute(Map<String, String> filters) {
        System.out.println("Searching recipes using filters: " + filters);
        try {
            return gateway.searchRecipes(filters);
        } catch (Exception e) {
            System.err.println("Error executing recipe search: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

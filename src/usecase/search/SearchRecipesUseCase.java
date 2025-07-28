package usecase.search;

import entity.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchRecipesUseCase {

    private final RecipeSearchGateway gateway;

    public SearchRecipesUseCase(RecipeSearchGateway gateway) {
        this.gateway = gateway;
    }

    public List<Recipe> execute(Map<String, String> filters) {
        try {
            String json = gateway.fetchRecipes(filters);
            return parseRecipes(json);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Recipe> parseRecipes(String jsonResponse) {
        List<Recipe> recipes = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray hits = json.optJSONArray("hits");
            if (hits == null) return recipes;

            for (int i = 0; i < hits.length(); i++) {
                JSONObject recipeJson = hits.getJSONObject(i).getJSONObject("recipe");
                String title = recipeJson.getString("label");
                String imageUrl = recipeJson.optString("image", "");
                String sourceUrl = recipeJson.optString("url", "");
                recipes.add(new Recipe(title, imageUrl, sourceUrl));
            }

        } catch (Exception e) {
            System.err.println("Parse error: " + e.getMessage());
        }

        return recipes;
    }
}

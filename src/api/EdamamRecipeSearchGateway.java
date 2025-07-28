// api/EdamamRecipeSearchGateway.java
package api;

import entity.Recipe;
import entity.Nutrients;
import org.json.JSONArray;
import org.json.JSONObject;
import usecase.search.RecipeSearchGateway;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class EdamamRecipeSearchGateway implements RecipeSearchGateway {

    private static final String APP_ID = "30597585";
    private static final String APP_KEY = "51e24339cd8c63f545b3c9dce37082b8";
    private String sourceUrl;

    @Override
    public List<Recipe> searchRecipes(Map<String, String> filters) throws Exception {
        String apiUrl = buildApiUrl(filters);
        try {
            String jsonResponse = sendApiRequest(apiUrl);
            return parseRecipes(jsonResponse);
        } catch (Exception e) {
            System.err.println("Gateway Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public String fetchRecipes(Map<String, String> filters) {
        return "";
    }

    private String buildApiUrl(Map<String, String> params) throws Exception {
        StringBuilder urlBuilder = new StringBuilder("https://api.edamam.com/api/recipes/v2?type=public");
        urlBuilder.append("&app_id=").append(APP_ID);
        urlBuilder.append("&app_key=").append(APP_KEY);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value != null && !value.isEmpty()) {
                if (key.equals("q")) {
                    urlBuilder.append("&q=").append(URLEncoder.encode(value, "UTF-8"));
                } else if (key.equals("diet")) {
                    urlBuilder.append("&diet=").append(URLEncoder.encode(value, "UTF-8"));
                } else if (key.equals("calories")) {
                    urlBuilder.append("&calories=").append(URLEncoder.encode(value, "UTF-8"));
                } else {
                    String nutrientCode = getNutrientCode(key);
                    if (nutrientCode != null) {
                        urlBuilder.append("&nutrients%5B").append(nutrientCode)
                                .append("%5D=0-").append(URLEncoder.encode(value, "UTF-8"));
                    }
                }
            }
        }
        return urlBuilder.toString();
    }

    private String sendApiRequest(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                conn.getResponseCode() == 200 ? conn.getInputStream() : conn.getErrorStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) response.append(line);
        reader.close();

        return response.toString();
    }

    private List<Recipe> parseRecipes(String jsonResponse) {
        List<Recipe> recipes = new ArrayList<>();
        JSONObject json = new JSONObject(jsonResponse);

        JSONArray hits = json.optJSONArray("hits");
        if (hits == null) return recipes;

        for (int i = 0; i < hits.length(); i++) {
            JSONObject recipeJson = hits.getJSONObject(i).getJSONObject("recipe");

            String name = recipeJson.optString("label");
            String mainIngredient = "N/A";
            List<String> ingredients = new ArrayList<>();
            JSONArray ingredientLines = recipeJson.optJSONArray("ingredientLines");
            if (ingredientLines != null) {
                for (int j = 0; j < ingredientLines.length(); j++) {
                    ingredients.add(ingredientLines.getString(j));
                }
                if (!ingredients.isEmpty()) mainIngredient = ingredients.get(0);
            }

            String instructions = "See full recipe online.";
            int ingredientCount = ingredients.size();
            List<String> dietLabels = new ArrayList<>();
            JSONArray diets = recipeJson.optJSONArray("dietLabels");
            if (diets != null) for (int j = 0; j < diets.length(); j++) dietLabels.add(diets.getString(j));

            JSONObject totalNutrients = recipeJson.optJSONObject("totalNutrients");
            Nutrients nutrients = new Nutrients(
                    (int) recipeJson.optDouble("calories", 0),
                    totalNutrients.optJSONObject("PROCNT").optDouble("quantity", 0),
                    totalNutrients.optJSONObject("FAT").optDouble("quantity", 0),
                    totalNutrients.optJSONObject("SUGAR").optDouble("quantity", 0),
                    totalNutrients.optJSONObject("NA").optDouble("quantity", 0)
            );

            double prepTime = recipeJson.optDouble("totalTime", 0);
            String cuisineType = recipeJson.optJSONArray("cuisineType") != null ?
                    recipeJson.getJSONArray("cuisineType").optString(0) : "N/A";
            String mealType = recipeJson.optJSONArray("mealType") != null ?
                    recipeJson.getJSONArray("mealType").optString(0) : "N/A";
            String dishType = recipeJson.optJSONArray("dishType") != null ?
                    recipeJson.getJSONArray("dishType").optString(0) : "N/A";

            recipes.add(new Recipe(name, mainIngredient, ingredients, instructions,
                    ingredientCount, dietLabels, nutrients, prepTime, cuisineType, mealType, dishType, this.sourceUrl));
        }
        return recipes;
    }

    private String getNutrientCode(String filterName) {
        return switch (filterName) {
            case "protein" -> "PROCNT";
            case "fat" -> "FAT";
            case "sugar" -> "SUGAR";
            case "carbohydrates" -> "CHOCDF";
            default -> null;
        };
    }
}

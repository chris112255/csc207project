package api;

import entity.Nutrients;
import entity.Recipe;
import org.json.JSONArray;
import org.json.JSONObject;
import usecase.search.RecipeSearchGateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class EdamamRecipeSearchGateway implements RecipeSearchGateway {

    private static final String APP_ID = "30597585";
    private static final String APP_KEY = "51e24339cd8c63f545b3c9dce37082b8";

    public Recipe recipeLookup(String uri) throws IOException {
        String recipeId = uri.substring(uri.indexOf("#") + 1);
        StringBuilder urlBuilder = new StringBuilder("https://api.edamam.com/api/recipes/v2/");
        urlBuilder.append(recipeId).append("?type=public&app_id=").append(APP_ID);
        urlBuilder.append("&app_key=").append(APP_KEY);
        URL url = new URL(urlBuilder.toString());

        // Open connection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Edamam-Account-User", "utoronto");

        // Check HTTP response
        if (conn.getResponseCode() == 200) {
            // Read the JSON from the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Parse JSON
            JSONObject recipeJson = new JSONObject(response.toString());

            // Example: Access the recipe label
            JSONObject recipeObj = recipeJson.getJSONObject("recipe");

            String name = recipeObj.optString("label");

            String mainIngredient = "N/A";
            List<String> ingredients = new ArrayList<>();
            JSONArray ingredientLines = recipeObj.optJSONArray("ingredientLines");
            if (ingredientLines != null) {
                for (int j = 0; j < ingredientLines.length(); j++) {
                    ingredients.add(ingredientLines.getString(j));
                }
                if (!ingredients.isEmpty()) mainIngredient = ingredients.get(0);
            }

            String instructions = "See full recipe online.";
            int ingredientCount = ingredients.size();

            List<String> dietLabels = new ArrayList<>();
            JSONArray diets = recipeObj.optJSONArray("dietLabels");
            if (diets != null) {
                for (int j = 0; j < diets.length(); j++) {
                    dietLabels.add(diets.getString(j));
                }
            }

            JSONObject totalNutrients = recipeObj.optJSONObject("totalNutrients");

            Nutrients nutrients = new Nutrients(
                    (int) recipeObj.optDouble("calories", 0),
                    totalNutrients.optJSONObject("PROCNT").optDouble("quantity", 0),
                    totalNutrients.optJSONObject("FAT").optDouble("quantity", 0),
                    totalNutrients.optJSONObject("SUGAR").optDouble("quantity", 0),
                    totalNutrients.optJSONObject("NA").optDouble("quantity", 0),
                    totalNutrients.optJSONObject("CHOCDF").optDouble("quantity", 0)
            );

            double prepTime = recipeObj.optDouble("totalTime", 0);
            String cuisineType = recipeObj.optJSONArray("cuisineType") != null ?
                    recipeObj.getJSONArray("cuisineType").optString(0) : "N/A";
            String mealType = recipeObj.optJSONArray("mealType") != null ?
                    recipeObj.getJSONArray("mealType").optString(0) : "N/A";
            String dishType = recipeObj.optJSONArray("dishType") != null ?
                    recipeObj.getJSONArray("dishType").optString(0) : "N/A";
            String sourceUrl = recipeObj.optString("url", "");
            String imageUrl = recipeObj.optString("image", "");

            return new Recipe(
                    name,
                    mainIngredient,
                    ingredients,
                    instructions,
                    ingredientCount,
                    dietLabels,
                    nutrients,
                    prepTime,
                    cuisineType,
                    mealType,
                    dishType,
                    sourceUrl,
                    imageUrl,
                    recipeObj.optString("uri")
            );
        }
        else{
            System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
        }
        return null;
    }

    @Override
    public List<Recipe> searchRecipes(Map<String, String> filters) throws Exception {
        String jsonResponse = fetchRecipes(filters);
        return parseRecipes(jsonResponse);
    }

    @Override
    public String fetchRecipes(Map<String, String> filters) {
        try {
            String apiUrl = buildApiUrl(filters);
            return sendApiRequest(apiUrl);
        } catch (Exception e) {
            System.err.println("Error fetching recipes: " + e.getMessage());
            return "{}";  // empty JSON object fallback
        }
    }

    private String buildApiUrl(Map<String, String> params) throws Exception {
        StringBuilder urlBuilder = new StringBuilder("https://api.edamam.com/api/recipes/v2?type=public");
        urlBuilder.append("&app_id=").append(APP_ID);
        urlBuilder.append("&app_key=").append(APP_KEY);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            if (value != null && !value.isEmpty()) {
                if (key.equals("q") || key.equals("diet") || key.equals("calories")) {
                    urlBuilder.append("&").append(key).append("=")
                            .append(URLEncoder.encode(value, "UTF-8"));
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

    public String sendApiRequest(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        System.out.println("Sending request to " + url);
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

    public List<Recipe> parseRecipes(String jsonResponse) {
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
            if (diets != null) {
                for (int j = 0; j < diets.length(); j++) {
                    dietLabels.add(diets.getString(j));
                }
            }

            JSONObject totalNutrients = recipeJson.optJSONObject("totalNutrients");
            //System.out.println("Total Nutrients");

            Nutrients nutrients = new Nutrients(
                    (int) recipeJson.optDouble("calories", 0),
                    totalNutrients.optJSONObject("PROCNT").optDouble("quantity", 0),
                    totalNutrients.optJSONObject("FAT").optDouble("quantity", 0),
                    totalNutrients.optJSONObject("SUGAR").optDouble("quantity", 0),
                    totalNutrients.optJSONObject("NA").optDouble("quantity", 0),
                    totalNutrients.optJSONObject("CHOCDF").optDouble("quantity", 0)
            );

            double prepTime = recipeJson.optDouble("totalTime", 0);
            String cuisineType = recipeJson.optJSONArray("cuisineType") != null ?
                    recipeJson.getJSONArray("cuisineType").optString(0) : "N/A";
            String mealType = recipeJson.optJSONArray("mealType") != null ?
                    recipeJson.getJSONArray("mealType").optString(0) : "N/A";
            String dishType = recipeJson.optJSONArray("dishType") != null ?
                    recipeJson.getJSONArray("dishType").optString(0) : "N/A";
            String sourceUrl = recipeJson.optString("url", "");
            String imageUrl = recipeJson.optString("image", "");

            recipes.add(new Recipe(
                    name,
                    mainIngredient,
                    ingredients,
                    instructions,
                    ingredientCount,
                    dietLabels,
                    nutrients,
                    prepTime,
                    cuisineType,
                    mealType,
                    dishType,
                    sourceUrl,
                    imageUrl,
                    recipeJson.optString("uri")
            ));
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

package usecase;

import api.EdamamRecipeSearchGateway;
import entity.Nutrients;
import entity.Recipe;
import entity.RecipeBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchRecipesUsecase {
    private static final String APP_ID = "30597585";
    private static final String APP_KEY = "51e24339cd8c63f545b3c9dce37082b8";

    public List<Recipe> execute(Map<String, String> filters) {
        String apiUrl = buildApiUrl(filters);
        System.out.println("API URL: " + apiUrl);

        try {
            String jsonResponse = sendApiRequest(apiUrl);
            List<Recipe> recipes = parseRecipes(jsonResponse);
            System.out.println("Found " + recipes.size() + " recipes");
            return recipes;
        } catch (Exception e) {
            System.err.println("Error in SearchRecipesUseCase: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String buildApiUrl(Map<String, String> params) {
        try {
            StringBuilder urlBuilder = new StringBuilder("https://api.edamam.com/api/recipes/v2?type=public");
            urlBuilder.append("&app_id=").append(APP_ID);
            urlBuilder.append("&app_key=").append(APP_KEY);

            for (Map.Entry<String, String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (value != null && !value.isEmpty()) {
                    // Handle different parameter types
                    if (key.equals("q")) {
                        // General search query
                        urlBuilder.append("&q=").append(URLEncoder.encode(value, "UTF-8"));
                    } else if (key.equals("ingr")) {

                        String currentQuery = "";
                        // Check if we already have a 'q' parameter
                        for (Map.Entry<String, String> searchEntry : params.entrySet()) {
                            if (searchEntry.getKey().equals("q") && searchEntry.getValue() != null
                                    && !searchEntry.getValue().isEmpty()) {
                                currentQuery = searchEntry.getValue() + " ";
                                break;
                            }
                        }
                        // Add ingredient to query instead of using separate parameter
                        if (!currentQuery.contains(value)) {
                            urlBuilder.append("&q=").append(URLEncoder.encode(currentQuery + value, "UTF-8"));
                        }
                    } else if (key.equals("diet")) {
                        // Diet parameter
                        urlBuilder.append("&diet=").append(URLEncoder.encode(value, "UTF-8"));
                    } else if (key.equals("calories")) {
                        // Calorie range parameter
                        if (!value.equals("-")) { // Only add if not empty range
                            urlBuilder.append("&calories=").append(URLEncoder.encode(value, "UTF-8"));
                        }
                    } else {
                        // Nutrient parameters - convert to EDAMAM format
                        String nutrientCode = getNutrientCode(key);
                        if (nutrientCode != null) {
                            // Format as range: 0-maxValue
                            //System.out.println(urlBuilder.append("&nutrients%5B").append(nutrientCode)
                             //       .append("%5D=").append(URLEncoder.encode(value, "UTF-8")).append("+"));
//                            if(key.equals("protein")) {
//                                String minValuePlus = value + "+";
//                                System.out.println(minValuePlus);
//                                urlBuilder.append("&nutrients%5B").append(nutrientCode)
//                                        .append("%5D=").append(URLEncoder.encode(minValuePlus, "UTF-8"));
//                            }
//                            else {
                                urlBuilder.append("&nutrients%5B").append(nutrientCode)
                                        .append("%5D=").append(URLEncoder.encode(value, "UTF-8"));
                            //}
                        }
                    }
                }
            }

            return urlBuilder.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error building URL", e);
        }
    }

    // Convert filter names to EDAMAM nutrient codes
    private String getNutrientCode(String filterName) {
        switch (filterName) {
            case "protein": return "PROCNT";
            case "fat": return "FAT";
            case "sugar": return "SUGAR";
            case "carbohydrates": return "CHOCDF";
            default: return null;
        }
    }

    private String sendApiRequest(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Edamam-Account-User", "Huzaifah");

        int responseCode = conn.getResponseCode();
        System.out.println("HTTP Response Code: " + responseCode); // Debug output

        if (responseCode != 200) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();
            System.err.println("Error response: " + errorResponse.toString());
            throw new RuntimeException("HTTP error code: " + responseCode + " - " + errorResponse.toString());
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    private List<Recipe> parseRecipes(String jsonResponse) {
        List<Recipe> recipes = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonResponse);

            if (!json.has("hits")) {
                System.err.println("No 'hits' found in response");
                return recipes;
            }

            JSONArray hits = json.getJSONArray("hits");

            System.out.println("Processing " + hits.length() + " hits"); // Debug output

            for (int i = 0; i < hits.length(); i++) {
                JSONObject recipeJson = hits.getJSONObject(i).getJSONObject("recipe");
                Recipe recipe = new RecipeBuilder()
                        .fromEdamamJson(recipeJson)
                        .build();
                recipes.add(recipe);
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }

        return recipes;
    }

}
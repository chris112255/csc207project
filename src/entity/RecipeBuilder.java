package entity;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder class for creating Recipe objects.
 */
public class RecipeBuilder {
    private String name;
    private String mainIngredient = "N/A";
    private List<String> ingredients = new ArrayList<>();
    private String instructions = "See full recipe online.";
    private int ingredientCount = 0;
    private List<String> dietType = new ArrayList<>();
    private Nutrients nutrients;
    private double prepTime = 0.0;
    private String cuisineType = "N/A";
    private String mealType = "N/A";
    private String dishType = "N/A";
    private String sourceUrl = "";
    private String imageUrl = "";
    private String uri = "";

    /**
     * Creates a RecipeBuilder from Edamam API JSON object response.
     * Extracts all recipe data from the JSON object.
     *
     * @param recipeJson JSONObject from Edamam API
     * @return RecipeBuilder with all fields populated from JSON
     */
    public RecipeBuilder fromEdamamJson(JSONObject recipeJson) {
        // Extract name
        this.name = recipeJson.optString("label");

        // Extract ingredients
        this.ingredients = new ArrayList<>();
        JSONArray ingredientLines = recipeJson.optJSONArray("ingredientLines");
        if (ingredientLines != null) {
            for (int i = 0; i < ingredientLines.length(); i++) {
                this.ingredients.add(ingredientLines.getString(i));
            }
            if (!this.ingredients.isEmpty()) {
                this.mainIngredient = this.ingredients.get(0);
            }
        }
        this.ingredientCount = this.ingredients.size();

        // Extract diet labels
        this.dietType = new ArrayList<>();
        JSONArray diets = recipeJson.optJSONArray("dietLabels");
        if (diets != null) {
            for (int i = 0; i < diets.length(); i++) {
                this.dietType.add(diets.getString(i));
            }
        }

        // Extract nutrients
        JSONObject totalNutrients = recipeJson.optJSONObject("totalNutrients");
        if (totalNutrients != null) {
            this.nutrients = new Nutrients(
                    (int) recipeJson.optDouble("calories", 0),
                    totalNutrients.optJSONObject("PROCNT") != null ?
                            totalNutrients.optJSONObject("PROCNT").optDouble("quantity", 0) : 0,
                    totalNutrients.optJSONObject("FAT") != null ?
                            totalNutrients.optJSONObject("FAT").optDouble("quantity", 0) : 0,
                    totalNutrients.optJSONObject("SUGAR") != null ?
                            totalNutrients.optJSONObject("SUGAR").optDouble("quantity", 0) : 0,
                    totalNutrients.optJSONObject("NA") != null ?
                            totalNutrients.optJSONObject("NA").optDouble("quantity", 0) : 0,
                    totalNutrients.optJSONObject("CHOCDF") != null ?
                            totalNutrients.optJSONObject("CHOCDF").optDouble("quantity", 0) : 0
            );
        } else {
            this.nutrients = new Nutrients(0, 0, 0, 0, 0, 0);
        }

        // Extract other fields
        this.prepTime = recipeJson.optDouble("totalTime", 0);

        this.cuisineType = recipeJson.optJSONArray("cuisineType") != null ?
                recipeJson.getJSONArray("cuisineType").optString(0, "N/A") : "N/A";

        this.mealType = recipeJson.optJSONArray("mealType") != null ?
                recipeJson.getJSONArray("mealType").optString(0, "N/A") : "N/A";

        this.dishType = recipeJson.optJSONArray("dishType") != null ?
                recipeJson.getJSONArray("dishType").optString(0, "N/A") : "N/A";

        this.sourceUrl = recipeJson.optString("url", "");
        this.imageUrl = recipeJson.optString("image", "");
        this.uri = recipeJson.optString("uri", "");

        return this;
    }

    /**
     * Builds and returns the Recipe object.
     *
     * @return Recipe object with all configured properties
     */
    public Recipe build() {
        return new Recipe(
                name,
                mainIngredient,
                ingredients,
                instructions,
                ingredientCount,
                dietType,
                nutrients,
                prepTime,
                cuisineType,
                mealType,
                dishType,
                sourceUrl,
                imageUrl,
                uri
        );
    }
}
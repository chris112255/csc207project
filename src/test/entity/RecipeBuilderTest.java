package test.entity;

import entity.Nutrients;
import entity.Recipe;
import entity.RecipeBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RecipeBuilderTest {

    @Test
    void testFromEdamamJson() {
        JSONObject json = new JSONObject();
        json.put("label", "Test Recipe");

        JSONArray ingredients = new JSONArray();
        ingredients.put("1 cup flour");
        ingredients.put("2 eggs");
        json.put("ingredientLines", ingredients);

        JSONArray diets = new JSONArray();
        diets.put("low-carb");
        diets.put("high-protein");
        json.put("dietLabels", diets);

        json.put("calories", 500);
        json.put("totalTime", 45);

        JSONObject nutrients = new JSONObject();
        JSONObject protein = new JSONObject();
        protein.put("quantity", 30);
        nutrients.put("PROCNT", protein);
        // Add other nutrients similarly...
        json.put("totalNutrients", nutrients);

        JSONArray cuisineType = new JSONArray();
        cuisineType.put("American");
        json.put("cuisineType", cuisineType);

        json.put("url", "http://example.com");
        json.put("image", "http://example.com/image.jpg");
        json.put("uri", "recipe-uri");

        Recipe recipe = new RecipeBuilder().fromEdamamJson(json).build();

        assertEquals("Test Recipe", recipe.getName());
        assertEquals("1 cup flour", recipe.getMainIngredient());
        assertEquals(2, recipe.getIngredients().size());
        assertEquals(2, recipe.getDietType().size());
        assertEquals(500, recipe.getNutriCalories());
        assertEquals(45.0, recipe.getPrepTime(), 0.001);
        assertEquals("American", recipe.getCuisineType());
        assertEquals("http://example.com", recipe.getSourceUrl());
        assertEquals("http://example.com/image.jpg", recipe.getImageUrl());
        assertEquals("recipe-uri", recipe.getUri());
    }

    @Test
    void testFromEdamamJsonWithMissingFields() {
        JSONObject json = new JSONObject();
        json.put("label", "Test Recipe");

        // Missing many fields intentionally
        Recipe recipe = new RecipeBuilder().fromEdamamJson(json).build();

        assertEquals("Test Recipe", recipe.getName());
        assertEquals("N/A", recipe.getMainIngredient());
        assertEquals(0, recipe.getIngredients().size());
        assertEquals(0, recipe.getDietType().size());
        assertEquals(0, recipe.getNutriCalories());
        assertEquals(0.0, recipe.getPrepTime(), 0.001);
        assertEquals("N/A", recipe.getCuisineType());
        assertEquals("", recipe.getSourceUrl());
        assertEquals("", recipe.getImageUrl());
        assertEquals("", recipe.getUri());
    }

    @Test
    void testBuildWithManualConfiguration() {
        Nutrients nutrients = new Nutrients(500, 30, 15, 10, 200, 45);
        Recipe recipe = new RecipeBuilder()
                .fromEdamamJson(new JSONObject()) // Start with empty JSON
                .build();

        // Verify default values
        assertNotNull(recipe);
        assertEquals("N/A", recipe.getMainIngredient());
        assertEquals("See full recipe online.", recipe.getInstructions());
    }
}
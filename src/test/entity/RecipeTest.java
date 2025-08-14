package test.entity;

import entity.Nutrients;
import entity.Recipe;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @Test
    void testConstructorAndGetters() {
        Nutrients nutrients = new Nutrients(500, 30, 15, 10, 200, 45);
        List<String> ingredients = Arrays.asList("Flour", "Sugar", "Eggs");
        List<String> dietTypes = Arrays.asList("Vegetarian", "Low-Carb");

        Recipe recipe = new Recipe(
                "Test Cake", "Flour", ingredients, "Mix and bake",
                3, dietTypes, nutrients, 45.0, "American",
                "Dessert", "Cake", "http://example.com",
                "http://example.com/image.jpg", "recipe-uri"
        );

        assertEquals("Test Cake", recipe.getName());
        assertEquals("Flour", recipe.getMainIngredient());
        assertEquals(ingredients, recipe.getIngredients());
        assertEquals("Mix and bake", recipe.getInstructions());
        assertEquals(3, recipe.getIngredientCount());
        assertEquals(dietTypes, recipe.getDietType());
        assertEquals(nutrients, recipe.getNutrients());
        assertEquals(45.0, recipe.getPrepTime(), 0.001);
        assertEquals("American", recipe.getCuisineType());
        assertEquals("Dessert", recipe.getMealType());
        assertEquals("Cake", recipe.getDishType());
        assertEquals("http://example.com", recipe.getSourceUrl());
        assertEquals("http://example.com/image.jpg", recipe.getImageUrl());
        assertEquals("recipe-uri", recipe.getUri());
    }

    @Test
    void testCalcWarnings() {
        // Test high sodium
        Nutrients highSodium = new Nutrients(500, 30, 15, 10, 900, 45);
        Recipe recipe1 = createTestRecipe(highSodium);
        assertTrue(recipe1.getWarnings().contains("sodium"));

        // Test high calories
        Nutrients highCalories = new Nutrients(900, 30, 15, 10, 200, 45);
        Recipe recipe2 = createTestRecipe(highCalories);
        assertTrue(recipe2.getWarnings().contains("calories"));

        // Test multiple warnings
        Nutrients multipleWarnings = new Nutrients(900, 30, 30, 15, 900, 120);
        Recipe recipe3 = createTestRecipe(multipleWarnings);
        String warnings = recipe3.getWarnings();
        assertTrue(warnings.contains("sodium"));
        assertTrue(warnings.contains("calories"));
        assertTrue(warnings.contains("fat"));
        assertTrue(warnings.contains("sugar"));
        assertTrue(warnings.contains("carbs"));

        // Test no warnings
        Nutrients noWarnings = new Nutrients(500, 30, 15, 5, 200, 45);
        Recipe recipe4 = createTestRecipe(noWarnings);
        assertEquals("None", recipe4.getWarnings());
    }

    @Test
    void testNullNutrientsHandling() {
        Recipe recipe = new Recipe(
                "Test", "Ingredient", List.of("A"), "Instructions",
                1, List.of(), null, 30.0, "Cuisine",
                "Meal", "Dish", "http://test.com", "http://image.com", "uri"
        );

        assertEquals(0, recipe.getNutriCalories());
        assertEquals(0, recipe.getNutriProtein(), 0.001);
        assertEquals(0, recipe.getNutriFat(), 0.001);
        assertEquals(0, recipe.getNutriSugar(), 0.001);
        assertEquals(0, recipe.getNutriSodium(), 0.001);
        assertEquals(0, recipe.getNutriCarbs(), 0.001);
    }

    @Test
    void testNullImageUrlHandling() {
        Recipe recipe = new Recipe(
                "Test", "Ingredient", List.of("A"), "Instructions",
                1, List.of(), new Nutrients(0,0,0,0,0,0), 30.0, "Cuisine",
                "Meal", "Dish", "http://test.com", null, "uri"
        );

        assertEquals("", recipe.getImageUrl());
    }

    private Recipe createTestRecipe(Nutrients nutrients) {
        return new Recipe(
                "Test", "Ingredient", List.of("A"), "Instructions",
                1, List.of(), nutrients, 30.0, "Cuisine",
                "Meal", "Dish", "http://test.com", "http://image.com", "uri"
        );
    }
}
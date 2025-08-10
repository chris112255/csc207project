package test;

import entity.Recipe;
import entity.Nutrients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.MealPlannerUsecase;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MealPlannerUsecaseTest {

    private MealPlannerUsecase mealPlanner;
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    @BeforeEach
    void setUp() {
        mealPlanner = new MealPlannerUsecase();

        // Create test recipes with different nutritional values
        Nutrients nutrients1 = new Nutrients(500, 30, 10, 5, 200, 40);
        Nutrients nutrients2 = new Nutrients(300, 20, 5, 2, 100, 30);
        Nutrients nutrients3 = new Nutrients(700, 40, 15, 10, 300, 50);

        recipe1 = new Recipe(
                "Pasta Carbonara",
                "Pasta",
                Arrays.asList("Pasta", "Eggs", "Bacon"),
                "Cook pasta, mix with eggs and bacon",
                3,
                Arrays.asList("High-Protein"),
                nutrients1,
                30.0,
                "Italian",
                "Dinner",
                "Main Course",
                "http://example.com/pasta",
                "http://example.com/pasta.jpg",
                "uri1"
        );

        recipe2 = new Recipe(
                "Vegetable Stir Fry",
                "Vegetables",
                Arrays.asList("Broccoli", "Carrots"),
                "Stir fry vegetables",
                2,
                Arrays.asList("Vegetarian"),
                nutrients2,
                20.0,
                "Asian",
                "Lunch",
                "Main Course",
                "http://example.com/stirfry",
                "http://example.com/stirfry.jpg",
                "uri2"
        );

        recipe3 = new Recipe(
                "Chicken Curry",
                "Chicken",
                Arrays.asList("Chicken", "Curry Paste"),
                "Cook chicken with curry paste",
                2,
                Arrays.asList("High-Protein"),
                nutrients3,
                40.0,
                "Indian",
                "Dinner",
                "Main Course",
                "http://example.com/curry",
                "http://example.com/curry.jpg",
                "uri3"
        );
    }

    @Test
    void testAddToPlanner() {
        assertFalse(mealPlanner.isSelected(recipe1));
        assertEquals(0, mealPlanner.getMeals().size());

        mealPlanner.addToPlanner(recipe1);
        assertTrue(mealPlanner.isSelected(recipe1));
        assertEquals(1, mealPlanner.getMeals().size());
        assertEquals(recipe1, mealPlanner.getMeals().get(0));
    }

    @Test
    void testRemoveFromPlanner() {
        mealPlanner.addToPlanner(recipe1);
        mealPlanner.addToPlanner(recipe2);
        assertEquals(2, mealPlanner.getMeals().size());

        mealPlanner.removeFromPlanner(recipe1);
        assertFalse(mealPlanner.isSelected(recipe1));
        assertTrue(mealPlanner.isSelected(recipe2));
        assertEquals(1, mealPlanner.getMeals().size());
    }

    @Test
    void testGetTotalCalories() {
        mealPlanner.addToPlanner(recipe1); // 500 calories
        mealPlanner.addToPlanner(recipe2); // 300 calories
        assertEquals(800, mealPlanner.getTotalCalories());

        mealPlanner.addToPlanner(recipe3); // 700 calories
        assertEquals(1500, mealPlanner.getTotalCalories());
    }

    @Test
    void testGetTotalProtein() {
        mealPlanner.addToPlanner(recipe1); // 30g protein
        mealPlanner.addToPlanner(recipe2); // 20g protein
        assertEquals(50, mealPlanner.getTotalProtein());

        mealPlanner.addToPlanner(recipe3); // 40g protein
        assertEquals(90, mealPlanner.getTotalProtein());
    }

    @Test
    void testGetTotalFat() {
        mealPlanner.addToPlanner(recipe1); // 10g fat
        mealPlanner.addToPlanner(recipe2); // 5g fat
        assertEquals(15, mealPlanner.getTotalFat());

        mealPlanner.addToPlanner(recipe3); // 15g fat
        assertEquals(30, mealPlanner.getTotalFat());
    }

    @Test
    void testGetTotalCarbs() {
        mealPlanner.addToPlanner(recipe1); // 40g carbs
        mealPlanner.addToPlanner(recipe2); // 30g carbs
        assertEquals(70, mealPlanner.getTotalCarbs());

        mealPlanner.addToPlanner(recipe3); // 50g carbs
        assertEquals(120, mealPlanner.getTotalCarbs());
    }

    @Test
    void testCalculateCalories() {
        mealPlanner.addToPlanner(recipe1); // 500
        mealPlanner.addToPlanner(recipe2); // 300

        // Below range
        String result1 = mealPlanner.calculateCalories(900, 1200);
        assertTrue(result1.contains("under by 100"));

        // Within range
        mealPlanner.removeFromPlanner(recipe2);
        String result2 = mealPlanner.calculateCalories(400, 600);
        assertTrue(result2.contains("falls in range"));

        // Above range
        mealPlanner.addToPlanner(recipe3); // 700
        String result3 = mealPlanner.calculateCalories(400, 1000);
        assertTrue(result3.contains("over by 200"));
    }

    @Test
    void testCalculateProtein() {
        mealPlanner.addToPlanner(recipe1); // 30g
        mealPlanner.addToPlanner(recipe2); // 20g

        // Below minimum
        String result1 = mealPlanner.calculateProtein(60);
        assertTrue(result1.contains("under by 10"));

        // Above minimum
        String result2 = mealPlanner.calculateProtein(40);
        assertTrue(result2.contains("falls in range"));
    }

    @Test
    void testCalculateFat() {
        mealPlanner.addToPlanner(recipe1); // 10g
        mealPlanner.addToPlanner(recipe3); // 15g

        // Below max
        String result1 = mealPlanner.calculateFat(30);
        assertTrue(result1.contains("falls in range"));

        // Above max
        String result2 = mealPlanner.calculateFat(20);
        assertTrue(result2.contains("over by 5"));
    }

    @Test
    void testCalculateCarbs() {
        mealPlanner.addToPlanner(recipe2); // 30g
        mealPlanner.addToPlanner(recipe3); // 50g

        // Below max
        String result1 = mealPlanner.calculateCarbs(90);
        assertTrue(result1.contains("falls in range"));

        // Above max
        String result2 = mealPlanner.calculateCarbs(70);
        assertTrue(result2.contains("over by 10"));
    }

    @Test
    void testDuplicateRecipes() {
        mealPlanner.addToPlanner(recipe1);
        mealPlanner.addToPlanner(recipe1); // Add same recipe again

        assertEquals(1, mealPlanner.getMeals().size()); // Should not allow duplicates
    }

    @Test
    void testEmptyPlanner() {
        assertEquals(0, mealPlanner.getTotalCalories());
        assertEquals(0, mealPlanner.getTotalProtein());
        assertEquals(0, mealPlanner.getTotalFat());
        assertEquals(0, mealPlanner.getTotalCarbs());

        String caloriesResult = mealPlanner.calculateCalories(100, 200);
        assertTrue(caloriesResult.contains("under by 100"));
    }
}
package test;

import entity.Recipe;
import entity.Nutrients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.MealPlannerUsecase;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MealPlannerUsecaseTest {

    private MealPlannerUsecase mealPlanner;
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    @BeforeEach
    void setUp() {
        mealPlanner = new MealPlannerUsecase();

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
        mealPlanner.addToPlanner(recipe1);
        mealPlanner.addToPlanner(recipe2);
        assertEquals(800, mealPlanner.getTotalCalories());

        mealPlanner.addToPlanner(recipe3);
        assertEquals(1500, mealPlanner.getTotalCalories());
    }

    @Test
    void testGetTotalProtein() {
        mealPlanner.addToPlanner(recipe1);
        mealPlanner.addToPlanner(recipe2);
        assertEquals(50, mealPlanner.getTotalProtein());

        mealPlanner.addToPlanner(recipe3);
        assertEquals(90, mealPlanner.getTotalProtein());
    }

    @Test
    void testGetTotalFat() {
        mealPlanner.addToPlanner(recipe1);
        mealPlanner.addToPlanner(recipe2);
        assertEquals(15, mealPlanner.getTotalFat());

        mealPlanner.addToPlanner(recipe3);
        assertEquals(30, mealPlanner.getTotalFat());
    }

    @Test
    void testGetTotalCarbs() {
        mealPlanner.addToPlanner(recipe1);
        mealPlanner.addToPlanner(recipe2);
        assertEquals(70, mealPlanner.getTotalCarbs());

        mealPlanner.addToPlanner(recipe3);
        assertEquals(120, mealPlanner.getTotalCarbs());
    }

    @Test
    void testCalculateCalories() {
        mealPlanner.addToPlanner(recipe1);
        mealPlanner.addToPlanner(recipe2);

        String result1 = mealPlanner.calculateCalories(900, 1200);
        assertTrue(result1.contains("under by 100"));

        mealPlanner.removeFromPlanner(recipe2);
        String result2 = mealPlanner.calculateCalories(400, 600);
        assertTrue(result2.contains("falls in range"));

        mealPlanner.addToPlanner(recipe3);
        String result3 = mealPlanner.calculateCalories(400, 1000);
        assertTrue(result3.contains("over by 200"));
    }

    @Test
    void testCalculateProtein() {
        mealPlanner.addToPlanner(recipe1);
        mealPlanner.addToPlanner(recipe2);

        String result1 = mealPlanner.calculateProtein(60);
        assertTrue(result1.contains("under by 10"));

        String result2 = mealPlanner.calculateProtein(40);
        assertTrue(result2.contains("falls in range"));
    }

    @Test
    void testCalculateFat() {
        mealPlanner.addToPlanner(recipe1); // 10g
        mealPlanner.addToPlanner(recipe3); // 15g

        String result1 = mealPlanner.calculateFat(30);
        assertTrue(result1.contains("falls in range"));

        String result2 = mealPlanner.calculateFat(20);
        assertTrue(result2.contains("over by 5"));
    }

    @Test
    void testCalculateCarbs() {
        mealPlanner.addToPlanner(recipe2);
        mealPlanner.addToPlanner(recipe3);

        String result1 = mealPlanner.calculateCarbs(90);
        assertTrue(result1.contains("falls in range"));

        String result2 = mealPlanner.calculateCarbs(70);
        assertTrue(result2.contains("over by 10"));
    }

    @Test
    void testDuplicateRecipes() {
        mealPlanner.addToPlanner(recipe1);
        mealPlanner.addToPlanner(recipe1);

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
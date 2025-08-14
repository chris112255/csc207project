package test;

import entity.Recipe;
import entity.Nutrients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.favourites.FavouritesUsecase;
import usecase.sort.RecipeSorterUseCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RecipeSorterTest {

    private List<Recipe> recipes;
    private Recipe recipe1;
    private Recipe recipe2;
    private Recipe recipe3;

    @BeforeEach
    void setUp() {
        Nutrients nutrients1 = new Nutrients(500, 30, 5, 15, 600, 50);
        Nutrients nutrients2 = new Nutrients(800, 20, 15, 25, 400, 70);
        Nutrients nutrients3 = new Nutrients(300, 40, 2, 10, 200, 30);

        // Create test recipes
        recipe1 = new Recipe(
                "Pasta Carbonara",
                "Pasta",
                Arrays.asList("Pasta", "Eggs", "Bacon", "Cheese"),
                "Cook pasta, mix with eggs and bacon",
                4,
                Arrays.asList("High-Protein", "Non-Vegetarian"),
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
                Arrays.asList("Broccoli", "Carrots", "Bell Peppers", "Soy Sauce"),
                "Stir fry vegetables with sauce",
                4,
                Arrays.asList("Vegetarian", "Vegan"),
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
                "Fruit Salad",
                "Fruits",
                Arrays.asList("Apple", "Banana", "Orange"),
                "Mix chopped fruits together",
                3,
                Arrays.asList("Vegetarian", "Vegan", "Low-Calorie"),
                nutrients3,
                10.0,
                "International",
                "Breakfast",
                "Dessert",
                "http://example.com/fruitsalad",
                "http://example.com/fruitsalad.jpg",
                "uri3"
        );

        recipes = new ArrayList<>(Arrays.asList(recipe1, recipe2, recipe3));
    }

    private static class TestFavouritesUsecase extends FavouritesUsecase {
        private final List<Recipe> testFavourites;

        public TestFavouritesUsecase(List<Recipe> testFavourites) {
            this.testFavourites = new ArrayList<>(testFavourites);
        }

        @Override
        public List<Recipe> getFavourites() {
            return new ArrayList<>(testFavourites);
        }
    }

    @Test
    void sortByIngredientsCount() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("ingredients");
        sorter.sortRecipes(recipes);

        assertEquals(recipe3, recipes.get(0));
        assertEquals(recipe1, recipes.get(1));
        assertEquals(recipe2, recipes.get(2));
    }

    @Test
    void sortByPrepTime() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("time");
        sorter.sortRecipes(recipes);

        assertEquals(recipe3, recipes.get(0));
        assertEquals(recipe2, recipes.get(1));
        assertEquals(recipe1, recipes.get(2));
    }

    @Test
    void sortByCaloriesAscending() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("caloriesAscending");
        sorter.sortRecipes(recipes);

        assertEquals(recipe3, recipes.get(0));
        assertEquals(recipe1, recipes.get(1));
        assertEquals(recipe2, recipes.get(2));
    }

    @Test
    void sortByCaloriesDescending() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("caloriesDescending");
        sorter.sortRecipes(recipes);

        assertEquals(recipe2, recipes.get(0));
        assertEquals(recipe1, recipes.get(1));
        assertEquals(recipe3, recipes.get(2));
    }

    @Test
    void sortByProteinAscending() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("proteinAscending");
        sorter.sortRecipes(recipes);

        assertEquals(recipe2, recipes.get(0));
        assertEquals(recipe1, recipes.get(1));
        assertEquals(recipe3, recipes.get(2));
    }

    @Test
    void sortByProteinDescending() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("proteinDescending");
        sorter.sortRecipes(recipes);

        assertEquals(recipe3, recipes.get(0));
        assertEquals(recipe1, recipes.get(1));
        assertEquals(recipe2, recipes.get(2));
    }

    @Test
    void sortByFatAscending() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("fatAscending");
        sorter.sortRecipes(recipes);

        assertEquals(recipe3, recipes.get(0));
        assertEquals(recipe1, recipes.get(1));
        assertEquals(recipe2, recipes.get(2));
    }

    @Test
    void sortByFatDescending() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("fatDescending");
        sorter.sortRecipes(recipes);

        assertEquals(recipe2, recipes.get(0));
        assertEquals(recipe1, recipes.get(1));
        assertEquals(recipe3, recipes.get(2));
    }

    @Test
    void sortBySugarAscending() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("sugarAscending");
        sorter.sortRecipes(recipes);

        assertEquals(recipe3, recipes.get(0));
        assertEquals(recipe1, recipes.get(1));
        assertEquals(recipe2, recipes.get(2));
    }

    @Test
    void sortBySugarDescending() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("sugarDescending");
        sorter.sortRecipes(recipes);

        assertEquals(recipe2, recipes.get(0));
        assertEquals(recipe1, recipes.get(1));
        assertEquals(recipe3, recipes.get(2));
    }

    @Test
    void sortBySodiumAscending() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("sodiumAscending");
        sorter.sortRecipes(recipes);

        assertEquals(recipe3, recipes.get(0));
        assertEquals(recipe2, recipes.get(1));
        assertEquals(recipe1, recipes.get(2));
    }

    @Test
    void sortBySodiumDescending() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("sodiumDescending");
        sorter.sortRecipes(recipes);

        assertEquals(recipe1, recipes.get(0));
        assertEquals(recipe2, recipes.get(1));
        assertEquals(recipe3, recipes.get(2));
    }

    /*@Test
    void sortByType() {
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("typeMain Course");
        sorter.sortRecipes(recipes);

        assertTrue(recipes.indexOf(recipe3) > recipes.indexOf(recipe1));
        assertTrue(recipes.indexOf(recipe3) > recipes.indexOf(recipe2));

        RecipeSorterUseCase sorter2 = new RecipeSorterUseCase("typeInternational");
        sorter2.sortRecipes(recipes);

        assertTrue(recipes.indexOf(recipe3) < recipes.indexOf(recipe1));
        assertTrue(recipes.indexOf(recipe3) < recipes.indexOf(recipe2));
    }*/

    @Test
    void sortByFavorites() {
        List<Recipe> testFavorites = Arrays.asList(
                new Recipe(
                        "Favorite Pasta",
                        "Pasta",
                        Arrays.asList("Pasta", "Cheese"),
                        "Favorite pasta recipe",
                        2,
                        Arrays.asList("High-Protein"),
                        new Nutrients(600, 25, 10, 5, 500, 60),
                        25.0,
                        "Italian",
                        "Dinner",
                        "Main Course",
                        "http://example.com/favpasta",
                        "http://example.com/favpasta.jpg",
                        "uri4"
                ),
                recipe3
        );

        FavouritesUsecase testFavouritesUsecase = new TestFavouritesUsecase(testFavorites);

        RecipeSorterUseCase sorter = new RecipeSorterUseCase("favs");
        sorter.strategies.get("favs").setTestCriteria(testFavouritesUsecase);
        sorter.sortRecipes(recipes);

        assertEquals(recipe2, recipes.get(0));
        assertEquals(recipe1, recipes.get(1));
        assertEquals(recipe3, recipes.get(2));
    }

    @Test
    void sortWithUnknownCriteriaShouldNotChangeOrder() {
        List<Recipe> originalOrder = new ArrayList<>(recipes);
        RecipeSorterUseCase sorter = new RecipeSorterUseCase("unknown");
        sorter.sortRecipes(recipes);

        assertEquals(originalOrder, recipes);
    }
}
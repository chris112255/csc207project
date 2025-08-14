package test.interactor;

import entity.Nutrients;
import entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import usecase.search.RecipeSearchGateway;
import usecase.search.SearchRecipesUseCase;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchRecipesTest {

    private SearchRecipesUseCase searchRecipesUseCase;

    @Mock
    private RecipeSearchGateway mockGateway;

    @BeforeEach
    void setUp() {
        searchRecipesUseCase = new SearchRecipesUseCase(mockGateway);
    }

    @Test
    void execute_ShouldReturnRecipes_WhenGatewayReturnsResults() throws Exception {
        Map<String, String> filters = Map.of("query", "chicken");

        Recipe chickenCurry = createTestRecipe(
                "Chicken Curry",
                "chicken-curry-uri",
                "http://example.com/chicken-curry"
        );

        Recipe grilledChicken = createTestRecipe(
                "Grilled Chicken",
                "grilled-chicken-uri",
                "http://example.com/grilled-chicken"
        );

        List<Recipe> expectedRecipes = List.of(chickenCurry, grilledChicken);

        when(mockGateway.searchRecipes(anyMap())).thenReturn(expectedRecipes);

        List<Recipe> actualRecipes = searchRecipesUseCase.execute(filters);

        assertEquals(2, actualRecipes.size());
        assertEquals("Chicken Curry", actualRecipes.get(0).getName());
        assertEquals("Grilled Chicken", actualRecipes.get(1).getName());
        verify(mockGateway).searchRecipes(filters);
    }

    private Recipe createTestRecipe(String name, String uri, String url) {
        return new Recipe(
                name,
                "Main Ingredient",
                List.of("Ingredient 1", "Ingredient 2"),
                "Test instructions",
                2,
                List.of("Test Diet"),
                new Nutrients(500, 30, 10, 5, 200, 40), // calories, protein, fat, sugar, sodium, carbs
                30.0, // prepTime
                "Test Cuisine",
                "Test Meal Type",
                "Test Dish Type",
                url,
                "http://example.com/image.jpg",
                uri
        );
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenGatewayThrowsException() throws Exception {
        Map<String, String> filters = Map.of("query", "chicken");
        when(mockGateway.searchRecipes(anyMap())).thenThrow(new Exception("API Error"));

        List<Recipe> actualRecipes = searchRecipesUseCase.execute(filters);

        assertTrue(actualRecipes.isEmpty());
        verify(mockGateway).searchRecipes(filters);
    }

    @Test
    void execute_ShouldPassFiltersCorrectly_ToGateway() throws Exception {
        Map<String, String> filters = new HashMap<>();
        filters.put("query", "pasta");
        filters.put("diet", "vegetarian");
        filters.put("maxCalories", "500");

        when(mockGateway.searchRecipes(anyMap())).thenReturn(List.of());

        searchRecipesUseCase.execute(filters);

        verify(mockGateway).searchRecipes(argThat(
                passedFilters ->
                        passedFilters.get("query").equals("pasta") &&
                                passedFilters.get("diet").equals("vegetarian") &&
                                passedFilters.get("maxCalories").equals("500")
        ));
    }

    @Test
    void execute_ShouldHandleEmptyFilters_Properly() throws Exception {
        Map<String, String> emptyFilters = Collections.emptyMap();
        when(mockGateway.searchRecipes(anyMap())).thenReturn(List.of());

        List<Recipe> result = searchRecipesUseCase.execute(emptyFilters);

        assertTrue(result.isEmpty());
        verify(mockGateway).searchRecipes(emptyFilters);
    }

    @Test
    void execute_ShouldLogError_WhenExceptionOccurs() throws Exception {
        Map<String, String> filters = Map.of("query", "beef");
        Exception expectedException = new Exception("Test exception");
        when(mockGateway.searchRecipes(anyMap())).thenThrow(expectedException);

        searchRecipesUseCase.execute(filters);

        verify(mockGateway).searchRecipes(filters);
    }
}
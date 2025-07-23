// ExplorePageView.java
package main.view;

import usecase.SearchRecipesUseCase;
import entity.Recipe;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExplorePageView extends RecipeView {
    private final SearchRecipesUseCase searchRecipesUseCase = new SearchRecipesUseCase();

    public ExplorePageView() {
        super("Explore Page");

        // Add action listener to search button
        searchButton.addActionListener(e -> {
            Map<String, String> filters = getSearchFilters();
            List<Recipe> recipes = searchRecipesUseCase.execute(filters);
            updateResults(recipes);
        });
    }

    private Map<String, String> getSearchFilters() {
        Map<String, String> filters = new HashMap<>();

        // Combine name and primary ingredient into a single search query
        StringBuilder queryBuilder = new StringBuilder();
        String recipeName = name.getText().trim();
        String ingredient = primaryIngredient.getText().trim();

        if (!recipeName.isEmpty()) {
            queryBuilder.append(recipeName);
        }

        if (!ingredient.isEmpty()) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append(" ");
            }
            queryBuilder.append(ingredient);
        }

        if (queryBuilder.length() > 0) {
            filters.put("q", queryBuilder.toString());
        }

        // Diet type
        String diet = dietType.getText().trim();
        if (!diet.isEmpty()) {
            filters.put("diet", diet);
        }

        // Handle calorie range
        String minCal = minCalories.getText().trim();
        String maxCal = maxCalories.getText().trim();
        if (!minCal.isEmpty() || !maxCal.isEmpty()) {
            String calorieRange = "";
            if (!minCal.isEmpty() && !maxCal.isEmpty()) {
                calorieRange = minCal + "-" + maxCal;
            } else if (!minCal.isEmpty()) {
                calorieRange = minCal + "+";
            } else if (!maxCal.isEmpty()) {
                calorieRange = "0-" + maxCal;
            }
            if (!calorieRange.isEmpty()) {
                filters.put("calories", calorieRange);
            }
        }

        // Add nutritional parameters
        addNutrientFilter(filters, "protein", protein.getText().trim());
        addNutrientFilter(filters, "fat", maxFat.getText().trim());
        addNutrientFilter(filters, "sugar", maxSugar.getText().trim());
        addNutrientFilter(filters, "carbohydrates", maxCarbs.getText().trim());

        return filters;
    }

    // Helper method to add nutrient filters if they have values
    private void addNutrientFilter(Map<String, String> filters, String nutrient, String value) {
        if (!value.isEmpty()) {
            filters.put(nutrient, value);
        }
    }

    // Update results panel with actual recipes
    public void updateResults(List<Recipe> recipes) {
        resultsContainer.removeAll();

        if (recipes.isEmpty()) {
            JLabel noResultsLabel = new JLabel("No recipes found. Try adjusting your search criteria.");
            resultsContainer.add(noResultsLabel);
        } else {
            for (Recipe recipe : recipes) {
                JButton recipeButton = new JButton(recipe.getTitle());
                // Add action listener to show recipe details later
                recipeButton.addActionListener(e -> {
                    // This will open the recipe URL in browser
                    try {
                        java.awt.Desktop.getDesktop().browse(java.net.URI.create(recipe.getSourceUrl()));
                    } catch (Exception ex) {
                        System.err.println("Error opening recipe URL: " + ex.getMessage());
                    }
                });
                resultsContainer.add(recipeButton);
            }
        }

        resultsContainer.revalidate();
        resultsContainer.repaint();
    }
}
package main.view;

import usecase.search.SearchRecipesUseCase;
import entity.Recipe;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExplorePageView extends RecipeView {

    private final SearchRecipesUseCase searchRecipesUseCase;

    // Fixed: now takes SearchRecipesUseCase as a constructor argument
    public ExplorePageView(SearchRecipesUseCase searchRecipesUseCase) {
        super("Explore Page");
        this.searchRecipesUseCase = searchRecipesUseCase;

        searchButton.addActionListener(e -> {
            Map<String, String> filters = getSearchFilters();
            List<Recipe> recipes = searchRecipesUseCase.execute(filters);
            updateResults(recipes);
        });
    }

    private Map<String, String> getSearchFilters() {
        Map<String, String> filters = new HashMap<>();

        StringBuilder queryBuilder = new StringBuilder();
        String recipeName = name.getText().trim();
        String ingredient = primaryIngredient.getText().trim();

        if (!recipeName.isEmpty()) queryBuilder.append(recipeName);
        if (!ingredient.isEmpty()) {
            if (queryBuilder.length() > 0) queryBuilder.append(" ");
            queryBuilder.append(ingredient);
        }

        if (queryBuilder.length() > 0) filters.put("q", queryBuilder.toString());

        String diet = dietType.getText().trim();
        if (!diet.isEmpty()) filters.put("diet", diet);

        String minCal = minCalories.getText().trim();
        String maxCal = maxCalories.getText().trim();
        if (!minCal.isEmpty() || !maxCal.isEmpty()) {
            String calorieRange = (!minCal.isEmpty() && !maxCal.isEmpty()) ? minCal + "-" + maxCal
                    : (!minCal.isEmpty()) ? minCal + "+"
                    : "0-" + maxCal;
            filters.put("calories", calorieRange);
        }

        addNutrientFilter(filters, "protein", protein.getText().trim());
        addNutrientFilter(filters, "fat", maxFat.getText().trim());
        addNutrientFilter(filters, "sugar", maxSugar.getText().trim());
        addNutrientFilter(filters, "carbohydrates", maxCarbs.getText().trim());

        return filters;
    }

    private void addNutrientFilter(Map<String, String> filters, String nutrient, String value) {
        if (!value.isEmpty()) {
            filters.put(nutrient, value);
        }
    }

    public void updateResults(List<Recipe> recipes) {
        resultsContainer.removeAll();

        if (recipes.isEmpty()) {
            resultsContainer.add(new JLabel("No recipes found. Try adjusting your search criteria."));
        } else {
            for (Recipe recipe : recipes) {
                JButton recipeButton = new JButton(recipe.getDish());
                recipeButton.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().browse(URI.create(recipe.getSourceUrl()));
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

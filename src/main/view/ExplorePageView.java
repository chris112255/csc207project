package main.view;


import usecase.FavouritesUsecase;
import usecase.SearchRecipesUsecase;
import entity.Recipe;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExplorePageView extends RecipeView {

    private final SearchRecipesUsecase searchRecipesUseCase = new SearchRecipesUsecase();
    private final FavouritesUsecase favouritesUsecase = new FavouritesUsecase();
    public ExplorePageView() {

        super("Explore Page");

        searchButton.addActionListener(e -> {
            Map<String, String> filters = getSearchFilters();
            List<Recipe> recipes = searchRecipesUseCase.execute(filters);
            updateResults(recipes);
        });
    }

    private Map<String, String> getSearchFilters() {
        Map<String, String> filters = new HashMap<>();

        StringBuilder queryBuilder = new StringBuilder();
        String ingredient = primaryIngredient.getText().trim();

        if (!ingredient.isEmpty()) {
            if (queryBuilder.length() > 0) queryBuilder.append(" ");
            queryBuilder.append(ingredient);
        }

        if (queryBuilder.length() > 0) filters.put("q", queryBuilder.toString());

        String diet = dietType.getText().trim();
        if (!diet.isEmpty()) filters.put("diet", diet);

        // Calorie range
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

                JPanel recipePanel = new JPanel();
                recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));
                recipePanel.setBorder(BorderFactory.createEtchedBorder());

                // Recipe title button
                JButton recipeButton = new JButton("<html><center>" + recipe.getName() + "</center></html>");
                recipeButton.setPreferredSize(new java.awt.Dimension(180, 60));


                JButton favoriteButton = new JButton("Add to Favorites");
                favoriteButton.setPreferredSize(new java.awt.Dimension(180, 30));

                // Check if already in favorites and update button text
                if (favouritesUsecase.isFavourite(recipe)) {
                    favoriteButton.setText("Remove from Favorites");
                }

                favoriteButton.addActionListener(e -> {
                    if (favouritesUsecase.isFavourite(recipe)) {
                        favouritesUsecase.removeFromFavourites(recipe);
                        favoriteButton.setText("Add to Favorites");
                        JOptionPane.showMessageDialog(this.resultsContainer,
                                "Removed from favorites: " + recipe.getName(),
                                "Favorites", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        favouritesUsecase.addToFavourites(recipe);
                        favoriteButton.setText("Remove from Favorites");
                        JOptionPane.showMessageDialog(this.resultsContainer,
                                "Added to favorites: " + recipe.getName(),
                                "Favorites", JOptionPane.INFORMATION_MESSAGE);

                    }
                });

                recipePanel.add(recipeButton);
                recipePanel.add(favoriteButton);
                resultsContainer.add(recipePanel);
            }
        }

        resultsContainer.revalidate();
        resultsContainer.repaint();
    }
}

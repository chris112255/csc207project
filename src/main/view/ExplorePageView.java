package main.view;

import usecase.FavouritesUsecase;
import usecase.SearchRecipesUsecase;
import entity.Recipe;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExplorePageView extends RecipeView {

    private final SearchRecipesUsecase searchRecipesUseCase = new SearchRecipesUsecase();
    private final FavouritesUsecase favouritesUsecase = new FavouritesUsecase();

    private List<Recipe> allRecipes;
    private int currentPage = 0;
    private final int RECIPES_PER_PAGE = 8;

    public ExplorePageView() {
        super("Explore Page");

        searchButton.addActionListener(e -> {
            Map<String, String> filters = getSearchFilters();
            allRecipes = searchRecipesUseCase.execute(filters);
            currentPage = 0;
            showPage(currentPage);
        });

        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                showPage(currentPage);
            }
        });

        nextButton.addActionListener(e -> {
            if ((currentPage + 1) * RECIPES_PER_PAGE < allRecipes.size()) {
                currentPage++;
                showPage(currentPage);
            }
        });
    }

    private Map<String, String> getSearchFilters() {
        Map<String, String> filters = new HashMap<>();

        StringBuilder queryBuilder = new StringBuilder();
        String ingredient = primaryIngredient.getText().trim();

        if (!ingredient.isEmpty()) {
            queryBuilder.append(ingredient);
        }

        if (queryBuilder.length() > 0) filters.put("q", queryBuilder.toString());

        String selectedDiet = (String) dietTypeDropdown.getSelectedItem();
        if (selectedDiet != null && !selectedDiet.isEmpty()) {
            filters.put("diet", selectedDiet);
        }

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

    private void showPage(int pageIndex) {
        resultsContainer.removeAll();

        if (allRecipes == null || allRecipes.isEmpty()) {
            resultsContainer.add(new JLabel("No recipes found. Try adjusting your search criteria."));
        } else {
            int start = pageIndex * RECIPES_PER_PAGE;
            int end = Math.min(start + RECIPES_PER_PAGE, allRecipes.size());
            List<Recipe> pageRecipes = allRecipes.subList(start, end);

            for (Recipe recipe : pageRecipes) {
                JPanel recipePanel = new JPanel();
                recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));
                recipePanel.setBorder(BorderFactory.createEtchedBorder());
                recipePanel.setPreferredSize(new Dimension(180, 220));

                try {
                    URL imageUrl = new URL(recipe.getImageUrl());
                    ImageIcon icon = new ImageIcon(imageUrl);
                    Image scaledImg = icon.getImage().getScaledInstance(160, 120, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
                    recipePanel.add(imageLabel);
                } catch (Exception e) {
                    recipePanel.add(new JLabel("[Image not available]"));
                }

                JButton recipeButton = new JButton("<html><center>" + recipe.getName() + "</center></html>");
                recipeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                recipeButton.addActionListener(e -> new SingleRecipeView(recipe));

                JButton favoriteButton = new JButton(favouritesUsecase.isFavourite(recipe) ? "Remove from Favorites" : "Add to Favorites");
                favoriteButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                favoriteButton.addActionListener(e -> {
                    if (favouritesUsecase.isFavourite(recipe)) {
                        favouritesUsecase.removeFromFavourites(recipe);
                        favoriteButton.setText("Add to Favorites");
                    } else {
                        favouritesUsecase.addToFavourites(recipe);
                        favoriteButton.setText("Remove from Favorites");
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

package main.view;

import usecase.FavouritesUsecase;
import usecase.SearchRecipesUsecase;
import entity.Recipe;
import usecase.sort.RecipeSorterUseCase;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
            createResultSorter(recipes);
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

        // ✅ Updated: use dietTypeDropdown from RecipeView
        String selectedDiet = (String) dietTypeDropdown.getSelectedItem();
        if (selectedDiet != null && !selectedDiet.isEmpty()) {
            filters.put("diet", selectedDiet);
        }

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

                JButton recipeButton = new JButton("<html><center>" + recipe.getName() + "</center></html>");
                recipeButton.setPreferredSize(new Dimension(180, 60));
                recipeButton.addActionListener(e -> new SingleRecipeView(recipe));

                JButton favoriteButton = new JButton("Add to Favorites");
                favoriteButton.setPreferredSize(new Dimension(180, 30));

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

    private void createResultSorter(List<Recipe> recipes) {
        List<Recipe> results = new ArrayList<>(recipes);
        List<Recipe> unsortedResults = new ArrayList<>(results);
        JPopupMenu sortMenu = new JPopupMenu();
        sortMenu.add(new JLabel("Sort By:"));

        JMenuItem defaultItem = new JMenuItem("Default");
        defaultItem.addActionListener(e -> {
            updateResults(unsortedResults);
        });
        sortMenu.add(defaultItem);

        JMenuItem favItem = new JMenuItem("Favourites");
        favItem.addActionListener(e -> {
            RecipeSorterUseCase favSort = new RecipeSorterUseCase("favs");
            favSort.sortRecipes(results);
            updateResults(results);
        });
        sortMenu.add(favItem);

        JMenuItem ingredientItem = new JMenuItem("Least Ingredients");
        ingredientItem.addActionListener(e -> {
            RecipeSorterUseCase ingredientSort = new RecipeSorterUseCase("ingredients");
            ingredientSort.sortRecipes(results);
            updateResults(results);
        });
        sortMenu.add(ingredientItem);

        JMenuItem timeItem = new JMenuItem("Least Prep Time");
        timeItem.addActionListener(e -> {
            RecipeSorterUseCase timeSort = new RecipeSorterUseCase("time");
            timeSort.sortRecipes(results);
            updateResults(results);
        });
        sortMenu.add(timeItem);

        JMenuItem leastCalItem = new JMenuItem("Least Calories");
        leastCalItem.addActionListener(e -> {
            RecipeSorterUseCase leastCalSort = new RecipeSorterUseCase("caloriesAscending");
            leastCalSort.sortRecipes(results);
            updateResults(results);
        });
        sortMenu.add(leastCalItem);

        JMenuItem mostCalItem = new JMenuItem("Most Calories");
        mostCalItem.addActionListener(e -> {
            RecipeSorterUseCase mostCalSort = new RecipeSorterUseCase("caloriesDescending");
            mostCalSort.sortRecipes(results);
            updateResults(results);
        });
        sortMenu.add(mostCalItem);

        JMenuItem mostProtItem = new JMenuItem("Most Protein");
        mostProtItem.addActionListener(e -> {
            RecipeSorterUseCase mostProtSort = new RecipeSorterUseCase("proteinDescending");
            mostProtSort.sortRecipes(results);
            updateResults(results);
        });
        sortMenu.add(mostProtItem);

        JMenuItem leastFatItem = new JMenuItem("Least Fat");
        leastFatItem.addActionListener(e -> {
            RecipeSorterUseCase leastFatSort = new RecipeSorterUseCase("fatAscending");
            leastFatSort.sortRecipes(results);
            updateResults(results);
        });
        sortMenu.add(leastFatItem);

        JMenuItem leastSugarItem = new JMenuItem("Least Sugar");
        leastSugarItem.addActionListener(e -> {
            RecipeSorterUseCase leastSugarSort = new RecipeSorterUseCase("sugarAscending");
            leastSugarSort.sortRecipes(results);
            updateResults(results);
        });
        sortMenu.add(leastSugarItem);

        String[] types = {
                "Breakfast", "Dinner", "Lunch", "Snack", "Desserts", "Drinks",
                "Asian", "British", "Caribbean", "Central Europe", "Chinese", "Eastern Europe",
                "French", "Greek", "Indian", "Italian", "Japanese", "Korean", "Kosher",
                "Mediterranean", "Mexican", "Middle Eastern", "Nordic", "South American", "South East Asian"
        };

        for (String type : types) {
            String menuItemLabel = type + " First";
            String sorterType = "type" + type;

            JMenuItem menuItem = new JMenuItem(menuItemLabel);
            menuItem.addActionListener(e -> {
                RecipeSorterUseCase sorter = new RecipeSorterUseCase(sorterType);
                sorter.sortRecipes(results);
                updateResults(results);
            });
            sortMenu.add(menuItem);
        }

        sortButton.addActionListener(e -> {
            sortMenu.show(sortButton, 0, sortButton.getHeight() + 200);
        });
    }
}

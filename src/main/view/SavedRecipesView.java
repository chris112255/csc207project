package main.view;

import usecase.FavouritesUsecase;
import entity.Recipe;
import usecase.MealPlannerUsecase;
import usecase.sort.RecipeSorterUseCase;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class SavedRecipesView extends RecipeView {
    private final FavouritesUsecase favoritesUsecase = new FavouritesUsecase();
    private List<Recipe> originalFavorites = new ArrayList<>();

    public SavedRecipesView(MealPlannerUsecase mpUseCase) {
        super("Saved Recipes", mpUseCase);
        loadFavorites();
        searchButton.addActionListener(e -> {
            Map<String, String> filters = getFilterCriteria();
            List<Recipe> filteredFavorites = filterFavorites(originalFavorites, filters);
            displayFavorites(filteredFavorites);
        });
        createFavSorter();
    }
    /**
     * Get filter options from GUI
     */
    private Map<String, String> getFilterCriteria() {
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

    /**
     * Helper method to add nutrient filters
     */
    private void addNutrientFilter(Map<String, String> filters, String nutrient, String value) {
        if (!value.isEmpty()) {
            filters.put(nutrient, value);
        }
    }

    /**
     * Filter favorites based on criteria
     */
    private List<Recipe> filterFavorites(List<Recipe> favorites, Map<String, String> filters) {
        if (filters.isEmpty()) {
            return new ArrayList<>(favorites);
        }

        List<Recipe> filteredResults = new ArrayList<>();

        for (Recipe recipe : favorites) {
            boolean matchesFilters = true;

            if (filters.containsKey("q")) {
                String query = filters.get("q").toLowerCase();
                boolean matchesQuery = recipe.getName().toLowerCase().contains(query) ||
                        recipe.getMainIngredient().toLowerCase().contains(query) ||
                        recipe.getIngredients().stream().anyMatch(ingredient ->
                                ingredient.toLowerCase().contains(query));
                if (!matchesQuery) {
                    matchesFilters = false;
                }
            }

            if (filters.containsKey("diet") && matchesFilters) {
                String dietFilter = filters.get("diet").toLowerCase();
                boolean matchesDiet = recipe.getDietType().stream().anyMatch(diet ->
                        diet.toLowerCase().contains(dietFilter));
                if (!matchesDiet) {
                    matchesFilters = false;
                }
            }

            if (filters.containsKey("calories") && matchesFilters) {
                String calorieRange = filters.get("calories");
                int recipeCalories = recipe.getNutriCalories();

                if (calorieRange.contains("-")) {
                    String[] parts = calorieRange.split("-");
                    try {
                        int minCal = Integer.parseInt(parts[0]);
                        int maxCal = Integer.parseInt(parts[1]);
                        if (recipeCalories < minCal || recipeCalories > maxCal) {
                            matchesFilters = false;
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid calorie range
                    }
                } else if (calorieRange.endsWith("+")) {
                    try {
                        int minCal = Integer.parseInt(calorieRange.replace("+", ""));
                        if (recipeCalories < minCal) {
                            matchesFilters = false;
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid calorie range
                    }
                }
            }

            if (filters.containsKey("protein") && matchesFilters) {
                try {
                    double maxProtein = Double.parseDouble(filters.get("protein"));
                    if (recipe.getNutriProtein() > maxProtein) {
                        matchesFilters = false;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid protein value
                }
            }

            if (filters.containsKey("fat") && matchesFilters) {
                try {
                    double maxFat = Double.parseDouble(filters.get("fat"));
                    if (recipe.getNutriFat() > maxFat) {
                        matchesFilters = false;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid fat value
                }
            }

            if (filters.containsKey("sugar") && matchesFilters) {
                try {
                    double maxSugar = Double.parseDouble(filters.get("sugar"));
                    if (recipe.getNutriSugar() > maxSugar) {
                        matchesFilters = false;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid sugar value
                }
            }

            if (filters.containsKey("carbohydrates") && matchesFilters) {
                try {
                    double maxCarbs = Double.parseDouble(filters.get("carbohydrates"));
                    if (recipe.getNutriCarbs() > maxCarbs) {
                        matchesFilters = false;
                    }
                } catch (NumberFormatException e) {
                    // Skip invalid carbs value
                }
            }

            if (matchesFilters) {
                filteredResults.add(recipe);
            }
        }

        return filteredResults;
    }

    /**
    Load and display all favorite recipes
     */
    private void loadFavorites() {
        List<Recipe> favorites = favoritesUsecase.getFavourites();
        originalFavorites = new ArrayList<>(favorites);
        displayFavorites(favorites);
    }

    /**
     Display favorite recipes in the results container
     */
    private void displayFavorites(List<Recipe> favorites) {
        resultsContainer.removeAll();

        if (favorites.isEmpty()) {
            JLabel noFavoritesLabel = new JLabel("No favorite recipes saved yet.");
            resultsContainer.add(noFavoritesLabel);
        } else {
            for (Recipe recipe : favorites) {
                JPanel recipePanel = new JPanel();
                recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));
                recipePanel.setBorder(BorderFactory.createEtchedBorder());

                // Recipe title button
                JButton recipeButton = new JButton("<html><center>" + recipe.getName() + "</center></html>");
                recipeButton.setPreferredSize(new java.awt.Dimension(180, 60));
                recipeButton.addActionListener(e -> {
                    new SingleRecipeView(recipe);
                });

                JButton removeButton = new JButton("Remove from Favorites");
                removeButton.setPreferredSize(new java.awt.Dimension(180, 30));
                removeButton.addActionListener(e -> {
                    favoritesUsecase.removeFromFavourites(recipe);
                    JOptionPane.showMessageDialog(this.resultsContainer,
                            "Removed from favorites: " + recipe.getName(),
                            "Favorites", JOptionPane.INFORMATION_MESSAGE);
                    // Refresh the display
                    loadFavorites();
                });

                recipePanel.add(recipeButton);
                recipePanel.add(removeButton);

                JButton addToPlanner = new JButton("Add to Planner");
                if (mealPlannerUseCase.isSelected(recipe)) {
                    addToPlanner.setText("Remove from Planner");
                }

                addToPlanner.addActionListener(e -> {
                    if (mealPlannerUseCase.isSelected(recipe)) {
                        mealPlannerUseCase.removeFromPlanner(recipe);
                        addToPlanner.setText("Add to Planner");
                    } else {
                        mealPlannerUseCase.addToPlanner(recipe);
                        addToPlanner.setText("Remove from Planner");
                    }
                });
                recipePanel.add(addToPlanner);

                resultsContainer.add(recipePanel);
            }
        }

        resultsContainer.revalidate();
        resultsContainer.repaint();
    }


    private void createFavSorter() {
        List<Recipe> favourites = favoritesUsecase.getFavourites();
        JPopupMenu sortMenu = new JPopupMenu();
        sortMenu.add(new JLabel("Sort By:"));

        JMenuItem defaultItem = new JMenuItem("Default");
        defaultItem.addActionListener(e -> {
            Map<String, String> filters = getFilterCriteria();
            List<Recipe> filteredFavorites = filterFavorites(originalFavorites, filters);
            displayFavorites(filteredFavorites);
        });
        sortMenu.add(defaultItem);

        JMenuItem ingredientItem = new JMenuItem("Least Ingredients");
        ingredientItem.addActionListener(e -> {
            RecipeSorterUseCase ingredientSort = new RecipeSorterUseCase("ingredients");
            ingredientSort.sortRecipes(favourites);
            displayFavorites(favourites);
        });
        sortMenu.add(ingredientItem);

        JMenuItem timeItem = new JMenuItem("Least Prep Time");
        timeItem.addActionListener(e -> {
            RecipeSorterUseCase timeSort = new RecipeSorterUseCase("time");
            timeSort.sortRecipes(favourites);
            displayFavorites(favourites);
        });
        sortMenu.add(timeItem);

        JMenuItem leastCalItem = new JMenuItem("Least Calories");
        leastCalItem.addActionListener(e -> {
            RecipeSorterUseCase leastCalSort = new RecipeSorterUseCase("caloriesAscending");
            leastCalSort.sortRecipes(favourites);
            displayFavorites(favourites);
        });
        sortMenu.add(leastCalItem);

        JMenuItem mostCalItem = new JMenuItem("Most Calories");
        mostCalItem.addActionListener(e -> {
            RecipeSorterUseCase mostCalSort = new RecipeSorterUseCase("caloriesDescending");
            mostCalSort.sortRecipes(favourites);
            displayFavorites(favourites);
        });
        sortMenu.add(mostCalItem);

        JMenuItem mostProtItem = new JMenuItem("Most Protein");
        mostProtItem.addActionListener(e -> {
            RecipeSorterUseCase mostProtSort = new RecipeSorterUseCase("proteinDescending");
            mostProtSort.sortRecipes(favourites);
            displayFavorites(favourites);
        });
        sortMenu.add(mostProtItem);

        JMenuItem leastFatItem = new JMenuItem("Least Fat");
        leastFatItem.addActionListener(e -> {
            RecipeSorterUseCase leastFatSort = new RecipeSorterUseCase("fatAscending");
            leastFatSort.sortRecipes(favourites);
            displayFavorites(favourites);
        });
        sortMenu.add(leastFatItem);

        JMenuItem leastSugarItem = new JMenuItem("Least Sugar");
        leastSugarItem.addActionListener(e -> {
            RecipeSorterUseCase leastSugarSort = new RecipeSorterUseCase("sugarAscending");
            leastSugarSort.sortRecipes(favourites);
            displayFavorites(favourites);
        });
        sortMenu.add(leastSugarItem);

        String[] types = {
                "Breakfast", "Dinner", "Lunch", "Snack", "Desserts", "Drinks", "American",
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
                sorter.sortRecipes(favourites);
                displayFavorites(favourites);
            });
            sortMenu.add(menuItem);
        }

        sortButton.addActionListener(e -> {
            sortMenu.show(sortButton, 0, sortButton.getHeight() + 200);
        });
    }
}
package main.view;

import usecase.FavouritesUsecase;
import usecase.SearchRecipesUsecase;
import usecase.MealPlannerUsecase;
import entity.Recipe;
import usecase.sort.RecipeSorterUseCase;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExplorePageView extends RecipeView {

    private final SearchRecipesUsecase searchRecipesUseCase = new SearchRecipesUsecase();
    private final FavouritesUsecase favouritesUsecase = new FavouritesUsecase();
    //private final MealPlannerUsecase mealPlannerUsecase = new MealPlannerUsecase();

    private List<Recipe> allRecipes;
    private int currentPage = 0;
    private final int RECIPES_PER_PAGE = 8;
    //private MealPlannerUsecase mealPlannerUseCase;

    public ExplorePageView(MealPlannerUsecase mpUseCase) {
        super("Explore Page", mpUseCase);

        searchButton.addActionListener(e -> {
            Map<String, String> filters = getSearchFilters();            
            allRecipes = searchRecipesUseCase.execute(filters);
            currentPage = 0;
            showPage(currentPage);
            createResultSorter(allRecipes)
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

                JButton addToMealPlanButton = new JButton("Add to Meal Plan");
                addToMealPlanButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                addToMealPlanButton.addActionListener(e -> {
                    if(mealPlannerUseCase.isSelected(recipe)) {
                        System.out.println("already asdded");
                        mealPlannerUseCase.removeFromPlanner(recipe);
                        addToMealPlanButton.setText("Add to Meal Plan");
                    }
                    else {
                        mealPlannerUseCase.addToPlanner(recipe);
                        addToMealPlanButton.setText("Remove from Meal Plan");
                    }
                });

                recipePanel.add(recipeButton);
                recipePanel.add(favoriteButton);
                recipePanel.add(addToMealPlanButton);
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

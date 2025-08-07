package main.view;

import usecase.FavouritesUsecase;
import entity.Recipe;
import usecase.MealPlannerUsecase;
import usecase.sort.RecipeSorterUseCase;


import javax.swing.*;
import java.util.List;
import java.util.Map;

public class SavedRecipesView extends RecipeView {
    private final FavouritesUsecase favoritesUsecase = new FavouritesUsecase();

    public SavedRecipesView(MealPlannerUsecase mpUseCase) {
        super("Saved Recipes", mpUseCase);
        loadFavorites();

        createFavSorter();
    }

    /**
    Load and display all favorite recipes
     */
    private void loadFavorites() {
        List<Recipe> favorites = favoritesUsecase.getFavourites();
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
                recipePanel.add(addToPlanner);
                addToPlanner.addActionListener(e -> {
                    mealPlannerUseCase.addToPlanner(recipe);
                });

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
            List<Recipe> unsortedFavs = favoritesUsecase.getFavourites();
            displayFavorites(unsortedFavs);
        });
        sortMenu.add(defaultItem);

        JMenuItem favItem = new JMenuItem("Similar to Favourites");
        favItem.addActionListener(e -> {
            RecipeSorterUseCase favSort = new RecipeSorterUseCase("favs");
            favSort.sortRecipes(favourites);
            displayFavorites(favourites);
        });
        sortMenu.add(favItem);

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
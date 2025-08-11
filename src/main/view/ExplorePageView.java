package main.view;

import usecase.FavouritesUsecase;
import usecase.SearchRecipesUsecase;
import usecase.MealPlannerUsecase;
import entity.Recipe;
import usecase.sort.RecipeSorterUseCase;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExplorePageView extends RecipeView {

    private final SearchRecipesUsecase searchRecipesUseCase = new SearchRecipesUsecase();
    private final FavouritesUsecase favouritesUsecase = new FavouritesUsecase();

    private List<Recipe> currentResults = new ArrayList<>();

    public ExplorePageView(MealPlannerUsecase mpUseCase) {
        super("Explore Page", mpUseCase);

        // Search
        searchButton.addActionListener(e -> {
            Map<String, String> filters = getSearchFilters();
            List<Recipe> recipes = searchRecipesUseCase.execute(filters);
            currentResults = new ArrayList<>(recipes);
            updateResults(currentResults);
            createResultSorter(currentResults);
        });
    }

    private Map<String, String> getSearchFilters() {
        Map<String, String> filters = new HashMap<>();

        String ingredient = primaryIngredient.getText().trim();
        if (!ingredient.isEmpty()) {
            filters.put("q", ingredient);
        }

        String selectedDiet = (String) dietTypeDropdown.getSelectedItem();
        if (selectedDiet != null && !selectedDiet.isEmpty()) {
            filters.put("diet", selectedDiet);
        }

        String minCal = minCalories.getText().trim();
        String maxCal = maxCalories.getText().trim();
        if (!minCal.isEmpty() || !maxCal.isEmpty()) {
            String calorieRange = (!minCal.isEmpty() && !maxCal.isEmpty()) ? (minCal + "-" + maxCal)
                    : (!minCal.isEmpty()) ? (minCal + "+")
                    : ("0-" + maxCal);
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

    /** Render all results into the scrollable grid with image thumbnails. */
    public void updateResults(List<Recipe> recipes) {
        resultsContainer.removeAll();

        if (recipes == null || recipes.isEmpty()) {
            resultsContainer.add(new JLabel("No recipes found. Try adjusting your search criteria."));
        } else {
            for (Recipe recipe : recipes) {
                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(BorderFactory.createEtchedBorder());
                card.setPreferredSize(new Dimension(240, 240));

                // Thumbnail
                try {
                    String imgUrl = recipe.getImageUrl();
                    if (imgUrl != null && !imgUrl.isEmpty()) {
                        ImageIcon icon = new ImageIcon(new URL(imgUrl));
                        Image scaled = icon.getImage().getScaledInstance(200, 130, Image.SCALE_SMOOTH);
                        JLabel imageLabel = new JLabel(new ImageIcon(scaled));
                        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                        card.add(Box.createVerticalStrut(6));
                        card.add(imageLabel);
                    } else {
                        JLabel noImg = new JLabel("[No image]");
                        noImg.setAlignmentX(Component.CENTER_ALIGNMENT);
                        card.add(noImg);
                    }
                } catch (Exception ex) {
                    JLabel noImg = new JLabel("[No image]");
                    noImg.setAlignmentX(Component.CENTER_ALIGNMENT);
                    card.add(noImg);
                }

                // Title button
                JButton openBtn = new JButton("<html><center>" + recipe.getName() + "</center></html>");
                openBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                openBtn.addActionListener(e -> new SingleRecipeView(recipe));
                card.add(Box.createVerticalStrut(6));
                card.add(openBtn);

                // Favourites toggle
                JButton favBtn = new JButton(favouritesUsecase.isFavourite(recipe) ? "Remove from Favorites" : "Add to Favorites");
                favBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                favBtn.addActionListener(e -> {
                    if (favouritesUsecase.isFavourite(recipe)) {
                        favouritesUsecase.removeFromFavourites(recipe);
                        favBtn.setText("Add to Favorites");
                        JOptionPane.showMessageDialog(resultsContainer, "Removed from favorites: " + recipe.getName(),
                                "Favorites", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        favouritesUsecase.addToFavourites(recipe);
                        favBtn.setText("Remove from Favorites");
                        JOptionPane.showMessageDialog(resultsContainer, "Added to favorites: " + recipe.getName(),
                                "Favorites", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
                card.add(Box.createVerticalStrut(4));
                card.add(favBtn);

                resultsContainer.add(card);
            }
        }

        resultsContainer.revalidate();
        resultsContainer.repaint();
    }

    /** Sort menu; re-renders in the scroll grid. */
    private void createResultSorter(List<Recipe> recipes) {
        List<Recipe> results = new ArrayList<>(recipes);
        List<Recipe> unsortedResults = new ArrayList<>(results);

        JPopupMenu sortMenu = new JPopupMenu();
        sortMenu.add(new JLabel("Sort By:"));

        JMenuItem defaultItem = new JMenuItem("Default");
        defaultItem.addActionListener(e -> {
            currentResults = new ArrayList<>(unsortedResults);
            updateResults(currentResults);
        });
        sortMenu.add(defaultItem);

        JMenuItem favItem = new JMenuItem("Similar to Favourites");
        favItem.addActionListener(e -> {
            RecipeSorterUseCase favSort = new RecipeSorterUseCase("favs");
            favSort.sortRecipes(results);
            currentResults = new ArrayList<>(results);
            updateResults(currentResults);
        });
        sortMenu.add(favItem);

        JMenuItem ingredientItem = new JMenuItem("Least Ingredients");
        ingredientItem.addActionListener(e -> {
            RecipeSorterUseCase ingredientSort = new RecipeSorterUseCase("ingredients");
            ingredientSort.sortRecipes(results);
            currentResults = new ArrayList<>(results);
            updateResults(currentResults);
        });
        sortMenu.add(ingredientItem);

        JMenuItem timeItem = new JMenuItem("Least Prep Time");
        timeItem.addActionListener(e -> {
            RecipeSorterUseCase timeSort = new RecipeSorterUseCase("time");
            timeSort.sortRecipes(results);
            currentResults = new ArrayList<>(results);
            updateResults(currentResults);
        });
        sortMenu.add(timeItem);

        JMenuItem leastCalItem = new JMenuItem("Least Calories");
        leastCalItem.addActionListener(e -> {
            RecipeSorterUseCase leastCalSort = new RecipeSorterUseCase("caloriesAscending");
            leastCalSort.sortRecipes(results);
            currentResults = new ArrayList<>(results);
            updateResults(currentResults);
        });
        sortMenu.add(leastCalItem);

        JMenuItem mostCalItem = new JMenuItem("Most Calories");
        mostCalItem.addActionListener(e -> {
            RecipeSorterUseCase mostCalSort = new RecipeSorterUseCase("caloriesDescending");
            mostCalSort.sortRecipes(results);
            currentResults = new ArrayList<>(results);
            updateResults(currentResults);
        });
        sortMenu.add(mostCalItem);

        JMenuItem mostProtItem = new JMenuItem("Most Protein");
        mostProtItem.addActionListener(e -> {
            RecipeSorterUseCase mostProtSort = new RecipeSorterUseCase("proteinDescending");
            mostProtSort.sortRecipes(results);
            currentResults = new ArrayList<>(results);
            updateResults(currentResults);
        });
        sortMenu.add(mostProtItem);

        JMenuItem leastFatItem = new JMenuItem("Least Fat");
        leastFatItem.addActionListener(e -> {
            RecipeSorterUseCase leastFatSort = new RecipeSorterUseCase("fatAscending");
            leastFatSort.sortRecipes(results);
            currentResults = new ArrayList<>(results);
            updateResults(currentResults);
        });
        sortMenu.add(leastFatItem);

        JMenuItem leastSugarItem = new JMenuItem("Least Sugar");
        leastSugarItem.addActionListener(e -> {
            RecipeSorterUseCase leastSugarSort = new RecipeSorterUseCase("sugarAscending");
            leastSugarSort.sortRecipes(results);
            currentResults = new ArrayList<>(results);
            updateResults(currentResults);
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
                currentResults = new ArrayList<>(results);
                updateResults(currentResults);
            });
            sortMenu.add(menuItem);
        }

        sortButton.addActionListener(e -> sortMenu.show(sortButton, 0, sortButton.getHeight()));
    }
}

package main.view;

import entity.Recipe;
import use_case.MealPlannerUsecase;
import use_case.sort.RecipeSorterUseCase;

import interface_adapter.favourites.FavouritesController;
import interface_adapter.favourites.FavouritesState;
import interface_adapter.favourites.FavouritesViewModel;
import data_access.FileRecipeDataAccessObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.awt.event.ActionListener;

public class SavedRecipesView extends RecipeView implements PropertyChangeListener {

    private final FileRecipeDataAccessObject dataAccess = new FileRecipeDataAccessObject();
    private List<Recipe> originalFavorites = new ArrayList<>();

    private FavouritesController favouritesController;
    private FavouritesViewModel favouritesViewModel;


    public SavedRecipesView(MealPlannerUsecase mpUseCase) {
        super("Saved Recipes", mpUseCase);

        // Make the results area span the full window as a vertical list of rows
        resultsContainer.setLayout(new BoxLayout(resultsContainer, BoxLayout.Y_AXIS));

        loadFavorites();
        searchButton.addActionListener(e -> {
            Map<String, String> filters = getFilterCriteria();
            List<Recipe> filteredFavorites = filterFavorites(originalFavorites, filters);
            displayFavorites(filteredFavorites);
        });

        createFavSorter();
    }

    public void setFavouritesController(FavouritesController favouritesController) {
        this.favouritesController = favouritesController;
    }

    public void setFavouritesViewModel(FavouritesViewModel favouritesViewModel) {
        System.out.println("*** Setting favourites view model ***");
        this.favouritesViewModel = favouritesViewModel;
        this.favouritesViewModel.addPropertyChangeListener(this);
        System.out.println("Property change listener added to favourites view model");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final FavouritesState state = (FavouritesState) evt.getNewValue();
        if (state.getFavouritesError() != null) {
            JOptionPane.showMessageDialog(this, state.getFavouritesError());
        } else if (state.getMessage() != null) {

            SwingUtilities.invokeLater(() -> {
                loadFavorites();
                forceRefresh();  // Force refresh
                System.out.println("View refreshed!");
            });
        }
    }


    private Map<String, String> getFilterCriteria() {
        Map<String, String> filters = new HashMap<>();
        String ingredient = primaryIngredient.getText().trim();
        if (!ingredient.isEmpty()) filters.put("q", ingredient);

        String selectedDiet = (String) dietTypeDropdown.getSelectedItem();
        if (selectedDiet != null && !selectedDiet.isEmpty()) filters.put("diet", selectedDiet);

        String minCal = minCalories.getText().trim();
        String maxCal = maxCalories.getText().trim();
        if (!minCal.isEmpty() || !maxCal.isEmpty()) {
            String calorieRange = (!minCal.isEmpty() && !maxCal.isEmpty()) ? (minCal + "-" + maxCal)
                    : (!minCal.isEmpty()) ? (minCal + "+")
                    : ("0-" + maxCal);
            filters.put("calories", calorieRange);
        }

        addNutrientFilter(filters, "protein", protein.getText().trim());
        addNutrientFilter(filters, "fat",     maxFat.getText().trim());
        addNutrientFilter(filters, "sugar",   maxSugar.getText().trim());
        addNutrientFilter(filters, "carbohydrates", maxCarbs.getText().trim());
        return filters;
    }
    private void addNutrientFilter(Map<String, String> filters, String nutrient, String value) {
        if (!value.isEmpty()) filters.put(nutrient, value);
    }

    private List<Recipe> filterFavorites(List<Recipe> favorites, Map<String, String> filters) {
        if (filters.isEmpty()) return new ArrayList<>(favorites);

        List<Recipe> out = new ArrayList<>();
        for (Recipe r : favorites) {
            boolean ok = true;

            if (filters.containsKey("q")) {
                String q = filters.get("q").toLowerCase();
                boolean hit = r.getName().toLowerCase().contains(q)
                        || r.getMainIngredient().toLowerCase().contains(q)
                        || r.getIngredients().stream().anyMatch(s -> s.toLowerCase().contains(q));
                if (!hit) ok = false;
            }

            if (ok && filters.containsKey("diet")) {
                String want = filters.get("diet").toLowerCase();
                boolean hit = r.getDietType().stream().anyMatch(d -> d.toLowerCase().contains(want));
                if (!hit) ok = false;
            }

            if (ok && filters.containsKey("calories")) {
                String range = filters.get("calories");
                int cals = r.getNutriCalories();
                if (range.contains("-")) {
                    String[] p = range.split("-");
                    try {
                        int lo = Integer.parseInt(p[0]);
                        int hi = Integer.parseInt(p[1]);
                        if (cals < lo || cals > hi) ok = false;
                    } catch (NumberFormatException ignored) {}
                } else if (range.endsWith("+")) {
                    try {
                        int lo = Integer.parseInt(range.replace("+", ""));
                        if (cals < lo) ok = false;
                    } catch (NumberFormatException ignored) {}
                }
            }

            if (ok && filters.containsKey("protein")) {
                try { if (r.getNutriProtein() < Double.parseDouble(filters.get("protein"))) ok = false; }
                catch (NumberFormatException ignored) {}
            }
            if (ok && filters.containsKey("fat")) {
                try { if (r.getNutriFat() > Double.parseDouble(filters.get("fat"))) ok = false; }
                catch (NumberFormatException ignored) {}
            }
            if (ok && filters.containsKey("sugar")) {
                try { if (r.getNutriSugar() > Double.parseDouble(filters.get("sugar"))) ok = false; }
                catch (NumberFormatException ignored) {}
            }
            if (ok && filters.containsKey("carbohydrates")) {
                try { if (r.getNutriCarbs() > Double.parseDouble(filters.get("carbohydrates"))) ok = false; }
                catch (NumberFormatException ignored) {}
            }

            if (ok) out.add(r);
        }
        return out;
    }

    public void forceRefresh() {

        FileRecipeDataAccessObject freshDataAccess = new FileRecipeDataAccessObject();
        List<Recipe> freshFavorites = freshDataAccess.getAllFavourites();

        originalFavorites = new ArrayList<>(freshFavorites);
        displayFavorites(freshFavorites);

        this.revalidate();
        this.repaint();
    }

    public void loadFavorites() {
        List<Recipe> favorites = dataAccess.getAllFavourites();
        originalFavorites = new ArrayList<>(favorites);
        displayFavorites(favorites);
    }

    private void displayFavorites(List<Recipe> favorites) {
        resultsContainer.removeAll();

        if (favorites.isEmpty()) {
            resultsContainer.add(new JLabel("No favorite recipes saved yet."));
        } else {
            for (Recipe recipe : favorites) {
                resultsContainer.add(buildRow(recipe));
                resultsContainer.add(Box.createVerticalStrut(8));
            }
        }

        resultsContainer.revalidate();
        resultsContainer.repaint();
    }

    private JPanel buildRow(Recipe recipe) {
        JPanel row = new JPanel(new BorderLayout(12, 8));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210,210,210)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JLabel thumb = new JLabel("[Loading image]", SwingConstants.CENTER);
        thumb.setPreferredSize(new Dimension(200, 130));
        thumb.setBorder(BorderFactory.createLineBorder(new Color(230,230,230)));
        row.add(thumb, BorderLayout.WEST);

        String imgUrl = recipe.getImageUrl();
        if (imgUrl != null && !imgUrl.isEmpty()) {
            loadImageAsync(imgUrl, thumb, 200, 130);
        } else {
            thumb.setText("[No image]");
        }

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        JButton titleBtn = new JButton("<html><b>" + recipe.getName() + "</b></html>");
        titleBtn.setHorizontalAlignment(SwingConstants.LEFT);
        titleBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBtn.addActionListener(e -> new SingleRecipeView(recipe, mealPlannerUseCase));

        JLabel meta = new JLabel(
                String.format("<html>%s | %s | %s<br/>Calories: %d | Protein: %.0fg | Carbs: %.0fg | Fat: %.0fg</html>",
                        recipe.getCuisineType(), recipe.getMealType(), recipe.getDishType(),
                        recipe.getNutriCalories(), recipe.getNutriProtein(), recipe.getNutriCarbs(), recipe.getNutriFat())
        );
        meta.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton removeBtn = new JButton("Remove from Saved");
        removeBtn.addActionListener(e -> {
            if (favouritesController != null) {
                favouritesController.executeRemove(recipe);
            } else {
                JOptionPane.showMessageDialog(this, "Favourites system not initialized");
            }
        });


        JButton plannerBtn = new JButton(mealPlannerUseCase.isSelected(recipe) ? "Remove from Planner" : "Add to Planner");
        plannerBtn.addActionListener(e -> {
            if (mealPlannerUseCase.isSelected(recipe)) {
                mealPlannerUseCase.removeFromPlanner(recipe);
                plannerBtn.setText("Add to Planner");
            } else {
                mealPlannerUseCase.addToPlanner(recipe);
                plannerBtn.setText("Remove from Planner");
            }
        });

        actions.add(removeBtn);
        actions.add(plannerBtn);

        center.add(titleBtn);
        center.add(Box.createVerticalStrut(6));
        center.add(meta);
        center.add(Box.createVerticalStrut(8));
        center.add(actions);

        row.add(center, BorderLayout.CENTER);
        return row;
    }

    private void loadImageAsync(String url, JLabel target, int width, int height) {
        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() {
                try {
                    BufferedImage img = ImageIO.read(new URL(url));
                    if (img == null) return null;
                    Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaled);
                } catch (Throwable t) {
                    return null;
                }
            }
            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        target.setText(null);
                        target.setIcon(icon);
                    } else {
                        target.setText("[No image]");
                        target.setIcon(null);
                    }
                } catch (Exception e) {
                    target.setText("[No image]");
                    target.setIcon(null);
                }
            }
        }.execute();
    }

    private void createFavSorter() {
        //List<Recipe> favourites = favoritesUsecase.getFavourites();
        List<Recipe> favourites = dataAccess.getAllFavourites();
        JPopupMenu sortMenu = new JPopupMenu();
        sortMenu.add(new JLabel("Sort By:"));

        JMenuItem def = new JMenuItem("Default");
        def.addActionListener(e -> {
            Map<String, String> filters = getFilterCriteria();
            List<Recipe> filtered = filterFavorites(originalFavorites, filters);
            displayFavorites(filtered);
        });
        sortMenu.add(def);

        JMenuItem ingredientItem = new JMenuItem("Least Ingredients");
        ingredientItem.addActionListener(e -> { new RecipeSorterUseCase("ingredients").sortRecipes(favourites); displayFavorites(favourites); });
        sortMenu.add(ingredientItem);

        JMenuItem timeItem = new JMenuItem("Least Prep Time");
        timeItem.addActionListener(e -> { new RecipeSorterUseCase("time").sortRecipes(favourites); displayFavorites(favourites); });
        sortMenu.add(timeItem);

        JMenuItem leastCalItem = new JMenuItem("Least Calories");
        leastCalItem.addActionListener(e -> { new RecipeSorterUseCase("caloriesAscending").sortRecipes(favourites); displayFavorites(favourites); });
        sortMenu.add(leastCalItem);

        JMenuItem mostCalItem = new JMenuItem("Most Calories");
        mostCalItem.addActionListener(e -> { new RecipeSorterUseCase("caloriesDescending").sortRecipes(favourites); displayFavorites(favourites); });
        sortMenu.add(mostCalItem);

        JMenuItem mostProtItem = new JMenuItem("Most Protein");
        mostProtItem.addActionListener(e -> { new RecipeSorterUseCase("proteinDescending").sortRecipes(favourites); displayFavorites(favourites); });
        sortMenu.add(mostProtItem);

        JMenuItem leastFatItem = new JMenuItem("Least Fat");
        leastFatItem.addActionListener(e -> { new RecipeSorterUseCase("fatAscending").sortRecipes(favourites); displayFavorites(favourites); });
        sortMenu.add(leastFatItem);

        JMenuItem leastSugarItem = new JMenuItem("Least Sugar");
        leastSugarItem.addActionListener(e -> { new RecipeSorterUseCase("sugarAscending").sortRecipes(favourites); displayFavorites(favourites); });
        sortMenu.add(leastSugarItem);

        for (ActionListener al : sortButton.getActionListeners()) sortButton.removeActionListener(al);
        sortButton.addActionListener(e -> sortMenu.show(sortButton, 0, sortButton.getHeight()));
    }
}

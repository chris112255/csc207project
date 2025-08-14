package main.view;

import api.EdamamRecipeSearchGateway;
import entity.Recipe;
import interface_adapter.favourites.FavouritesController;
import use_case.MealPlannerUsecase;
import use_case.search.SearchRecipesUseCase;
import use_case.sort.RecipeSorterUseCase;
import data_access.FileRecipeDataAccessObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;
import java.util.List;

public class ExplorePageView extends RecipeView {

    private final SearchRecipesUseCase searchRecipesUseCase =
            new SearchRecipesUseCase(new EdamamRecipeSearchGateway());
    private FavouritesController favouritesController;
    private final FileRecipeDataAccessObject dataAccess = new FileRecipeDataAccessObject();
    private List<Recipe> currentResults = new ArrayList<>();
    private Map<Recipe, JButton> favoriteButtons = new HashMap<>();
    public ExplorePageView(MealPlannerUsecase mpUseCase) {
        super("Explore Page", mpUseCase);

        searchButton.addActionListener(e -> {
            Map<String, String> filters = getSearchFilters();

            String raw = primaryIngredient.getText().trim();
            List<String> tokens = parseTokens(raw);
            if (!tokens.isEmpty()) {
                filters.put("q", String.join(" ", tokens));
            }

            setBusy(true);
            new SwingWorker<List<Recipe>, Void>() {
                @Override
                protected List<Recipe> doInBackground() {
                    List<Recipe> results = searchRecipesUseCase.execute(filters);
                    if (!tokens.isEmpty()) {
                        results = filterByAllTokens(results, tokens);
                    }
                    return results;
                }
                @Override
                protected void done() {
                    try {
                        currentResults = new ArrayList<>(get());
                        updateResults(currentResults);
                        createResultSorter(currentResults);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(resultsContainer,
                                "Search failed: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        setBusy(false);
                    }
                }
            }.execute();
        });
    }

    /** Disable search & sort and show a wait cursor while background work runs. */
    private void setBusy(boolean busy) {
        searchButton.setEnabled(!busy);
        sortButton.setEnabled(!busy);
        Window w = getWindow();
        if (w != null) {
            w.setCursor(busy ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
                    : Cursor.getDefaultCursor());
        }
    }

    /** Split by comma, trim, and drop empties. */
    private List<String> parseTokens(String raw) {
        List<String> tokens = new ArrayList<>();
        if (raw == null || raw.isEmpty()) return tokens;
        for (String t : raw.split(",")) {
            String s = t.trim();
            if (!s.isEmpty()) tokens.add(s);
        }
        return tokens;
    }

    /** Keep only recipes that contain ALL tokens in name or ingredient lines (case-insensitive). */
    private List<Recipe> filterByAllTokens(List<Recipe> recipes, List<String> tokens) {
        if (recipes == null || recipes.isEmpty()) return recipes;
        List<String> needles = new ArrayList<>();
        for (String t : tokens) needles.add(t.toLowerCase());

        List<Recipe> out = new ArrayList<>();
        for (Recipe r : recipes) {
            String name = (r.getName() != null) ? r.getName().toLowerCase() : "";
            boolean all = true;
            for (String needle : needles) {
                boolean inName = name.contains(needle);
                boolean inIngredients = false;
                if (!inName && r.getIngredients() != null) {
                    for (String line : r.getIngredients()) {
                        if (line != null && line.toLowerCase().contains(needle)) {
                            inIngredients = true; break;
                        }
                    }
                }
                if (!(inName || inIngredients)) { all = false; break; }
            }
            if (all) out.add(r);
        }
        return out;
    }

    private Map<String, String> getSearchFilters() {
        Map<String, String> filters = new HashMap<>();

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

        // protein is a MIN in the UI; gateway maps proteinMin -> PROCNT=VALUE+
        addNutrientFilter(filters, "proteinMin", protein.getText().trim());
        // Others are treated as MAX in the gateway (0-VALUE)
        addNutrientFilter(filters, "fat", maxFat.getText().trim());
        addNutrientFilter(filters, "sugar", maxSugar.getText().trim());
        addNutrientFilter(filters, "carbohydrates", maxCarbs.getText().trim());

        return filters;
    }

    private void addNutrientFilter(Map<String, String> filters, String nutrient, String value) {
        if (!value.isEmpty()) filters.put(nutrient, value);
    }

    private boolean isFavourite(Recipe recipe) {
        return dataAccess.existsByUri(recipe.getUri());
    }

    /** Render all results into the scrollable grid with image thumbnails (loaded async). */
    public void updateResults(List<Recipe> recipes) {
        resultsContainer.removeAll();
        favoriteButtons.clear();

        if (recipes == null || recipes.isEmpty()) {
            resultsContainer.add(new JLabel("No recipes found. Try adjusting your search criteria."));
        } else {
            for (Recipe recipe : recipes) {
                JPanel card = new JPanel();
                card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
                card.setBorder(BorderFactory.createEtchedBorder());
                card.setPreferredSize(new Dimension(240, 260));

                // Placeholder thumbnail
                JLabel imageLabel = new JLabel("[Loading image]");
                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                card.add(Box.createVerticalStrut(6));
                card.add(imageLabel);

                // Load image off the EDT
                String imgUrl = recipe.getImageUrl();
                if (imgUrl != null && !imgUrl.isEmpty()) {
                    loadImageAsync(imgUrl, imageLabel, 200, 130);
                } else {
                    imageLabel.setText("[No image]");
                }

                // Title button
                JButton openBtn = new JButton("<html><center>" + recipe.getName() + "</center></html>");
                openBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                openBtn.addActionListener(e -> new SingleRecipeView(recipe, mealPlannerUseCase));
                card.add(Box.createVerticalStrut(6));
                card.add(openBtn);

                // Save/Remove toggle (labels updated)
                JButton favBtn = new JButton(isFavourite(recipe) ? "Remove from Saved" : "Save Recipe");
                favBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
                favBtn.addActionListener(e -> handleFavouriteAction(recipe, favBtn));
                favoriteButtons.put(recipe, favBtn);

                card.add(Box.createVerticalStrut(4));
                card.add(favBtn);

                resultsContainer.add(card);
            }
        }

        resultsContainer.revalidate();
        resultsContainer.repaint();
    }

    /** Load and scale an image without blocking the EDT; fall back to text on failure. */
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

        // Show the menu when clicking Sort (remove old listeners to avoid stacking)
        for (ActionListener al : sortButton.getActionListeners()) {
            sortButton.removeActionListener(al);
        }
        sortButton.addActionListener(e -> sortMenu.show(sortButton, 0, sortButton.getHeight()));
    }

    public void setFavouritesController(FavouritesController controller) {
        this.favouritesController = controller;
    }

    private void handleFavouriteAction(Recipe recipe, JButton favBtn) {
        if (favouritesController != null) {
            String currentText = favBtn.getText();

            if (currentText.equals("Remove from Saved")) {
                favouritesController.executeRemove(recipe);
                JOptionPane.showMessageDialog(this, "Removed from saved: " + recipe.getName());
                favBtn.setText("Save Recipe");
            } else {
                favouritesController.executeAdd(recipe);
                JOptionPane.showMessageDialog(this, "Added to saved: " + recipe.getName());
                favBtn.setText("Remove from Saved");
            }

        } else {
            JOptionPane.showMessageDialog(this, "Favourites system not initialized");
        }
    }

}

package main.view;

import entity.Recipe;
import usecase.FavouritesUsecase;
import usecase.MealPlannerUsecase;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class SingleRecipeView {

    private final Recipe recipe;
    private final FavouritesUsecase favouritesUsecase = new FavouritesUsecase();
    private final MealPlannerUsecase mealPlannerUsecase;
    private JFrame frame;

    public SingleRecipeView(Recipe recipe, MealPlannerUsecase mpUsecase) {
        this.recipe = recipe;
        this.mealPlannerUsecase = mpUsecase;
        this.createView();
    }

    // (Keep this for backward compatibility if other places call it,
    //  but note it won't share the same planner instance)
    public SingleRecipeView(Recipe recipe) {
        this(recipe, new MealPlannerUsecase());
    }

    private JPanel createNamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel name = new JLabel(recipe.getName());
        JLabel types = new JLabel(recipe.getCuisineType() +
                " | " + recipe.getMealType() + " | " + recipe.getDishType());
        panel.add(name);
        panel.add(types);
        return panel;
    }

    private JPanel createRecipeInfo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel cookTime = new JLabel("Cook Time (minutes): " + recipe.getPrepTime());
        JLabel ingredients = new JLabel("<html> Ingredients: " + recipe.getIngredients() + "</html>");
        panel.add(cookTime);
        panel.add(ingredients);
        return panel;
    }

    private JPanel createNutritionalInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Nutritional Information"));
        panel.add(new JLabel("Calories: " + recipe.getNutriCalories()));
        panel.add(new JLabel("<html>Protein (g): " + recipe.getNutriProtein() +
                "<br>Carbohydrates (g): " + recipe.getNutriCarbs() + "<br>Fat (g): " +
                recipe.getNutriFat() + "</html>"));
        panel.add(new JLabel("Sodium (mg): " + recipe.getNutriSodium()));
        panel.add(new JLabel("Sugar (g): " + recipe.getNutriSugar()));
        panel.add(new JLabel("Nutritional Warnings: " + recipe.getWarnings()));
        return panel;
    }

    private void createView() {
        frame = new JFrame("Recipe");
        frame.setSize(520, 720);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Control bar
        JPanel controlBar = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> frame.dispose());

        JButton favButton = new JButton(favouritesUsecase.isFavourite(recipe) ? "Remove from Favorites" : "Add to Favorites");
        favButton.addActionListener(e -> {
            if (favouritesUsecase.isFavourite(recipe)) {
                favouritesUsecase.removeFromFavourites(recipe);
                favButton.setText("Add to Favorites");
                JOptionPane.showMessageDialog(frame, "Removed from favorites: " + recipe.getName(),
                        "Favorites", JOptionPane.INFORMATION_MESSAGE);
            } else {
                favouritesUsecase.addToFavourites(recipe);
                favButton.setText("Remove from Favorites");
                JOptionPane.showMessageDialog(frame, "Added to favorites: " + recipe.getName(),
                        "Favorites", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // NEW: Add/Remove to Meal Planner
        JButton plannerBtn = new JButton(mealPlannerUsecase.isSelected(recipe) ? "Remove from Planner" : "Add to Planner");
        plannerBtn.addActionListener(e -> {
            if (mealPlannerUsecase.isSelected(recipe)) {
                mealPlannerUsecase.removeFromPlanner(recipe);
                plannerBtn.setText("Add to Planner");
                JOptionPane.showMessageDialog(frame, "Removed from planner: " + recipe.getName(),
                        "Meal Planner", JOptionPane.INFORMATION_MESSAGE);
            } else {
                mealPlannerUsecase.addToPlanner(recipe);
                plannerBtn.setText("Remove from Planner");
                JOptionPane.showMessageDialog(frame, "Added to planner: " + recipe.getName(),
                        "Meal Planner", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        JButton recipeButton = new JButton("Go to Recipe");
        recipeButton.addActionListener(e -> {
            try { Desktop.getDesktop().browse(new URI(recipe.getSourceUrl())); }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error opening link: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        controlBar.add(closeButton);
        controlBar.add(favButton);
        controlBar.add(plannerBtn);
        controlBar.add(recipeButton);
        panel.add(controlBar);

        JPanel namePanel = createNamePanel();
        panel.add(namePanel);

        // Image (safe load)
        JPanel imagePanel = new JPanel();
        String url = recipe.getImageUrl();
        if (url != null && !url.isEmpty()) {
            try {
                BufferedImage img = ImageIO.read(new URL(url));
                if (img != null) imagePanel.add(new JLabel(new ImageIcon(img)));
            } catch (IOException ignored) {
                imagePanel.add(new JLabel("[No image]"));
            }
        } else {
            imagePanel.add(new JLabel("[No image]"));
        }
        panel.add(imagePanel);

        panel.add(createRecipeInfo());
        panel.add(Box.createVerticalStrut(16));
        panel.add(createNutritionalInfoPanel());
        panel.add(Box.createVerticalGlue());

        // Align left
        controlBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        imagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

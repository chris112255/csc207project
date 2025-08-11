package main.view;

import entity.Recipe;
import usecase.FavouritesUsecase;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.List;

public class SingleRecipeView {

    public Recipe recipe;
    private final FavouritesUsecase favouritesUsecase = new FavouritesUsecase();
    private Component resultsContainer;

    public SingleRecipeView(Recipe recipe) {
        this.recipe = recipe;
        this.createView();
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

        JLabel nutritionalInfo = new JLabel("Nutritional Information");
        panel.add(nutritionalInfo);

        JLabel caloriesLabel = new JLabel("Calories: " + recipe.getNutriCalories());
        panel.add(caloriesLabel);

        JLabel macrosLabel = new JLabel("<html>Protein (g): " + recipe.getNutriProtein() +
                "<br>Carbohydrates (g): " + recipe.getNutriCarbs() + "<br>Fat (g): " +
                recipe.getNutriFat() + "</html>");
        panel.add(macrosLabel);

        JLabel sodiumLabel = new JLabel("Sodium (mg): " + recipe.getNutriSodium());
        panel.add(sodiumLabel);

        JLabel sugarLabel = new JLabel("Sugar (g): " + recipe.getNutriSugar());
        panel.add(sugarLabel);

        JLabel warningsLabel = new JLabel("Nutritional Warnings: " + recipe.getWarnings());
        panel.add(warningsLabel);

        return panel;
    }

    private void createView() {
        JFrame frame = new JFrame();
        frame.setSize(500, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel controlBar = new JPanel();
        JButton closeButton = new JButton("Close Recipe");
        closeButton.addActionListener(e -> {
            frame.dispose();
        });
        JButton favButton = new JButton("Add to Favorites");
        if (favouritesUsecase.isFavourite(recipe)) {
            favButton.setText("Remove from Favorites");
        }
        favButton.addActionListener(e -> {
            if (favouritesUsecase.isFavourite(recipe)) {
                favouritesUsecase.removeFromFavourites(recipe);
                favButton.setText("Add to Favorites");
                JOptionPane.showMessageDialog(this.resultsContainer,
                        "Removed from favorites: " + recipe.getName(),
                        "Favorites", JOptionPane.INFORMATION_MESSAGE);
            } else {
                favouritesUsecase.addToFavourites(recipe);
                favButton.setText("Remove from Favorites");
                JOptionPane.showMessageDialog(this.resultsContainer,
                        "Added to favorites: " + recipe.getName(),
                        "Favorites", JOptionPane.INFORMATION_MESSAGE);

            }
        });
        JButton recipeButton = new JButton("Go to Recipe");
        recipeButton.addActionListener(e -> {
                    try {
                        Desktop.getDesktop().browse(new URI(recipe.getSourceUrl()));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this.resultsContainer,
                                "Error getting link" + ex.getMessage(),
                                "Error", JOptionPane.INFORMATION_MESSAGE);
                    }
                });
        controlBar.add(closeButton);
        controlBar.add(favButton);
        controlBar.add(recipeButton);
        panel.add(controlBar);

        JPanel namePanel = createNamePanel();
        panel.add(namePanel);

        JPanel imagePanel = new JPanel();
        URL imageURL = null;
        ImageIcon imageIcon = null;
        try {
            imageURL = new URL(recipe.getImageUrl());
        } catch (MalformedURLException e) {
            JLabel failedImage = new JLabel(recipe.getImageUrl());
            panel.add(failedImage);
        }
        if (imageURL != null) {
            BufferedImage image = null;
            try {
                image = ImageIO.read(imageURL);
                imageIcon = new ImageIcon(image);
            } catch (IOException e) {
                JLabel failedImage = new JLabel(recipe.getImageUrl());
                panel.add(failedImage);
            }
            if (imageIcon != null) {
                JLabel imageLabel = new JLabel(imageIcon);
                imagePanel.add(imageLabel);
            }
        }
        panel.add(imagePanel);

        JPanel recipeInfoPanel = createRecipeInfo();
        panel.add(recipeInfoPanel);

        panel.add(Box.createVerticalStrut(20));

        JPanel nutritionalInfoPanel = createNutritionalInfoPanel();
        panel.add(nutritionalInfoPanel);

        panel.add(Box.createVerticalGlue());

        controlBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        namePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        imagePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        recipeInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        nutritionalInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}

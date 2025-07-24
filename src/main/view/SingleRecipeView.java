package main.view;

import entity.Recipe;

import javax.swing.*;
import java.util.List;

public class SingleRecipeView {

    Recipe recipe;

    public SingleRecipeView(Recipe recipe) {
        this.recipe = recipe;
        this.createView();
    }

    private JPanel createControlBar() {
        JPanel panel = new JPanel();
        JButton homeButton = new JButton("Home");
        JButton resultsButton = new JButton("Back to Search Results");
        JButton saveButton = new JButton("Save Recipe");
        panel.add(homeButton);
        panel.add(resultsButton);
        panel.add(saveButton);
        return panel;
    }

    private JPanel createNamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("Recipe Name");
        JLabel types = new JLabel("Cuisine | Meal Type | Dish Type");
        panel.add(name);
        panel.add(types);
        return panel;
    }

    private JPanel createRecipeInfo() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel cookTime = new JLabel("Cook Time");
        JLabel ingredients = new JLabel("Ingredient, ingredient, ingredient, ingredient");
        panel.add(cookTime);
        panel.add(ingredients);
        return panel;
    }

    private JPanel createNutritionalInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel nutritionalInfo = new JLabel("Nutritional Information");
        panel.add(nutritionalInfo);

        JLabel caloriesLabel = new JLabel("Calories: ");
        panel.add(caloriesLabel);

        JLabel macrosLabel = new JLabel("Protein: Carbohydrates: Fat: ");
        panel.add(macrosLabel);

        JLabel sodiumLabel = new JLabel("Sodium: ");
        panel.add(sodiumLabel);

        JLabel warningsLabel = new JLabel("Nutritional Warnings: ");
        panel.add(warningsLabel);

        return panel;
    }

    private void createView() {
        JFrame frame = new JFrame();
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel controlBar = createControlBar();
        panel.add(controlBar);

        JPanel namePanel = createNamePanel();
        panel.add(namePanel);

        panel.add(Box.createVerticalStrut(100));

        JPanel recipeInfoPanel = createRecipeInfo();
        panel.add(recipeInfoPanel);

        panel.add(Box.createVerticalStrut(100));

        JPanel nutritionalInfoPanel = createNutritionalInfoPanel();
        panel.add(nutritionalInfoPanel);

        panel.add(Box.createVerticalStrut(100));
        panel.add(Box.createVerticalGlue());

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}

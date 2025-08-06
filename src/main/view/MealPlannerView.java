package main.view;

import usecase.FavouritesUsecase;
import usecase.SearchRecipesUsecase;
import entity.Recipe;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealPlannerView {

    private final SearchRecipesUsecase searchRecipesUseCase = new SearchRecipesUsecase();
    String title = "Meal Planner";
    JFrame frame = new JFrame(title);
    JTextField maxCalories = new JTextField("10");
    JTextField minCalories = new JTextField("10");
    JTextField minProtein = new JTextField("10");
    JTextField maxCarbs = new JTextField("10");
    JButton saveButton = new JButton("Save");

    public MealPlannerView() {
        createView();

    }

    private void createView() {
        frame = new JFrame("Meal Planenr");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(createTitleBar());
        topPanel.add(createMacrosPanel());
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(createMealsListBox(), BorderLayout.CENTER);
        frame.add(createTotalMacrosPanel(), BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createMealsListBox() {
        JPanel panel = new JPanel();

        JPanel mealsPanel = new JPanel();
        mealsPanel.setLayout(new BoxLayout(mealsPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Selected Meals");
        JLabel meal = new JLabel("Meal1");
        // create meals based on how much in thing
        mealsPanel.add(titleLabel);
        mealsPanel.add(meal);
        panel.add(mealsPanel);
        return panel;
    }

    private JPanel createTotalMacrosPanel() {
        JPanel panel = new JPanel();

        JPanel totalMacrosPanel = new JPanel();
        totalMacrosPanel.setLayout(new BoxLayout(totalMacrosPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Total Macros");
        JLabel calories = new JLabel("Calories: ");
        // create meals based on how much in thing
        totalMacrosPanel.add(titleLabel);
        totalMacrosPanel.add(calories);
        panel.add(totalMacrosPanel);
        return panel;
    }

    private JPanel createInputBox(String text, JComponent inputComponent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(text);
        panel.add(label);
        panel.add(inputComponent);
        return panel;
    }

    private JPanel createTitleBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            frame.dispose();
            new HomePageView();
        });
        JLabel label = new JLabel(this.title);
        panel.add(homeButton);
        panel.add(label);
        return panel;
    }

    private JPanel createMacrosPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel subtitlePanel = new JPanel();
        JLabel subtitle = new JLabel("Daily Macro Goals");
        subtitlePanel.add(subtitle);
        panel.add(subtitlePanel);

        JPanel inputBoxes = new JPanel();
        minCalories = new JTextField(8);
        JPanel minCaloriesPanel = createInputBox("Min Calories", minCalories);
        inputBoxes.add(minCaloriesPanel);

        maxCalories = new JTextField(8);
        JPanel maxCaloriesPanel = createInputBox("Max Calories", maxCalories);
        inputBoxes.add(maxCaloriesPanel);

        maxCarbs = new JTextField(8);
        JPanel carbsPanel = createInputBox("Max Carbs", maxCarbs);
        inputBoxes.add(carbsPanel);

        minProtein = new JTextField(8);
        JPanel proteinPanel = createInputBox("Max Protein", minProtein);
        inputBoxes.add(proteinPanel);

        saveButton = new JButton("Save");
        inputBoxes.add(saveButton);

        panel.add(inputBoxes);
        return panel;
    }
}

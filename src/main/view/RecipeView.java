package main.view;

import usecase.MealPlannerUsecase;

import javax.swing.*;
import java.awt.*;

public class RecipeView {
    String title;
    protected JFrame frame;

    // Filter fields
    protected JTextField primaryIngredient;
    protected JComboBox<String> dietTypeDropdown;
    protected JTextField minCalories;
    protected JTextField maxCalories;
    protected JTextField protein;
    protected JTextField maxFat;
    protected JTextField maxSugar;
    protected JTextField maxCarbs;

    protected JButton searchButton;
    protected JButton sortButton;
    protected JPanel resultsContainer;
    protected JPanel resultsWrapper;
    protected JPanel pageControlPanel;
    protected JButton prevButton;
    protected JButton nextButton;
    protected MealPlannerUsecase mealPlannerUseCase;

    public RecipeView(String title, MealPlannerUsecase mpUseCase) {
        this.title = title;
        mealPlannerUseCase = mpUseCase;
        this.createView();
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
            new HomePageView(mealPlannerUseCase);
        });
        JLabel label = new JLabel(this.title);
        panel.add(homeButton);
        panel.add(label);
        return panel;
    }

    private JPanel createFiltersPanel() {
        JPanel panel = new JPanel();

        primaryIngredient = new JTextField(8);
        panel.add(createInputBox("Primary Ingredient", primaryIngredient));

        dietTypeDropdown = new JComboBox<>(new String[] {
                "", "balanced", "high-protein", "high-fiber", "low-fat", "low-carb", "low-sodium"
        });
        panel.add(createInputBox("Diet Type", dietTypeDropdown));

        minCalories = new JTextField(5);
        panel.add(createInputBox("Min Calories", minCalories));

        maxCalories = new JTextField(5);
        panel.add(createInputBox("Max Calories", maxCalories));

        protein = new JTextField(5);
        panel.add(createInputBox("Protein", protein));

        maxFat = new JTextField(5);
        panel.add(createInputBox("Max Fats", maxFat));

        maxSugar = new JTextField(5);
        panel.add(createInputBox("Max Sugar", maxSugar));

        maxCarbs = new JTextField(5);
        panel.add(createInputBox("Max Carbs", maxCarbs));

        searchButton = new JButton("Search");
        panel.add(searchButton);

        sortButton = new JButton("Sort By:");
        panel.add(sortButton);

        return panel;
    }

    private JPanel createResultsPanel() {
        resultsContainer = new JPanel();
        resultsContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        resultsWrapper = new JPanel(new BorderLayout());
        resultsWrapper.add(resultsContainer, BorderLayout.CENTER);
        return resultsWrapper;
    }

    protected JPanel createPageControlPanel() {
        pageControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        prevButton = new JButton("prev");
        nextButton = new JButton("next");

        pageControlPanel.add(prevButton);
        pageControlPanel.add(nextButton);

        return pageControlPanel;
    }

    private void createView() {
        frame = new JFrame("Recipe Manager");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(createTitleBar());
        topPanel.add(createFiltersPanel());

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(createResultsPanel(), BorderLayout.CENTER);
        frame.add(createPageControlPanel(), BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

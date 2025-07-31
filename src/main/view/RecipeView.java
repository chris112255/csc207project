package main.view;
import entity.Recipe;

import javax.swing.*;
import java.awt.*;

public class RecipeView {
    String title;
    protected JFrame frame;
    // Created instance variables for the filters
    protected JTextField primaryIngredient;
    protected JTextField dietType;
    protected JTextField minCalories;
    protected JTextField maxCalories;
    protected JTextField protein;
    protected JTextField maxFat;
    protected JTextField maxSugar;
    protected JTextField maxCarbs;
    protected JButton searchButton;
    protected JPanel resultsContainer;

    public RecipeView(String title) {
        this.title = title;
        this.createView();
    }

    private JPanel createInputBox(String text, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(text);
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    private JPanel createTitleBar() {
        JPanel panel = new JPanel();
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            frame.dispose(); // Close current window
            new HomePageView(); // Open home page
        });
        JLabel label = new JLabel(this.title);
        panel.add(homeButton);
        panel.add(label);
        return panel;
    }

    private JPanel createFiltersPanel(){
        JPanel panel = new JPanel();

        primaryIngredient = new JTextField();
        JPanel primaryIngredientContainer = createInputBox("Primary Ingredient", primaryIngredient);
        panel.add(primaryIngredientContainer);

        dietType = new JTextField();
        JPanel dietTypeContainer = createInputBox("Diet Type", dietType);
        panel.add(dietTypeContainer);

        minCalories = new JTextField();
        JPanel minCaloriesContainer = createInputBox("Min Calories", minCalories);
        panel.add(minCaloriesContainer);

        maxCalories = new JTextField();
        JPanel maxCaloriesContainer = createInputBox("Max Calories", maxCalories);
        panel.add(maxCaloriesContainer);

        protein = new JTextField();
        JPanel proteinContainer = createInputBox("Protein", protein);
        panel.add(proteinContainer);

        maxFat = new JTextField();
        JPanel maxFatContainer = createInputBox("Max Fats", maxFat);
        panel.add(maxFatContainer);

        maxSugar = new JTextField();
        JPanel maxSugarContainer = createInputBox("Max Sugar", maxSugar);
        panel.add(maxSugarContainer);

        maxCarbs = new JTextField();
        JPanel maxCarbsContainer = createInputBox("Max Carbs", maxCarbs);
        panel.add(maxCarbsContainer);

        searchButton = new JButton("Search");  // Store reference
        panel.add(searchButton);
        return panel;
    }

    private JScrollPane createResultsPanel() {
        resultsContainer = new JPanel();
        resultsContainer.setLayout(new GridLayout(0, 4, 10, 10)); // 4 columns with spacing

        JScrollPane scrollPane = new JScrollPane(resultsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel createPageControlPanel(){
        JPanel pageControls = new JPanel();
        JButton nextPage = new JButton("next");
        JButton prevPage = new JButton("prev");
        pageControls.add(nextPage);
        pageControls.add(prevPage);
        return pageControls;
    }

    private void createView(){
        frame = new JFrame("Recipe Manager");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //title bar
        JPanel titleBar = new JPanel();
        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(e -> {
            new HomePageView();
            frame.dispose();
        });
        JLabel label = new JLabel(this.title);
        titleBar.add(homeButton);
        titleBar.add(label);
        panel.add(titleBar);


        //filters
        JPanel filtersPanel = createFiltersPanel();
        panel.add(filtersPanel);

        // results
        JScrollPane resultsPanel = createResultsPanel();
        panel.add(resultsPanel);

        //next/prev page
        JPanel pageControlPanel = createPageControlPanel();
        panel.add(pageControlPanel);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
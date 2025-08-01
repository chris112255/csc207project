package main.view;

import entity.Recipe;

import javax.swing.*;
import java.awt.*;

public class RecipeView {
    String title;
    protected JFrame frame;

    // Filter fields
    protected JTextField primaryIngredient;
    protected JComboBox<String> dietTypeDropdown; // ✅ Updated to dropdown
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

    private JPanel createInputBox(String text, JComponent inputComponent) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(text);
        panel.add(label);
        panel.add(inputComponent);
        return panel;
    }

    private JPanel createTitleBar() {
        JPanel panel = new JPanel();
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

    private JPanel createFiltersPanel() {
        JPanel panel = new JPanel();

        primaryIngredient = new JTextField();
        panel.add(createInputBox("Primary Ingredient", primaryIngredient));

        dietTypeDropdown = new JComboBox<>(new String[] {
                "", "balanced", "high-protein", "high-fiber", "low-fat", "low-carb", "low-sodium"
        });
        panel.add(createInputBox("Diet Type", dietTypeDropdown));

        minCalories = new JTextField();
        panel.add(createInputBox("Min Calories", minCalories));

        maxCalories = new JTextField();
        panel.add(createInputBox("Max Calories", maxCalories));

        protein = new JTextField();
        panel.add(createInputBox("Protein", protein));

        maxFat = new JTextField();
        panel.add(createInputBox("Max Fats", maxFat));

        maxSugar = new JTextField();
        panel.add(createInputBox("Max Sugar", maxSugar));

        maxCarbs = new JTextField();
        panel.add(createInputBox("Max Carbs", maxCarbs));

        searchButton = new JButton("Search");
        panel.add(searchButton);

        return panel;
    }

    private JScrollPane createResultsPanel() {
        resultsContainer = new JPanel();
        resultsContainer.setLayout(new GridLayout(0, 4, 10, 10));

        JScrollPane scrollPane = new JScrollPane(resultsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private JPanel createPageControlPanel() {
        JPanel pageControls = new JPanel();
        JButton nextPage = new JButton("next");
        JButton prevPage = new JButton("prev");
        pageControls.add(prevPage);
        pageControls.add(nextPage);
        return pageControls;
    }

    private void createView() {
        frame = new JFrame("Recipe Manager");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createTitleBar());
        panel.add(createFiltersPanel());
        panel.add(createResultsPanel());
        panel.add(createPageControlPanel());

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

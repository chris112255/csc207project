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
    protected MealPlannerUsecase mealPlannerUseCase;

    public RecipeView(String title, MealPlannerUsecase mpUseCase) {
        this.title = title;
        this.mealPlannerUseCase = mpUseCase;
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));

        primaryIngredient = new JTextField(10);
        panel.add(createInputBox("Primary Ingredient", primaryIngredient));

        dietTypeDropdown = new JComboBox<>(new String[] {
                "", "balanced", "high-protein", "high-fiber", "low-fat", "low-carb", "low-sodium"
        });
        panel.add(createInputBox("Diet Type", dietTypeDropdown));

        minCalories = new JTextField(6);
        panel.add(createInputBox("Min Calories", minCalories));

        maxCalories = new JTextField(6);
        panel.add(createInputBox("Max Calories", maxCalories));

        protein = new JTextField(6);
        // CHANGED: label indicates minimum protein
        panel.add(createInputBox("Protein (g min)", protein));

        maxFat = new JTextField(6);
        panel.add(createInputBox("Max Fat (g)", maxFat));

        maxSugar = new JTextField(6);
        panel.add(createInputBox("Max Sugar (g)", maxSugar));

        maxCarbs = new JTextField(6);
        panel.add(createInputBox("Max Carbs (g)", maxCarbs));

        searchButton = new JButton("Search");
        panel.add(searchButton);

        sortButton = new JButton("Sort By");
        panel.add(sortButton);

        return panel;
    }

    /** Results area WITH a scroll pane (no pagination buttons). */
    private JScrollPane createResultsPanel() {
        resultsContainer = new JPanel();
        // 4 columns, as many rows as needed
        resultsContainer.setLayout(new GridLayout(0, 4, 12, 12));
        resultsContainer.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane scrollPane = new JScrollPane(resultsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private void createView() {
        frame = new JFrame("Recipe Manager");
        frame.setSize(1100, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.add(createTitleBar());
        topPanel.add(createFiltersPanel());

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(createResultsPanel(), BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

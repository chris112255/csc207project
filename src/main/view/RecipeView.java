package main.view;

import usecase.MealPlannerUsecase;

import javax.swing.*;
import java.awt.*;

public class RecipeView extends JPanel {
    String title;

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

    protected JPanel createFiltersPanel() {
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
        // default grid; subclasses may change layout (Saved uses BoxLayout)
        resultsContainer.setLayout(new GridLayout(0, 4, 12, 12));
        resultsContainer.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JScrollPane scrollPane = new JScrollPane(resultsContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private void createView() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(this.title, SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(titleLabel);

        topPanel.add(createFiltersPanel());
        add(topPanel, BorderLayout.NORTH);
        add(createResultsPanel(), BorderLayout.CENTER);
    }

    /** Handy for children needing the enclosing window (e.g., to set cursor). */
    protected Window getWindow() {
        return SwingUtilities.getWindowAncestor(this);
    }
}

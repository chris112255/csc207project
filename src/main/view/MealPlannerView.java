package main.view;

import usecase.FavouritesUsecase;
import usecase.MealPlannerUsecase;
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
    JTextField maxFat = new JTextField("10");
    JButton saveButton = new JButton("Save");
    JLabel resultsCalories = new JLabel("");
    JLabel resultsProtein = new JLabel("");
    JLabel resultsCarbs = new JLabel("");
    JLabel resultsFat = new JLabel("");
    JPanel resultsPanel = new JPanel();

    private MealPlannerUsecase mealPlannerUsecase;

    public MealPlannerView(MealPlannerUsecase mpUsecase) {
        mealPlannerUsecase = mpUsecase;
        createView();
    }

    private void createView() {
        frame = new JFrame("Meal Planenr");
        frame.setSize(1000, 600);
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
        mealsPanel.add(titleLabel);

        for (int i = 0; i < mealPlannerUsecase.getMeals().size(); i++) {
            JPanel mealPanel = new JPanel();

            JLabel mealLabel = new JLabel(mealPlannerUsecase.getMeals().get(i).getName());
            JButton removeMealButton = new JButton("Remove Meal");
            int finalI = i;
            removeMealButton.addActionListener(e -> {
                mealPlannerUsecase.removeFromPlanner(mealPlannerUsecase.getMeals().get(finalI));
                mealsPanel.remove(mealPanel);
                mealsPanel.revalidate();
                mealsPanel.repaint();
            });

            mealPanel.add(mealLabel);
            mealPanel.add(removeMealButton);
            mealsPanel.add(mealPanel);
        }

        //System.out.println(mealPlannerUsecase.getMeals().get(0).getName());

        panel.add(mealsPanel);
        return panel;
    }

    private JPanel createTotalMacrosPanel() {
        JPanel panel = new JPanel();

        JPanel totalMacrosPanel = new JPanel();
        totalMacrosPanel.setLayout(new BoxLayout(totalMacrosPanel, BoxLayout.Y_AXIS));
        List<Recipe> plannedMeals = mealPlannerUsecase.getMeals();

        JLabel titleLabel = new JLabel("Macros");

        totalMacrosPanel.add(titleLabel);
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.add(resultsCalories);
        resultsPanel.add(resultsProtein);
        resultsPanel.add(resultsCarbs);
        resultsPanel.add(resultsFat);
        totalMacrosPanel.add(resultsPanel);
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
            new HomePageView(mealPlannerUsecase);
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

        maxFat = new JTextField(8);
        JPanel fatPanel = createInputBox("Max Fat", maxFat);
        inputBoxes.add(fatPanel);

        minProtein = new JTextField(8);
        JPanel proteinPanel = createInputBox("Min Protein", minProtein);
        inputBoxes.add(proteinPanel);

        saveButton = new JButton("Save");

        saveButton.addActionListener(e -> {
            int minCaloriesVal = Integer.parseInt(minCalories.getText());
            int maxCaloriesVal = Integer.parseInt(maxCalories.getText());
            int maxCarbsVal = Integer.parseInt(maxCarbs.getText());
            int maxFatVal = Integer.parseInt(maxFat.getText());
            int minProteinVal = Integer.parseInt(minProtein.getText());

            resultsCalories.setText(mealPlannerUsecase.calculateCalories(minCaloriesVal, maxCaloriesVal));
            resultsCarbs.setText(mealPlannerUsecase.calculateCarbs(maxCarbsVal));
            resultsFat.setText(mealPlannerUsecase.calculateFat(maxFatVal));
            resultsProtein.setText(mealPlannerUsecase.calculateProtein(minProteinVal));

            resultsPanel.invalidate();
            resultsPanel.repaint();
        });

        inputBoxes.add(saveButton);

        panel.add(inputBoxes);
        return panel;
    }
}

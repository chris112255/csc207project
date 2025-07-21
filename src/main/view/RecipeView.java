package main.view;
import javax.swing.*;

// should probably be an interface

public class RecipeView {
    String title;

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

    private JPanel createTitleBar(){
        JPanel panel = new JPanel();
        JButton homeButton = new JButton("Home");
        JLabel label = new JLabel(this.title);
        panel.add(homeButton);
        panel.add(label);
        return panel;
    }

    private JPanel createFiltersPanel(){
        JPanel panel = new JPanel();
        JTextField name = new JTextField();
        JPanel nameContainer = createInputBox("Name", name);
        panel.add(nameContainer);

        JTextField primaryIngredient = new JTextField();
        JPanel primaryIngredientContainer = createInputBox("Primary Ingredient", primaryIngredient);
        panel.add(primaryIngredientContainer);

        JTextField dietType = new JTextField();
        JPanel dietTypeContainer = createInputBox("Diet Type", dietType);
        panel.add(dietTypeContainer);

        JTextField minCalories = new JTextField();
        JPanel minCaloriesContainer = createInputBox("Min Calories", minCalories);
        panel.add(minCaloriesContainer);

        JTextField maxCalories = new JTextField();
        JPanel maxCaloriesContainer = createInputBox("Max Calories", maxCalories);
        panel.add(maxCaloriesContainer);

        JTextField protein = new JTextField();
        JPanel proteinContainer = createInputBox("Protein", protein);
        panel.add(proteinContainer);

        JTextField maxFat = new JTextField();
        JPanel maxFatContainer = createInputBox("Max Fats", maxFat);
        panel.add(maxFatContainer);

        JTextField maxSugar = new JTextField();
        JPanel maxSugarContainer = createInputBox("Max Sugar", maxSugar);
        panel.add(maxSugarContainer);

        JTextField maxCarbs = new JTextField();
        JPanel maxCarbsContainer = createInputBox("Max Carbs", maxCarbs);
        panel.add(maxCarbsContainer);

        JButton searchButton = new JButton("Search");
        panel.add(searchButton);
        return panel;
    }

    private JPanel createResultsPanel(){
        JPanel results = new JPanel();
        results.setLayout(new BoxLayout(results, BoxLayout.Y_AXIS));
        JPanel resultsRow1 = new JPanel();
        JPanel resultsRow2 = new JPanel();

        JButton result1 = new JButton("result1");
        JButton result2 = new JButton("result2");
        JButton result3 = new JButton("result3");
        JButton result4 = new JButton("result4");
        JButton[] resultList1 = {result1, result2, result3, result4};

        for(JButton button : resultList1){
            resultsRow1.add(button);
        }

        JButton result5 = new JButton("result5");
        JButton result6 = new JButton("result6");
        JButton result7 = new JButton("result7");
        JButton result8 = new JButton("result8");
        JButton[] resultList2 = {result5, result6, result7, result8};

        for(JButton button : resultList2){
            resultsRow2.add(button);
        }

        results.add(resultsRow1);
        results.add(resultsRow2);
        return results;
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
        JFrame frame = new JFrame("Recipe Manager");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //title bar
        JPanel titleBar = createTitleBar();
        panel.add(titleBar);

        //filters
        JPanel filtersPanel = createFiltersPanel();
        panel.add(filtersPanel);

        // results
        JPanel resultsPanel = createResultsPanel();
        panel.add(resultsPanel);

        //next/prev page
        JPanel pageControlPanel = createPageControlPanel();
        panel.add(pageControlPanel);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
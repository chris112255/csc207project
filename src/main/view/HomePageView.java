package main.view;
import javax.swing.*;
import api.EdamamRecipeSearchGateway;
import usecase.MealPlannerUsecase;
import usecase.search.SearchRecipesUseCase;


public class HomePageView {
    private MealPlannerUsecase mealPlannerUsecase;

    public HomePageView() {
        mealPlannerUsecase = new MealPlannerUsecase();
        createView();
    }

    public HomePageView(MealPlannerUsecase mealPlannerUsecase) {
        this.mealPlannerUsecase = mealPlannerUsecase;
        createView();
    }

    private void createView() {
        JFrame frame = new JFrame("Recipe Manager");
        frame.setSize(1100, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create panel
        JPanel buttons = new JPanel();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Recipe Manager");
        panel.add(label);

        JButton search = new JButton("Search Recipes");
        search.addActionListener(e -> {
            frame.dispose(); // Close current window
            new ExplorePageView(mealPlannerUsecase); // Open explore page
        });
        buttons.add(search);

        JButton favourites = new JButton("Favourites");
        favourites.addActionListener(e -> {
            frame.dispose(); // Close current window
            new SavedRecipesView(mealPlannerUsecase); // Open saved recipes page
        });
        buttons.add(favourites);

        JButton planner = new JButton("Meal Planner");
        planner.addActionListener(e -> {
            frame.dispose(); // Close current window
            new MealPlannerView(mealPlannerUsecase); // Open meal planenr page
        });
        buttons.add(planner);

        panel.add(buttons);
        frame.add(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
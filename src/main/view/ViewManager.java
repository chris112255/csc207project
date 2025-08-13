package main.view;

import api.EdamamRecipeSearchGateway;
import usecase.MealPlannerUsecase;
import usecase.search.SearchRecipesUseCase;

import javax.swing.*;
import java.awt.*;

public class ViewManager {

    // Card names
    public static final String CARD_HOME    = "HOME";
    public static final String CARD_EXPLORE = "EXPLORE";
    public static final String CARD_SAVED   = "SAVED";
    public static final String CARD_PLANNER = "PLANNER";

    private final JFrame frame;
    private final JPanel cards;
    private final CardLayout cl;

    // Persisted use case for the whole session
    private final MealPlannerUsecase plannerUsecase = new MealPlannerUsecase();

    public ViewManager() {
        // ----- Window -----
        frame = new JFrame("Recipe Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1250, 750);
        frame.setLayout(new BorderLayout());

        // ----- Top nav -----
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnHome    = new JButton("Home");
        JButton btnExplore = new JButton("Search Recipes");
        JButton btnSaved   = new JButton("Saved Recipes");
        JButton btnPlanner = new JButton("Meal Planner");
        nav.add(btnHome);
        nav.add(btnExplore);
        nav.add(btnSaved);
        nav.add(btnPlanner);
        frame.add(nav, BorderLayout.NORTH);

        // ----- Cards container -----
        cards = new JPanel();
        cl = new CardLayout();
        cards.setLayout(cl);

        // Views
        SearchRecipesUseCase searchUseCase =
                new SearchRecipesUseCase(new EdamamRecipeSearchGateway());

        ExplorePageView explorePanel = new ExplorePageView(searchUseCase);
        SavedRecipesView savedPanel  = new SavedRecipesView();
        MealPlannerView plannerPanel = new MealPlannerView(plannerUsecase);

        HomePageView homePanel = new HomePageView(
                () -> cl.show(cards, CARD_EXPLORE),
                () -> cl.show(cards, CARD_SAVED),
                () -> { plannerPanel.refreshMeals(); cl.show(cards, CARD_PLANNER); }
        );

        // Add cards
        cards.add(homePanel,   CARD_HOME);
        cards.add(explorePanel, CARD_EXPLORE);
        cards.add(savedPanel,   CARD_SAVED);
        cards.add(plannerPanel, CARD_PLANNER);

        frame.add(cards, BorderLayout.CENTER);

        // ----- Wire nav buttons -----
        btnHome.addActionListener(e -> cl.show(cards, CARD_HOME));
        btnExplore.addActionListener(e -> cl.show(cards, CARD_EXPLORE));
        btnSaved.addActionListener(e -> cl.show(cards, CARD_SAVED));
        btnPlanner.addActionListener(e -> {
            plannerPanel.refreshMeals();      // ensure latest selections are shown
            cl.show(cards, CARD_PLANNER);
        });

        // Start on Home
        cl.show(cards, CARD_HOME);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Keep rocky.tu’s approach: launch ViewManager on the EDT
        SwingUtilities.invokeLater(ViewManager::new);
    }
}

package main.view;

import usecase.MealPlannerUsecase;

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
    private final MealPlannerUsecase mp = new MealPlannerUsecase();

    // Panels created once (state persists while app runs)
    private final HomePageView homePanel;
    private final ExplorePageView explorePanel;
    private final SavedRecipesView savedPanel;
    private final MealPlannerView plannerPanel;

    public ViewManager() {
        frame = new JFrame("Recipe Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 750);
        frame.setLayout(new BorderLayout());

        // ---- Top navigation (always visible) ----
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnHome    = new JButton("Home");
        JButton btnExplore = new JButton("Search Recipes");
        JButton btnSaved   = new JButton("Saved Recipes");
        JButton btnPlanner = new JButton("Meal Planner");
        nav.add(btnHome); nav.add(btnExplore); nav.add(btnSaved); nav.add(btnPlanner);
        frame.add(nav, BorderLayout.NORTH);

        // ---- Card container ----
        cl = new CardLayout();
        cards = new JPanel(cl);

        // Create dependent panels FIRST so lambdas can reference them safely
        plannerPanel = new MealPlannerView(mp);
        explorePanel = new ExplorePageView(mp);
        savedPanel   = new SavedRecipesView(mp);

        // Home page (its "Meal Planner" action calls refreshMeals then shows the card)
        homePanel = new HomePageView(
                () -> cl.show(cards, CARD_EXPLORE),
                () -> cl.show(cards, CARD_SAVED),
                () -> { plannerPanel.refreshMeals(); cl.show(cards, CARD_PLANNER); }
        );

        // Add cards
        cards.add(homePanel,    CARD_HOME);
        cards.add(explorePanel, CARD_EXPLORE);
        cards.add(savedPanel,   CARD_SAVED);
        cards.add(plannerPanel, CARD_PLANNER);

        frame.add(cards, BorderLayout.CENTER);

        // ---- Wire nav buttons ----
        btnHome.addActionListener(e -> cl.show(cards, CARD_HOME));
        btnExplore.addActionListener(e -> cl.show(cards, CARD_EXPLORE));
        btnSaved.addActionListener(e -> cl.show(cards, CARD_SAVED));
        btnPlanner.addActionListener(e -> {
            plannerPanel.refreshMeals();   // ensure latest selections are shown
            cl.show(cards, CARD_PLANNER);
        });

        // Start on Home
        cl.show(cards, CARD_HOME);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ViewManager::new);
    }
}

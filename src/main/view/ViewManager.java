package main.view;

import use_case.MealPlannerUsecase;
import interface_adapter.favourites.FavouritesController;
import interface_adapter.favourites.FavouritesViewModel;

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
    private final MealPlannerUsecase plannerUsecase;

    // Panels created once so state persists
    private final HomePageView homePanel;
    private final ExplorePageView explorePanel;
    private final SavedRecipesView savedPanel;
    private final MealPlannerView plannerPanel;

    public ViewManager(MealPlannerUsecase plannerUsecase) {
        this.plannerUsecase = plannerUsecase;
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
        cl = new CardLayout();
        cards = new JPanel(cl);

        // Create panels (planner first so lambdas can call refreshMeals)
        plannerPanel = new MealPlannerView(plannerUsecase);
        explorePanel = new ExplorePageView(plannerUsecase);
        savedPanel   = new SavedRecipesView(plannerUsecase);

        homePanel = new HomePageView(
                () -> cl.show(cards, CARD_EXPLORE),
                () -> {
                    savedPanel.loadFavorites();
                    cl.show(cards, CARD_SAVED);
                },
                () -> { plannerPanel.refreshMeals(); cl.show(cards, CARD_PLANNER); }
        );

        // Add cards
        cards.add(homePanel,    CARD_HOME);
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
    public void setFavouritesController(FavouritesController controller) {
        savedPanel.setFavouritesController(controller);
        explorePanel.setFavouritesController(controller);
    }

    public void setFavouritesViewModel(FavouritesViewModel viewModel) {
        savedPanel.setFavouritesViewModel(viewModel);
    }

    public JFrame getFrame() {
        return frame;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            app.AppBuilder appBuilder = new app.AppBuilder();
            JFrame application = appBuilder
                    .addViewManager()
                    .addFavouritesUseCase()
                    .build();
        });
    }
}

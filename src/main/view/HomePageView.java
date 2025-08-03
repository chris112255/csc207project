package main.view;
import javax.swing.*;
import api.EdamamRecipeSearchGateway;
import usecase.search.SearchRecipesUseCase;


public class HomePageView {
    public HomePageView() {
        createView();
    }

    private void createView() {
        JFrame frame = new JFrame("Recipe Manager");
        frame.setSize(800, 600);
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
            new ExplorePageView(); // Open explore page
        });
        buttons.add(search);

        JButton favourites = new JButton("Favourites");
        favourites.addActionListener(e -> {
            frame.dispose(); // Close current window
            new SavedRecipesView(); // Open saved recipes page
        });
        buttons.add(favourites);
        panel.add(buttons);
        frame.add(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
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

        JLabel label = new JLabel("Receipe Manager");
        panel.add(label);

        JButton search = new JButton("Search Recipes");
        search.addActionListener(e -> {
            SearchRecipesUseCase useCase = new SearchRecipesUseCase(new EdamamRecipeSearchGateway());
            new ExplorePageView(useCase);
            frame.dispose();
        });
        buttons.add(search);
        JButton favourites = new JButton("Favourites");
        favourites.addActionListener(e -> {
            new SavedRecipesView();
            frame.dispose();
        });
        buttons.add(favourites);
        panel.add(buttons);
        frame.add(panel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
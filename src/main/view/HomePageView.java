package main.view;
import javax.swing.*;

public class HomePageView {
    public HomePageView() {
        createView();
    }

    private void createView() {
        JFrame frame = new JFrame("Recipe Manager");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create panel
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Receipe Manager");
        panel.add(label);

        JPanel buttons = new JPanel();
        JButton search = new JButton("Search Recipes");
        search.addActionListener(e -> {
            new ExplorePageView();
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
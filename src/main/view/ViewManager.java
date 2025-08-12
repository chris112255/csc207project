package main.view;

import javax.swing.JPanel;

/**
 * The View Manager for the program. It listens for property change events
 * in the ViewManagerModel and updates which View should be visible.
 */

public class ViewManager {
    private final JPanel view;

    public ViewManager(JPanel view) {
        this.view = view;
    }

    public static void main(String[] args) {
        HomePageView homePageView = new HomePageView();
    }
}

package main.view;

import usecase.FavouritesUsecase;
import entity.Recipe;

import javax.swing.*;
import java.util.List;

public class SavedRecipesView extends RecipeView {
    private final FavouritesUsecase favoritesUsecase = new FavouritesUsecase();

    public SavedRecipesView() {
        super("Saved Recipes");
        loadFavorites();
    }

    /**
    Load and display all favorite recipes
     */
    private void loadFavorites() {
        List<Recipe> favorites = favoritesUsecase.getFavourites();
        displayFavorites(favorites);
    }

    /**
     Display favorite recipes in the results container
     */
    private void displayFavorites(List<Recipe> favorites) {
        resultsContainer.removeAll();

        if (favorites.isEmpty()) {
            JLabel noFavoritesLabel = new JLabel("No favorite recipes saved yet.");
            resultsContainer.add(noFavoritesLabel);
        } else {
            for (Recipe recipe : favorites) {
                JPanel recipePanel = new JPanel();
                recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));
                recipePanel.setBorder(BorderFactory.createEtchedBorder());

                // Recipe title button
                JButton recipeButton = new JButton("<html><center>" + recipe.getName() + "</center></html>");
                recipeButton.setPreferredSize(new java.awt.Dimension(180, 60));

                JButton removeButton = new JButton("Remove from Favorites");
                removeButton.setPreferredSize(new java.awt.Dimension(180, 30));
                removeButton.addActionListener(e -> {
                    favoritesUsecase.removeFromFavourites(recipe);
                    JOptionPane.showMessageDialog(this.resultsContainer,
                            "Removed from favorites: " + recipe.getName(),
                            "Favorites", JOptionPane.INFORMATION_MESSAGE);
                    // Refresh the display
                    loadFavorites();
                });

                recipePanel.add(recipeButton);
                recipePanel.add(removeButton);
                resultsContainer.add(recipePanel);
            }
        }

        resultsContainer.revalidate();
        resultsContainer.repaint();
    }

}
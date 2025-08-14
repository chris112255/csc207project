package app;

import javax.swing.JFrame;

import data_access.FileRecipeDataAccessObject;
import interface_adapter.favourites.FavouritesController;
import interface_adapter.favourites.FavouritesPresenter;
import interface_adapter.favourites.FavouritesViewModel;
import usecase.favourites.FavouritesInputBoundary;
import usecase.favourites.FavouritesInteractor;
import usecase.favourites.FavouritesOutputBoundary;
import main.view.ViewManager;
import usecase.MealPlannerUsecase;

/**
 * The AppBuilder class is responsible for putting together the pieces of
 * our CA architecture; piece by piece.
 */
public class AppBuilder {
    private final FileRecipeDataAccessObject recipeDataAccessObject = new FileRecipeDataAccessObject();
    private final MealPlannerUsecase mealPlannerUsecase = new MealPlannerUsecase();

    private FavouritesViewModel favouritesViewModel;
    private ViewManager viewManager;

    public AppBuilder() {
        System.out.println("*** AppBuilder constructor called ***");
    }

    /**
     * Adds the Favourites Use Case to the application.
     * @return this builder
     */
    public AppBuilder addFavouritesUseCase() {
        System.out.println("*** AppBuilder.addFavouritesUseCase() called ***");
        favouritesViewModel = new FavouritesViewModel();

        final FavouritesOutputBoundary favouritesOutputBoundary = new FavouritesPresenter(favouritesViewModel);
        final FavouritesInputBoundary favouritesInteractor = new FavouritesInteractor(
                recipeDataAccessObject, favouritesOutputBoundary);

        final FavouritesController favouritesController = new FavouritesController(favouritesInteractor);

        if (viewManager != null) {
            System.out.println("ViewManager exists, wiring favourites components...");
            viewManager.setFavouritesController(favouritesController);
            viewManager.setFavouritesViewModel(favouritesViewModel);
        } else {
            System.out.println("ViewManager is null, will wire in build() method");
        }

        return this;
    }

    /**
     * Creates the ViewManager for the application.
     * @return this builder
     */
    public AppBuilder addViewManager() {
        System.out.println("*** AppBuilder.addViewManager() called ***");
        viewManager = new ViewManager(mealPlannerUsecase);
        return this;
    }

    /**
     * Creates the JFrame for the application.
     * @return the application
     */
    public JFrame build() {
        System.out.println("*** AppBuilder.build() called ***");

        if (favouritesViewModel != null && viewManager != null) {
            System.out.println("Wiring favourites components...");

            final FavouritesOutputBoundary favouritesOutputBoundary = new FavouritesPresenter(favouritesViewModel);
            final FavouritesInputBoundary favouritesInteractor = new FavouritesInteractor(
                    recipeDataAccessObject, favouritesOutputBoundary);
            final FavouritesController favouritesController = new FavouritesController(favouritesInteractor);

            viewManager.setFavouritesController(favouritesController);
            viewManager.setFavouritesViewModel(favouritesViewModel);

            System.out.println("Favourites components wired successfully!");
        } else {
            System.out.println("WARNING: favouritesViewModel or viewManager is null!");
            System.out.println("favouritesViewModel: " + favouritesViewModel);
            System.out.println("viewManager: " + viewManager);
        }

        return viewManager.getFrame();
    }
}
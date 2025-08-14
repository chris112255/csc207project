package app;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * The Main class of our application.
 */
public class Main {
    /**
     * Builds and runs the CA architecture of the application.
     * @param args unused arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final AppBuilder appBuilder = new AppBuilder();
            final JFrame application = appBuilder
                    .addViewManager()
                    .addFavouritesUseCase()
                    .build();
        });
    }
}
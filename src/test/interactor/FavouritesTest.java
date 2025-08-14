package test.interactor;

import entity.Recipe;
import entity.Nutrients;
import org.junit.jupiter.api.*;
import usecase.favourites.FavouritesUsecase;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FavouritesTest {
    private static final String FAVOURITES_FILE = "favourites.txt";
    private static File originalFileBackup;
    private FavouritesUsecase favouritesUsecase;
    private Recipe recipe1, recipe2, recipe3;

    @BeforeAll
    static void backupOriginalFile() throws IOException {
        // Backup original file if it exists
        File original = new File(FAVOURITES_FILE);
        if (original.exists()) {
            originalFileBackup = new File(FAVOURITES_FILE + ".bak");
            copyFile(original, originalFileBackup);
            original.delete();
        }
    }

    @AfterAll
    static void restoreOriginalFile() throws IOException {
        // Restore original file from backup
        if (originalFileBackup != null && originalFileBackup.exists()) {
            File original = new File(FAVOURITES_FILE);

            copyFile(originalFileBackup, original);
            originalFileBackup.delete();
        }
    }

    private static void copyFile(File source, File dest) throws IOException {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

    @BeforeEach
    void setUp() {
        // Clear the file before each test
        new File(FAVOURITES_FILE).delete();
        favouritesUsecase = new FavouritesUsecase();

        Nutrients nutrients = new Nutrients(500, 30, 10, 5, 200, 40);
        recipe1 = createTestRecipe("Pasta Carbonara", "uri1", "http://example.com/pasta", nutrients);
        recipe2 = createTestRecipe("Vegetable Stir Fry", "uri2", "http://example.com/stirfry", nutrients);
        recipe3 = createTestRecipe("Chicken Curry", "uri3", "http://example.com/curry", nutrients);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        new File(FAVOURITES_FILE).delete();
    }

    private Recipe createTestRecipe(String name, String uri, String url, Nutrients nutrients) {
        return new Recipe(
                name,
                "Main Ingredient",
                Arrays.asList("Ingredient1", "Ingredient2"),
                "Instructions",
                2,
                Arrays.asList("DietType"),
                nutrients,
                30.0,
                "CuisineType",
                "MealType",
                "DishType",
                url,
                "http://example.com/image.jpg",
                uri
        );
    }

    @Test
    void testAddAndGetFavourites() {
        favouritesUsecase.addToFavourites(recipe1);
        favouritesUsecase.addToFavourites(recipe2);

        List<Recipe> favourites = favouritesUsecase.getFavourites();
        assertEquals(2, favourites.size());
        assertTrue(favourites.stream().anyMatch(r -> r.getUri().equals("uri1")));
        assertTrue(favourites.stream().anyMatch(r -> r.getUri().equals("uri2")));
    }

    @Test
    void testAddDuplicateFavourite() {
        favouritesUsecase.addToFavourites(recipe1);
        favouritesUsecase.addToFavourites(recipe1); // Duplicate

        assertEquals(1, favouritesUsecase.getFavourites().size());
    }

    @Test
    void testRemoveFromFavourites() {
        favouritesUsecase.addToFavourites(recipe1);
        favouritesUsecase.addToFavourites(recipe2);
        favouritesUsecase.removeFromFavourites(recipe1);

        List<Recipe> favourites = favouritesUsecase.getFavourites();
        assertEquals(1, favourites.size());
        assertEquals("uri2", favourites.get(0).getUri());
    }

    @Test
    void testIsFavourite() {
        assertFalse(favouritesUsecase.isFavourite(recipe1));
        favouritesUsecase.addToFavourites(recipe1);
        assertTrue(favouritesUsecase.isFavourite(recipe1));
    }

    @Test
    void testPersistenceAcrossInstances() {
        favouritesUsecase.addToFavourites(recipe1);
        favouritesUsecase.addToFavourites(recipe2);

        FavouritesUsecase newInstance = new FavouritesUsecase();
        assertEquals(2, newInstance.getFavourites().size());
    }

    @Test
    void testEmptyFileOnNewInstance() {
        FavouritesUsecase newInstance = new FavouritesUsecase();
        assertEquals(0, newInstance.getFavourites().size());
    }
}
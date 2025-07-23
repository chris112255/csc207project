package entity;

public class Recipe {
    private final String title;
    private final String imageUrl;
    private final String sourceUrl;

    public Recipe(String title, String imageUrl, String sourceUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.sourceUrl = sourceUrl;
    }

    // Getters
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getSourceUrl() { return sourceUrl; }
}

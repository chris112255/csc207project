package entity;

public class Recipe {
    private final String dish;
    private final String imageUrl;
    private final String sourceUrl;

    public Recipe(String dish, String imageUrl, String sourceUrl) {
        this.dish = dish;
        this.imageUrl = imageUrl;
        this.sourceUrl = sourceUrl;
    }

    public String getDish() {
        return dish;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }
}

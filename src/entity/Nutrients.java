package entity;

public class Nutrients {

    private final int calories;
    private final double protein;
    private final double fat;
    private final double sugar;
    private final double sodium;
    private final double carbs;

    public Nutrients(int calories, double protein, double fat, double sugar, double sodium) {
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.sugar = sugar;
        this.sodium = sodium;
        this.carbs = 0;
    }

    public int getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getSugar() {
        return sugar;
    }

    public double getSodium() {
        return sodium;
    }

    public double getCarbs() {
        return carbs;
    }
}

package entity;

public class Nutrients {

    private int calories;
    private double protein;
    private double fat;
    private double sugar;
    private double sodium;
    private double carbs;

    private double roundTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public Nutrients(int calories, double protein, double fat, double sugar, double sodium, double carbs) {
        this.calories = calories;
        this.protein = roundTwoDecimals(protein);
        this.fat = roundTwoDecimals(fat);
        this.sugar = roundTwoDecimals(sugar);
        this.sodium = roundTwoDecimals(sodium);
        this.carbs = roundTwoDecimals(carbs);
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

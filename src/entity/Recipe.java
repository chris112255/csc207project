package entity;

import java.util.List;

public class Recipe {

    private final String name;
    private final String mainIngredient;
    private final List<String> ingredients;
    private final String instructions;
    private final int ingredientCount;
    private final List<String> dietType;
    private final Nutrients nutrients;
    private final double prepTime;
    private final String cuisineType;
    private final String mealType;
    private final String dishType;

    public Recipe(String name, String mainIngredient, List<String> ingredients,
                  String instructions, int ingredientCount, List<String> dietType,
                  Nutrients nutrients, double prepTime, String cuisineType,
                  String mealType, String dishType) {
        this.name = name;
        this.mainIngredient = mainIngredient;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.ingredientCount = ingredientCount;
        this.dietType = dietType;
        this.nutrients = nutrients;
        this.prepTime = prepTime;
        this.cuisineType = cuisineType;
        this.mealType = mealType;
        this.dishType = dishType;
    }

    public String getName() {
        return name;
    }

    public String getMainIngredient() {
        return mainIngredient;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getIngredientCount() {
        return ingredientCount;
    }

    public List<String> getDietType() {
        return dietType;
    }

    public Nutrients getNutrients() {
        return nutrients;
    }

    public double getPrepTime() {
        return prepTime;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public String getMealType() {
        return mealType;
    }

    public String getDishType() {
        return dishType;
    }
}

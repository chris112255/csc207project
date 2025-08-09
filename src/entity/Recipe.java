package entity;

import java.util.ArrayList;
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
    private final String sourceUrl;
    private final String imageUrl;
    private final String warnings;

    // === Full Constructor ===
    public Recipe(String name, String mainIngredient, List<String> ingredients,
                  String instructions, int ingredientCount, List<String> dietType,
                  Nutrients nutrients, double prepTime, String cuisineType,
                  String mealType, String dishType, String sourceUrl, String imageUrl) {
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
        this.sourceUrl = sourceUrl;
        this.imageUrl = imageUrl;
        this.warnings = this.calcWarnings();
    }

    // === Constructor used by EdamamRecipeSearchGateway (UPDATED to include imageUrl) ===
    public Recipe(String name, String mainIngredient, List<String> ingredients,
                  String instructions, int ingredientCount, List<String> dietType,
                  Nutrients nutrients, double prepTime, String cuisineType,
                  String mealType, String dishType, String sourceUrl, String imageUrl, boolean fromEdamam) {
        this(name, mainIngredient, ingredients, instructions, ingredientCount, dietType,
                nutrients, prepTime, cuisineType, mealType, dishType, sourceUrl, imageUrl);
    }

    // === Minimal Constructor used by SearchRecipesUseCase ===
    public Recipe(String name, String imageUrl, String sourceUrl) {
        this.name = name;
        this.warnings = this.calcWarnings();
        this.mainIngredient = "";
        this.ingredients = new ArrayList<>();
        this.instructions = "";
        this.ingredientCount = 0;
        this.dietType = new ArrayList<>();
        this.nutrients = null;
        this.prepTime = 0.0;
        this.cuisineType = "";
        this.mealType = "";
        this.dishType = "";
        this.sourceUrl = sourceUrl;
        this.imageUrl = imageUrl;
    }

    private String calcWarnings(){
        String warnings = "High in";
        if(this.getNutriSodium() > 800){
            warnings += " sodium";
        }
        if(this.getNutriCalories() > 800){
            warnings += " calories";
        }
        if(this.getNutriSugar() > 10){
            warnings += " sugar";
        }
        if(this.getNutriFat() > 25){
            warnings += " fat";
        }
        if(this.getNutriCarbs() > 100){
            warnings += " carbs";
        }
        if(warnings.equals("High")){return "None";}
        return warnings;
    }

    // === Getters ===
    public String getWarnings(){
        return this.warnings;
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

    public String getSourceUrl() {
        return sourceUrl;
    }

    public int getNutriCalories() {
        return (nutrients != null) ? nutrients.getCalories() : 0;
    }

    public double getNutriProtein() {
        return (nutrients != null) ? nutrients.getProtein() : 0;
    }

    public double getNutriSugar() {
        return (nutrients != null) ? nutrients.getSugar() : 0;
    }

    public double getNutriFat() {
        return (nutrients != null) ? nutrients.getFat() : 0;
    }

    public double getNutriSodium() {
        return (nutrients != null) ? nutrients.getSodium() : 0;
    }

    public String getImageUrl() {
        return (imageUrl != null) ? imageUrl : "";
    }

    public double getNutriCarbs() {return (nutrients != null) ? nutrients.getCarbs() : 0; }
}

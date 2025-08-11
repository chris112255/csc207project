package usecase;

import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MealPlannerUsecase {
    List<Recipe> selectedMeals = new ArrayList<Recipe>();

    public boolean isSelected(Recipe recipe){
        return selectedMeals.contains(recipe);
    }

    public List<Recipe> getMeals(){
        return selectedMeals;
    }

    public void removeFromPlanner(Recipe recipe) {
        selectedMeals.remove(recipe);
    }

    public void addToPlanner(Recipe recipe) {
        if (!selectedMeals.contains(recipe)) {
            selectedMeals.add(recipe);
        }
        System.out.println(selectedMeals.size());
    }

    public String calculateCalories(int minCaloriesVal, int maxCaloriesVal){
        String result = "Total Calories: " + this.getTotalCalories() + ", ";
        if(getTotalCalories() < minCaloriesVal){
            result += "under by " + (minCaloriesVal - getTotalCalories());
        }
        else if(getTotalCalories() > maxCaloriesVal){
            result += "over by " + (getTotalCalories() - maxCaloriesVal);
        }
        else{
            result += "falls in range";
        }
        return result;
    }

    public String calculateCarbs(int maxCarbsVal){
        String result = "Total Carbs: " + this.getTotalCarbs() + "g, ";
        if(getTotalCarbs() > maxCarbsVal){
            result += "over by " + (getTotalCarbs() - maxCarbsVal) + "g";
        }
        else{
            result += "falls in range";
        }
        return result;
    }

    public String calculateFat(int maxFatVal){
        String result = "Total Fat: " + this.getTotalFat() + "g, ";
        if(getTotalFat() > maxFatVal){
            result += "over by " + (getTotalFat() - maxFatVal) + "g";
        }
        else{
            result += "falls in range";
        }
        return result;
    }

    public String calculateProtein(int minProteinVal){
        String result = "Total Protein: " + this.getTotalProtein() + "g, ";
        if(getTotalProtein() < minProteinVal){
            result += "under by " + (minProteinVal - getTotalProtein()) + "g";
        }
        else{
            result += "falls in range";
        }
        return result;
    }

    public float getTotalCalories() {
        float totalCalories = 0;
        for (Recipe recipe : selectedMeals) {
            totalCalories += recipe.getNutriCalories();
        }
        return totalCalories;
    }

    public float getTotalFat() {
        float totalFat = 0;
        for (Recipe recipe : selectedMeals) {
            totalFat += recipe.getNutriFat();
        }
        return totalFat;
    }

    public float getTotalCarbs() {
        float totalCarbs = 0;
        for (Recipe recipe : selectedMeals) {
            totalCarbs += recipe.getNutriCarbs();
        }
        return totalCarbs;
    }

    public float getTotalProtein() {
        float totalProtein = 0;
        for (Recipe recipe : selectedMeals) {
            totalProtein += recipe.getNutriProtein();
        }
        return totalProtein;
    }
}

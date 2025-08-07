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
        selectedMeals.add(recipe);
        System.out.println(selectedMeals.size());
    }

    public int getTotalCalories() {
        int totalCalories = 0;
        for (Recipe recipe : selectedMeals) {
            totalCalories += recipe.getNutriCalories();
        }
        return totalCalories;
    }

    public int getTotalFat() {
        int totalFat = 0;
        for (Recipe recipe : selectedMeals) {
            totalFat += recipe.getNutriFat();
        }
        return totalFat;
    }

    public int getTotalCarbs() {
        int totalCarbs = 0;
        for (Recipe recipe : selectedMeals) {
            totalCarbs += recipe.getNutriCarbs();
        }
        return totalCarbs;
    }

    public int getTotalProtein() {
        int totalProtein = 0;
        for (Recipe recipe : selectedMeals) {
            totalProtein += recipe.getNutriProtein();
        }
        return totalProtein;
    }
}

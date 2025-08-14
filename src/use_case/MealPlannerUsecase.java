package use_case;

import entity.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MealPlannerUsecase {
    private final List<Recipe> selectedMeals = new ArrayList<>();

    // Saved goals (nullable = not set)
    private Integer minCaloriesGoal;
    private Integer maxCaloriesGoal;
    private Integer maxCarbsGoal;
    private Integer maxFatGoal;
    private Integer minProteinGoal;

    // ---- Selection ----
    public boolean isSelected(Recipe recipe){ return selectedMeals.contains(recipe); }
    public List<Recipe> getMeals(){ return selectedMeals; }
    public void removeFromPlanner(Recipe recipe) { selectedMeals.remove(recipe); }
    public void addToPlanner(Recipe recipe) { if (!selectedMeals.contains(recipe)) selectedMeals.add(recipe); }

    // ---- Goals ----
    public void saveGoals(Integer minCalories, Integer maxCalories,
                          Integer maxCarbs, Integer maxFat, Integer minProtein) {
        this.minCaloriesGoal = minCalories;
        this.maxCaloriesGoal = maxCalories;
        this.maxCarbsGoal = maxCarbs;
        this.maxFatGoal = maxFat;
        this.minProteinGoal = minProtein;
    }

    public Integer getMinCaloriesGoal() { return minCaloriesGoal; }
    public Integer getMaxCaloriesGoal() { return maxCaloriesGoal; }
    public Integer getMaxCarbsGoal()    { return maxCarbsGoal; }
    public Integer getMaxFatGoal()      { return maxFatGoal; }
    public Integer getMinProteinGoal()  { return minProteinGoal; }

    // ---- Aggregates ----
    public float getTotalCalories() { float t=0; for (Recipe r: selectedMeals) t += r.getNutriCalories(); return t; }
    public float getTotalFat()      { float t=0; for (Recipe r: selectedMeals) t += r.getNutriFat();      return t; }
    public float getTotalCarbs()    { float t=0; for (Recipe r: selectedMeals) t += r.getNutriCarbs();    return t; }
    public float getTotalProtein()  { float t=0; for (Recipe r: selectedMeals) t += r.getNutriProtein();  return t; }

    // ---- New: Nullable-goal evaluation helpers (return null if no goal provided) ----
    public String evaluateCalories(Integer minCal, Integer maxCal) {
        if (minCal == null && maxCal == null) return null;
        float total = getTotalCalories();
        String base = "Total Calories: " + Math.round(total);
        if (minCal != null && total < minCal) {
            return base + ", under by " + Math.round(minCal - total);
        }
        if (maxCal != null && total > maxCal) {
            return base + ", over by " + Math.round(total - maxCal);
        }
        return base + ", within goal";
    }

    public String evaluateCarbs(Integer maxCarbs) {
        if (maxCarbs == null) return null;
        float total = getTotalCarbs();
        String base = "Total Carbs: " + Math.round(total) + "g";
        return (total > maxCarbs)
                ? base + ", over by " + Math.round(total - maxCarbs) + "g"
                : base + ", within goal";
    }

    public String evaluateFat(Integer maxFat) {
        if (maxFat == null) return null;
        float total = getTotalFat();
        String base = "Total Fat: " + Math.round(total) + "g";
        return (total > maxFat)
                ? base + ", over by " + Math.round(total - maxFat) + "g"
                : base + ", within goal";
    }

    public String evaluateProtein(Integer minProtein) {
        if (minProtein == null) return null;
        float total = getTotalProtein();
        String base = "Total Protein: " + Math.round(total) + "g";
        return (total < minProtein)
                ? base + ", under by " + Math.round(minProtein - total) + "g"
                : base + ", within goal";
    }

    // ---- (Old text helpers kept for compatibility; unused by new UI) ----
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
}

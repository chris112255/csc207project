package use_case.sort;

import entity.Recipe;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CaloriesAscendingSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Recipe> recipes) {
        Collections.sort(recipes, Comparator.comparing(Recipe::getNutriCalories));
    }
}
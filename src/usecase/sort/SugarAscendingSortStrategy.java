package usecase.sort;

import entity.Recipe;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SugarAscendingSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Recipe> recipes) {
        Collections.sort(recipes, Comparator.comparing(Recipe::getNutriSugar));
    }

    @Override
    public void setTestCriteria(Object testCriteria) {
    }
}
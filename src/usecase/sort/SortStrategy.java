package usecase.sort;

import entity.Recipe;
import java.util.List;

public interface SortStrategy {
    void sort(List<Recipe> recipes);
}
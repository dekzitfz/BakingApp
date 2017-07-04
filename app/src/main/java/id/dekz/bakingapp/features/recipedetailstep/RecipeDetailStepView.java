package id.dekz.bakingapp.features.recipedetailstep;

import id.dekz.bakingapp.basemvp.BaseView;
import id.dekz.bakingapp.model.Step;

/**
 * Created by DEKZ on 7/4/2017.
 */

public interface RecipeDetailStepView extends BaseView {
    void bindData(Step step);
}

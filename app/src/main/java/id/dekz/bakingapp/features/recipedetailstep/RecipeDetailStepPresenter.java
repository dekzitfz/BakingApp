package id.dekz.bakingapp.features.recipedetailstep;

import id.dekz.bakingapp.basemvp.BasePresenter;

/**
 * Created by DEKZ on 7/4/2017.
 */

public class RecipeDetailStepPresenter implements BasePresenter<RecipeDetailStepView> {

    private RecipeDetailStepView view;

    @Override
    public void onAttach(RecipeDetailStepView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        view = null;
    }
}

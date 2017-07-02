package id.dekz.bakingapp.features.recipedetail;

import id.dekz.bakingapp.basemvp.BasePresenter;

/**
 * Created by DEKZ on 7/2/2017.
 */

public class RecipeDetailPresenter implements BasePresenter<RecipeDetailView> {

    private RecipeDetailView view;

    @Override
    public void onAttach(RecipeDetailView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        view = null;
    }
}

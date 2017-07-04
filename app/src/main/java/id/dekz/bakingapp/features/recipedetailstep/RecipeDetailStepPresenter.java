package id.dekz.bakingapp.features.recipedetailstep;

import com.google.gson.Gson;

import id.dekz.bakingapp.basemvp.BasePresenter;
import id.dekz.bakingapp.model.Step;

/**
 * Created by DEKZ on 7/4/2017.
 */

public class RecipeDetailStepPresenter implements BasePresenter<RecipeDetailStepView> {

    private RecipeDetailStepView view;
    private Gson gson = new Gson();

    @Override
    public void onAttach(RecipeDetailStepView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        gson = null;
        view = null;
    }

    void getStepModel(String json){
        view.bindData(gson.fromJson(json, Step.class));
    }
}

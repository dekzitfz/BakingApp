package id.dekz.bakingapp.features.recipedetail;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import id.dekz.bakingapp.R;
import id.dekz.bakingapp.basemvp.BasePresenter;
import id.dekz.bakingapp.features.recipedetailstep.RecipeDetailStepFragment;
import id.dekz.bakingapp.features.recipestep.RecipeStepFragment;
import id.dekz.bakingapp.model.Ingredient;
import id.dekz.bakingapp.model.Recipe;
import id.dekz.bakingapp.model.Step;

/**
 * Created by DEKZ on 7/2/2017.
 */

public class RecipeDetailPresenter implements BasePresenter<RecipeDetailView> {

    private RecipeDetailView view;
    private Gson gson;

    @Override
    public void onAttach(RecipeDetailView BaseView) {
        view = BaseView;
        gson = new Gson();
    }

    @Override
    public void onDetach() {
        gson = null;
        view = null;
    }

    void replaceFragment(Fragment fragment){
        view.getFragmentManagerFromActivity().beginTransaction()
                .replace(R.id.container, fragment, "removable")
                .commit();
    }

    void addFragment(Fragment fragment){
        view.getFragmentManagerFromActivity().beginTransaction()
                .add(view.getContainerID(), fragment)
                .commit();
    }

    void addFragments(Fragment left, Fragment right){
        view.getFragmentManagerFromActivity().beginTransaction()
                .add(R.id.container_left, left)
                .add(R.id.container_right, right)
                .commit();
    }

    void changeFragmentRight(Fragment fragment){
        view.getFragmentManagerFromActivity().beginTransaction()
                .replace(R.id.container_right, fragment)
                .commit();
    }

    Fragment getStepDetailFragment(@Nullable String json, int currentStep, int totalStep){
        return RecipeDetailStepFragment.newInstance(json, currentStep, totalStep);
    }

    Fragment getStepFragment(String json, boolean isTwoPane){
        return RecipeStepFragment.newInstance(json, isTwoPane);
    }

    void getRecipeModel(String json){
        view.bindData(gson.fromJson(json, Recipe.class));
    }

    List<Step> getListSteps(String json){
        List<Step> result = new ArrayList<>();
        Recipe recipe = gson.fromJson(json, Recipe.class);
        if(recipe != null){
            for(Step s : recipe.getSteps()){
                result.add(s);
            }
        }

        return result;
    }

    String getStepJsonByIndex(String jsonRecipe, int indexID){
        Recipe recipe = gson.fromJson(jsonRecipe, Recipe.class);
        return gson.toJson(recipe.getSteps().get(indexID));
    }
}

package id.dekz.bakingapp.features.recipestep;

import android.support.v4.app.Fragment;

import com.google.gson.Gson;

import java.util.List;

import id.dekz.bakingapp.R;
import id.dekz.bakingapp.basemvp.BasePresenter;
import id.dekz.bakingapp.features.recipedetailstep.RecipeDetailStepFragment;
import id.dekz.bakingapp.model.Ingredient;
import id.dekz.bakingapp.model.Recipe;
import id.dekz.bakingapp.model.Step;

/**
 * Created by DEKZ on 7/4/2017.
 */

public class RecipeStepPresenter implements BasePresenter<RecipeStepView> {

    private RecipeStepView view;
    private Gson gson = new Gson();

    @Override
    public void onAttach(RecipeStepView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        gson = null;
        view = null;
    }

    void getRecipeModel(String json){
        view.bindData(gson.fromJson(json, Recipe.class));
    }

    String getJsonStep(Step step){
        return gson.toJson(step);
    }

    public String getEachIngredient(List<Ingredient> data){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i=0; i<data.size(); i++){
            String name = data.get(i).getIngredient();
            String measure = data.get(i).getMeasure();
            String qty = String.valueOf(data.get(i).getQuantity());

            String strToAppend = "- "+name+"("+qty+" "+measure+")";

            if(i == data.size()-1){
                stringBuilder.append(strToAppend);
            }else{
                stringBuilder.append(strToAppend).append("\n");
            }
        }

        return stringBuilder.toString();
    }

    void addFragment(Fragment fragment){
        view.getFragmentManagerFromFragment().beginTransaction()
                .replace(R.id.container, fragment, RecipeDetailStepFragment.class.getSimpleName())
                .addToBackStack(RecipeDetailStepFragment.class.getSimpleName())
                .commit();
        view.getFragmentManagerFromFragment().executePendingTransactions();
    }

    Fragment getDetailStepFragment(String json,
                                   int currentStep,
                                   int totalStep,
                                   int previousStep,
                                   int nextStep){
        return RecipeDetailStepFragment.newInstance(json, currentStep, totalStep, previousStep, nextStep);
    }
}

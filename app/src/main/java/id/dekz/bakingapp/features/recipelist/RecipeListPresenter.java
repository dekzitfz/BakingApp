package id.dekz.bakingapp.features.recipelist;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import id.dekz.bakingapp.App;
import id.dekz.bakingapp.basemvp.BasePresenter;
import static id.dekz.bakingapp.database.contract.RecipeContract.RecipeEntry;
import id.dekz.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DEKZ on 6/27/2017.
 */

public class RecipeListPresenter implements BasePresenter<RecipeListView> {

    private RecipeListView view;
    private static final String TAG = RecipeListPresenter.class.getSimpleName();

    private Call<List<Recipe>> recipeCall;

    @Override
    public void onAttach(RecipeListView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        if(recipeCall != null) recipeCall.cancel();
        view = null;
    }

    void loadData(){
        recipeCall = App.getRestClient()
                .getService()
                .getRecipes();

        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        view.onDataReceived(response.body());
                        for(Recipe r : response.body()){
                            saveData(r);
                        }
                    }else{
                        Log.w(TAG, "response body is null!");
                    }
                }else{
                    Log.w(TAG, "response code: "+response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    void saveData(Recipe recipe){
        ContentValues values = new ContentValues();
        values.put(RecipeEntry.RECIPE_ID, recipe.getId());
        values.put(RecipeEntry.RECIPE_NAME, recipe.getName());
        values.put(RecipeEntry.RECIPE_IMAGE, recipe.getImage());
        values.put(RecipeEntry.RECIPE_SERVINGS, recipe.getServings());

        view.getResolver().insert(RecipeEntry.CONTENT_URI, values);
    }
}

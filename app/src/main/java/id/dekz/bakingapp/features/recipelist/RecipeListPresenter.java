package id.dekz.bakingapp.features.recipelist;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import id.dekz.bakingapp.App;
import id.dekz.bakingapp.basemvp.BasePresenter;
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
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        //noinspection ConstantConditions
                        view.onDataReceived(response.body());
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
}

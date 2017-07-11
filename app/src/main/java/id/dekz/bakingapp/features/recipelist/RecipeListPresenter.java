package id.dekz.bakingapp.features.recipelist;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.dekz.bakingapp.App;
import id.dekz.bakingapp.basemvp.BasePresenter;
import static id.dekz.bakingapp.database.contract.RecipeContract.RecipeEntry;

import static id.dekz.bakingapp.database.contract.IngredientContract.IngredientEntry;

import static id.dekz.bakingapp.database.contract.StepContract.StepEntry;
import id.dekz.bakingapp.model.Ingredient;
import id.dekz.bakingapp.model.Recipe;
import id.dekz.bakingapp.model.Step;
import id.dekz.bakingapp.util.Constant;
import id.dekz.bakingapp.util.RecipeLoader;
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
    private ArrayList<ContentProviderOperation> operations = new ArrayList<>();
    private LoaderManager.LoaderCallbacks<List<Recipe>> loaderCallbacks;
    private static final int LOADER_ID = 321;

    @Override
    public void onAttach(RecipeListView BaseView) {
        view = BaseView;
        setupLoader();
    }

    @Override
    public void onDetach() {
        if(recipeCall != null) recipeCall.cancel();
        view.getLoaderManagerFromActivity().destroyLoader(LOADER_ID);
        view = null;
    }

    void loadData(){
        view.onDataLoading();
        recipeCall = App.getRestClient()
                .getService()
                .getRecipes();

        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull final Response<List<Recipe>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        view.onDataReceived(response.body());

                        clearPreviousData();
                        for(Recipe r : response.body()){
                            saveRecipeData(r);
                        }
                        insertAllCV();
                    }else{
                        Log.w(TAG, "response body is null!");
                        initLoader();
                    }
                }else{
                    Log.w(TAG, "response code: "+response.code());
                    initLoader();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                t.printStackTrace();
                initLoader();
            }
        });
    }

    private void saveRecipeData(Recipe recipe){
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(RecipeEntry.RECIPE_ID, recipe.getId());
        recipeValues.put(RecipeEntry.RECIPE_NAME, recipe.getName());
        recipeValues.put(RecipeEntry.RECIPE_IMAGE, recipe.getImage());
        recipeValues.put(RecipeEntry.RECIPE_SERVINGS, recipe.getServings());

        operations.add(
                ContentProviderOperation.newInsert(RecipeEntry.CONTENT_URI)
                        .withValues(recipeValues)
                        .build()
        );

        for(Ingredient i : recipe.getIngredients()){
            saveIngredientData(i, recipe.getId());
        }

        for(Step s : recipe.getSteps()){
            saveStepData(s, recipe.getId());
        }
    }

    private void insertAllCV(){
        try {
            view.getResolver().applyBatch(Constant.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    private void clearPreviousData(){
        view.getResolver().delete(RecipeEntry.CONTENT_URI, null, null);
        view.getResolver().delete(IngredientEntry.CONTENT_URI, null, null);
        view.getResolver().delete(StepEntry.CONTENT_URI, null, null);
    }

    private void saveIngredientData(Ingredient ingredient, int recipeID){
        ContentValues ingredientValues = new ContentValues();
        ingredientValues.put(IngredientEntry.RECIPE_ID, recipeID);
        ingredientValues.put(IngredientEntry.INGREDIENT_QUANTITY, ingredient.getQuantity());
        ingredientValues.put(IngredientEntry.INGREDIENT_MEASURE, ingredient.getMeasure());
        ingredientValues.put(IngredientEntry.INGREDIENT, ingredient.getIngredient());

        operations.add(
                ContentProviderOperation.newInsert(IngredientEntry.CONTENT_URI)
                        .withValues(ingredientValues)
                        .build()
        );
    }

    private void saveStepData(Step step, int recipeID){
        ContentValues stepValues = new ContentValues();
        stepValues.put(StepEntry.RECIPE_ID, recipeID);
        stepValues.put(StepEntry.STEP_ID, step.getId());
        stepValues.put(StepEntry.STEP_DESCRIPTION, step.getDescription());
        stepValues.put(StepEntry.STEP_SHORT_DESC, step.getShortDescription());
        stepValues.put(StepEntry.STEP_THUMBNAIL_URL, step.getThumbnailURL());
        stepValues.put(StepEntry.STEP_VIDEO_URL, step.getVideoURL());

        operations.add(
                ContentProviderOperation.newInsert(StepEntry.CONTENT_URI)
                        .withValues(stepValues)
                        .build()
        );
    }

    private void setupLoader(){
        loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Recipe>>() {
            @Override
            public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
                return new RecipeLoader(view.getContext(), view.getResolver());
            }

            @Override
            public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
                if(data != null && data.size() > 0){
                    /*for(int i=0; i<data.size(); i++){
                        Log.i(TAG, data.get(i).getName()
                                + " with "+data.get(i).getIngredients().size()+" ingredients and "
                                + data.get(i).getSteps().size()+" steps");
                    }*/
                    view.onDataReceived(data);
                }else{
                    view.onWarningMessageReceived("Could Not Load Data");
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Recipe>> loader) {

            }
        };
    }

    void initLoader(){
        view.getLoaderManagerFromActivity().initLoader(LOADER_ID, null, loaderCallbacks);
    }

    void restartLoader(){
        view.getLoaderManagerFromActivity().restartLoader(LOADER_ID, null, loaderCallbacks);
    }

}

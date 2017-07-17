package id.dekz.bakingapp.features.widget;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import id.dekz.bakingapp.basemvp.BasePresenter;
import id.dekz.bakingapp.model.Recipe;
import id.dekz.bakingapp.util.Constant;
import id.dekz.bakingapp.util.RecipeLoader;

/**
 * Created by DEKZ on 7/15/2017.
 */

public class ConfigWisgetPresenter implements BasePresenter<ConfigWidgetView> {

    private static final String TAG = ConfigWisgetPresenter.class.getSimpleName();

    private ConfigWidgetView view;
    private LoaderManager.LoaderCallbacks<List<Recipe>> loaderCallbacks;
    private static final int LOADER_ID = 322;
    private Gson gson = new Gson();

    @Override
    public void onAttach(ConfigWidgetView BaseView) {
        view = BaseView;
        setupLoader();
        initLoader();
    }

    @Override
    public void onDetach() {
        view.getLoaderFromAct().destroyLoader(LOADER_ID);
        view = null;
    }

    private void setupLoader(){
        loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Recipe>>() {
            @Override
            public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
                return new RecipeLoader(view.getContextFromAct(), view.getContentResolverFromAct());
            }

            @Override
            public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
                if(data != null && data.size() > 0){
                    view.onBind(data);
                }else{
                    //data null
                    Log.d(TAG, "data is empty");
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Recipe>> loader) {

            }
        };
    }

    @SuppressLint("ApplySharedPref")
    void saveData(int widgetID, Recipe recipe){
        view.getPrefs().edit()
                .putInt(Constant.WIDGET_SELECTED_RECIPE_ID, recipe.getId())
                .commit();

        view.getPrefs().edit()
                .putString(Constant.WIDGET_SELECTED_RECIPE_NAME, recipe.getName())
                .commit();

        view.getPrefs().edit()
                .putString(Constant.KEY_RECIPE, gson.toJson(recipe))
                .commit();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(view.getContextFromAct());
        IngredientsWidget.updateAppWidget(view.getContextFromAct(),
                appWidgetManager,
                recipe.getId(),
                recipe.getName(),
                gson.toJson(recipe),
                widgetID);

        view.onComplete();
    }

    private void initLoader(){
        if(view.getLoaderFromAct().getLoader(LOADER_ID) != null){
            view.getLoaderFromAct().restartLoader(LOADER_ID, null, loaderCallbacks);
        }else{
            view.getLoaderFromAct().initLoader(LOADER_ID, null, loaderCallbacks);
        }
    }
}

package id.dekz.bakingapp.features.recipelist;

import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.app.LoaderManager;

import java.util.List;

import id.dekz.bakingapp.basemvp.BaseView;
import id.dekz.bakingapp.model.Recipe;

/**
 * Created by DEKZ on 6/27/2017.
 */

public interface RecipeListView extends BaseView {
    void onDataReceived(List<Recipe> data);
    void onWarningMessageReceived(String message);
    void onDataLoading();
    ContentResolver getResolver();
    Context getContext();
    LoaderManager getLoaderManagerFromActivity();
}

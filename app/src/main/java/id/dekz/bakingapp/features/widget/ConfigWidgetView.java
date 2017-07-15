package id.dekz.bakingapp.features.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;

import java.util.List;

import id.dekz.bakingapp.basemvp.BaseView;
import id.dekz.bakingapp.model.Recipe;

/**
 * Created by DEKZ on 7/15/2017.
 */

public interface ConfigWidgetView extends BaseView {
    void onBind(List<Recipe> data);
    void onComplete();
    Context getContextFromAct();
    ContentResolver getContentResolverFromAct();
    LoaderManager getLoaderFromAct();
    SharedPreferences getPrefs();
}

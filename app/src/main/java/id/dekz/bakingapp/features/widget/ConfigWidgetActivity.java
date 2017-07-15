package id.dekz.bakingapp.features.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.adapter.RecipeAdapter;
import id.dekz.bakingapp.model.Recipe;
import id.dekz.bakingapp.util.RecipeLoader;

/**
 * Created by DEKZ on 7/14/2017.
 */

public class ConfigWidgetActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickFromWidgetListener {

    private static final String TAG = ConfigWidgetActivity.class.getSimpleName();

    @BindView(R.id.rv_config_widget)RecyclerView rv;

    private RecipeAdapter adapter;
    private LoaderManager.LoaderCallbacks<List<Recipe>> loaderCallbacks;
    private static final int LOADER_ID = 322;
    private int widgetID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_widget);
        ButterKnife.bind(this);

        widgetID = getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d(TAG, "widgetID-> "+widgetID);

        adapter = new RecipeAdapter();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        setupLoader();
        initLoader();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getSupportLoaderManager().destroyLoader(LOADER_ID);
    }

    private void setupLoader(){
        loaderCallbacks = new LoaderManager.LoaderCallbacks<List<Recipe>>() {
            @Override
            public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
                return new RecipeLoader(ConfigWidgetActivity.this, getContentResolver());
            }

            @Override
            public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
                if(data != null && data.size() > 0){
                    adapter.replaceAll(data);
                    adapter.setClickFromWidgetListener(ConfigWidgetActivity.this);
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

    private void initLoader(){
        getSupportLoaderManager().initLoader(LOADER_ID, null, loaderCallbacks);
    }

    @Override
    public void onClickFromWidget(Recipe recipe) {
        Log.d(TAG, recipe.getName()+" selected");
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putInt("WIDGET_SELECTED_RECIPE_ID", recipe.getId())
                .apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        IngredientsWidget.updateAppWidget(this, appWidgetManager, recipe.getId(), recipe.getName(), widgetID);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

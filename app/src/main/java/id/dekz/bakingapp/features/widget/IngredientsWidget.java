package id.dekz.bakingapp.features.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.database.contract.IngredientContract;
import id.dekz.bakingapp.features.recipedetail.RecipeDetailActivity;
import id.dekz.bakingapp.features.recipelist.RecipeListActivity;
import id.dekz.bakingapp.model.Ingredient;
import id.dekz.bakingapp.util.Constant;

import static id.dekz.bakingapp.database.contract.IngredientContract.IngredientEntry.CONTENT_URI;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    private static final String TAG = IngredientsWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int recipeID, String recipeName, String jsonRecipe, int appWidgetId) {

        Log.d(TAG, "updateAppWidget");
        Log.d(TAG, "recipeName--> "+recipeName);
        String ingredient = getEachIngredient(getIngredients(context,recipeID));
        //Log.d(TAG, ingredient);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.tv_widget_recipe_name, recipeName);
        views.setTextViewText(R.id.tv_widget_ingredients, ingredient);

        Intent recipeDetail = new Intent(context, RecipeDetailActivity.class);
        recipeDetail.putExtra(Intent.EXTRA_TEXT, jsonRecipe);
        recipeDetail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, recipeDetail, PendingIntent.FLAG_CANCEL_CURRENT);
        if(jsonRecipe == null || jsonRecipe.equals("") || jsonRecipe.length()==0){
            Log.w(TAG, "json is empty!");
        }else{
            Log.w(TAG, "jsonwidget---> "+jsonRecipe);
            views.setOnClickPendingIntent(R.id.rootWidget, pendingIntent);
        }


        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    //called for 1st time widget create and interval update
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");
        int recipeID = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(Constant.WIDGET_SELECTED_RECIPE_ID, 1);

        String recipeName = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Constant.WIDGET_SELECTED_RECIPE_NAME, "");

        String jsonRecipe = PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Constant.KEY_RECIPE, "");

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipeID, recipeName, jsonRecipe, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled");
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled");
        // Enter relevant functionality for when the last widget is disabled
    }

    public static String getEachIngredient(List<Ingredient> data){
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

    private static List<Ingredient> getIngredients(Context context, int id){
        List<Ingredient> result = new ArrayList<>();

        Cursor cursor =  context.getContentResolver().query(
                ContentUris.withAppendedId(CONTENT_URI, id),
                null,
                null,
                null,
                null
        );

        if(cursor != null){
            if(cursor.getCount() > 0){
                if(cursor.moveToFirst()){
                    do {
                        Ingredient ingredient = new Ingredient();
                        ingredient.setQuantity(cursor.getDouble(
                                cursor.getColumnIndex(IngredientContract.IngredientEntry.INGREDIENT_QUANTITY)
                        ));
                        ingredient.setMeasure(cursor.getString(
                                cursor.getColumnIndex(IngredientContract.IngredientEntry.INGREDIENT_MEASURE)
                        ));
                        ingredient.setIngredient(cursor.getString(
                                cursor.getColumnIndex(IngredientContract.IngredientEntry.INGREDIENT)
                        ));

                        result.add(ingredient);
                    } while (cursor.moveToNext());
                }
            }else{
                Log.w(TAG, "cursor size is "+cursor.getCount());
            }

            cursor.close();
        }else{
            Log.w(TAG, "cursor is null");
        }

        return result;
    }
}


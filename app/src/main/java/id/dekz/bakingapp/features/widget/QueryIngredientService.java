package id.dekz.bakingapp.features.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.dekz.bakingapp.database.contract.IngredientContract;
import id.dekz.bakingapp.model.Ingredient;

import static id.dekz.bakingapp.database.contract.IngredientContract.BASE_CONTENT_URI;
import static id.dekz.bakingapp.database.contract.IngredientContract.IngredientEntry.CONTENT_URI;
import static id.dekz.bakingapp.database.contract.IngredientContract.PATH_INGREDIENTS;

/**
 * Created by DEKZ on 7/13/2017.
 */

public class QueryIngredientService extends IntentService {

    private static final String TAG = QueryIngredientService.class.getSimpleName();
    public static final String ACTION_QUERY_INGREDIENTS = "id.dekz.bakingapp.action.query_ingredients";
    public static final String ACTION_UPDATE_WIDGET = "id.dekz.bakingapp.action.update_widget";

    public QueryIngredientService() {
        super("QueryIngredientService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_QUERY_INGREDIENTS.equals(action)) {
                handleQueryIngredient(intent.getIntExtra("id", 1));
            }else if(action.equals(ACTION_UPDATE_WIDGET)){
                handleUpdateWidget();
            }
        }
    }

    public static void startQueryIngredientService(Context context){
        Log.d(TAG, "startQueryIngredientService");
        Intent intent = new Intent(context, QueryIngredientService.class);
        intent.setAction(ACTION_QUERY_INGREDIENTS);
        context.startService(intent);
    }

    public static void startWidgetUpdateService(Context context){
        Log.d(TAG, "startWidgetUpdateService");
        Intent intent = new Intent(context, QueryIngredientService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }

    private void handleUpdateWidget(){

    }

    private void handleQueryIngredient(int id){
        Log.d(TAG, "handleQueryIngredient, "+id);
        String ingredient = getEachIngredient(getIngredients(id));

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
        //update all widgets
        //IngredientsWidget.updateIngredientWidget(this, appWidgetManager, imgRes, appWidgetIds);
        //IngredientsWidget.updateAppWidget(this, appWidgetManager, recipe.getId(), widgetID);
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

    private List<Ingredient> getIngredients(int id){
        List<Ingredient> result = new ArrayList<>();

        Cursor cursor = getContentResolver().query(
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

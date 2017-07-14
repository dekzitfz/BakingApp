package id.dekz.bakingapp.features.widget;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

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
                handleQueryIngredient(3);
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

    private void handleQueryIngredient(long id){
        Log.d(TAG, "handleQueryIngredient");

        Cursor cursor = getContentResolver().query(
                ContentUris.withAppendedId(CONTENT_URI, id),
                null,
                null,
                null,
                null
        );

        if(cursor!=null){
            Log.d(TAG, ""+cursor.getCount());
            cursor.close();
        }
    }
}

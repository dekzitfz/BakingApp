package id.dekz.bakingapp.features.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;

import id.dekz.bakingapp.R;
import id.dekz.bakingapp.features.recipelist.RecipeListActivity;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    private static final String TAG = IngredientsWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                /*String ingredients,*/ int appWidgetId) {
        Log.d(TAG, "updateAppWidget");

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

        Intent recipeList = new Intent(context, RecipeListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, recipeList, 0);

        Intent queryService = new Intent(context, QueryIngredientService.class);
        queryService.setAction(QueryIngredientService.ACTION_QUERY_INGREDIENTS);
        PendingIntent queryPendingIntent = PendingIntent.getService(
                context,
                0,
                queryService,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        views.setOnClickPendingIntent(R.id.appwidget_text, queryPendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateIngredientWidget(Context context, AppWidgetManager appWidgetManager,
                                              String ingredients, int[] appWidgetIds){

    }

    //called for 1st time widget create and interval update
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate");
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        //QueryIngredientService.startWidgetUpdateService(context);
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
}


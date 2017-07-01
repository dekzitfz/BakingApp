package id.dekz.bakingapp.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static  id.dekz.bakingapp.database.contract.RecipeContract.RecipeEntry;
import id.dekz.bakingapp.model.Recipe;

/**
 * Created by DEKZ on 7/1/2017.
 */

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private static final String TAG = RecipeLoader.class.getSimpleName();
    private ContentResolver resolver;

    public RecipeLoader(Context context, ContentResolver resolver) {
        super(context);
        this.resolver = resolver;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {
        List<Recipe> recipes = new ArrayList<>();

        Cursor recipeCursor = resolver.query(
                RecipeEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        if(recipeCursor != null){
            if(recipeCursor.getCount() > 0){
                if(recipeCursor.moveToFirst()){
                    do {
                        Recipe recipe = new Recipe();
                        recipe.setId(recipeCursor.getInt(
                                recipeCursor.getColumnIndex(RecipeEntry.RECIPE_ID)
                        ));
                        recipe.setImage(recipeCursor.getString(
                                recipeCursor.getColumnIndex(RecipeEntry.RECIPE_IMAGE)
                        ));
                        recipe.setName(recipeCursor.getString(
                                recipeCursor.getColumnIndex(RecipeEntry.RECIPE_NAME)
                        ));
                        recipe.setServings(recipeCursor.getInt(
                                recipeCursor.getColumnIndex(RecipeEntry.RECIPE_SERVINGS)
                        ));

                        recipes.add(recipe);
                    } while (recipeCursor.moveToNext());
                }
            }else{
                Log.w(TAG, "cursor size is "+recipeCursor.getCount());
            }

            recipeCursor.close();
        }else{
            Log.w(TAG, "cursor is null");
        }

        return recipes;
    }
}

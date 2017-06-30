package id.dekz.bakingapp.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import static id.dekz.bakingapp.database.contract.IngredientContract.PATH_INGREDIENTS;
import static id.dekz.bakingapp.database.contract.RecipeContract.PATH_RECIPES;
import static id.dekz.bakingapp.database.contract.StepContract.PATH_STEPS;
import static id.dekz.bakingapp.util.Constant.AUTHORITY;

/**
 * Created by DEKZ on 6/30/2017.
 */

public class BakingContentProvider extends ContentProvider {

    private static final String TAG = BakingContentProvider.class.getSimpleName();

    private static final int RECIPES = 100;
    private static final int RECIPE_WITH_ID = 101;
    private static final int INGREDIENTS_WITH_RECIPE_ID = 201;
    private static final int STEPS_WITH_RECIPE_ID = 301;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private DBHelper dbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, PATH_RECIPES, RECIPES);
        matcher.addURI(AUTHORITY, PATH_RECIPES + "/#", RECIPE_WITH_ID);
        matcher.addURI(AUTHORITY, PATH_INGREDIENTS + "/#", INGREDIENTS_WITH_RECIPE_ID);
        matcher.addURI(AUTHORITY, PATH_STEPS + "/#", STEPS_WITH_RECIPE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

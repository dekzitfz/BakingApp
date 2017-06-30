package id.dekz.bakingapp.database;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import java.util.ArrayList;
import java.util.Set;

import static id.dekz.bakingapp.database.contract.StepContract.StepEntry;

import static id.dekz.bakingapp.database.contract.IngredientContract.IngredientEntry;

import static id.dekz.bakingapp.database.contract.RecipeContract.RecipeEntry;

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
    private static final int INGREDIENTS = 200;
    private static final int INGREDIENTS_WITH_RECIPE_ID = 201;
    private static final int STEPS = 300;
    private static final int STEPS_WITH_RECIPE_ID = 301;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private DBHelper dbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, PATH_RECIPES, RECIPES);
        matcher.addURI(AUTHORITY, PATH_RECIPES + "/#", RECIPE_WITH_ID);
        matcher.addURI(AUTHORITY, PATH_INGREDIENTS, INGREDIENTS);
        matcher.addURI(AUTHORITY, PATH_INGREDIENTS + "/#", INGREDIENTS_WITH_RECIPE_ID);
        matcher.addURI(AUTHORITY, PATH_STEPS, STEPS);
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

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri result = null;
        long id;

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        switch (match){
            case RECIPES:
                id = db.insert(RecipeEntry.TABLE_NAME, null, values);
                if(id > 0){
                    result = ContentUris.withAppendedId(uri, id);
                    getContext().getContentResolver().notifyChange(uri, null);
                }else{
                    Log.w(TAG, "inserting recipe data was failed!");
                }
                break;
            case INGREDIENTS:
                id = db.insert(IngredientEntry.TABLE_NAME, null, values);
                if(id > 0){
                    result = ContentUris.withAppendedId(uri, id);
                    getContext().getContentResolver().notifyChange(uri, null);
                }else{
                    Log.w(TAG, "inserting ingredient data was failed!");
                }
                break;
            case STEPS:
                id = db.insert(StepEntry.TABLE_NAME, null, values);
                if(id > 0){
                    result = ContentUris.withAppendedId(uri, id);
                    getContext().getContentResolver().notifyChange(uri, null);
                }else{
                    Log.w(TAG, "inserting step data was failed!");
                }
                break;
            default:
                Log.w(TAG, "unknown URI: "+uri);
        }

        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int numInserted = 0;
        String table = null;

        int match = uriMatcher.match(uri);
        switch (match) {
            case RECIPES:
                table = RecipeEntry.TABLE_NAME;
                break;
            case INGREDIENTS:
                table = RecipeEntry.TABLE_NAME;
                break;
            case STEPS:
                table = RecipeEntry.TABLE_NAME;
                break;
            default:
                Log.w(TAG, "uri not match: "+uri);
                break;
        }
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ContentValues cv : values) {
                if(table != null){
                    long newID = db.insert(table, null, cv);
                    if (newID <= 0) {
                        Log.w(TAG, "Failed to insert row into " + uri);
                    }
                }
            }
            db.setTransactionSuccessful();
            getContext().getContentResolver().notifyChange(uri, null);
            numInserted = values.length;
        } finally {
            db.endTransaction();
        }
        return numInserted;
    }

    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        ContentProviderResult[] result = new ContentProviderResult[operations
                .size()];
        // Opens the database object in "write" mode.
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        // Begin a transaction
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }

            db.setTransactionSuccessful();
        } catch (OperationApplicationException e) {
            Log.w(TAG, "batch failed: " + e.getLocalizedMessage());
        } finally {
            db.endTransaction();
        }

        return result;
    }
}

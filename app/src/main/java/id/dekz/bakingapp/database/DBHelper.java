package id.dekz.bakingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static id.dekz.bakingapp.database.contract.RecipeContract.RecipeEntry;
import static id.dekz.bakingapp.database.contract.IngredientContract.IngredientEntry;
import static id.dekz.bakingapp.database.contract.StepContract.StepEntry;

/**
 * Created by DEKZ on 6/29/2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "baking.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_RECIPE =
            "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +
            RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            RecipeEntry.RECIPE_ID + " INTEGER NOT NULL, " +
            RecipeEntry.RECIPE_NAME + " TEXT, " +
            RecipeEntry.RECIPE_SERVINGS + " INTEGER, " +
            RecipeEntry.RECIPE_IMAGE + " TEXT " +
            "); ";

    private static final String CREATE_TABLE_INGREDIENT =
            "CREATE TABLE " + IngredientEntry.TABLE_NAME + " (" +
                    IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    IngredientEntry.RECIPE_ID + " INTEGER NOT NULL, " +
                    IngredientEntry.INGREDIENT_QUANTITY + " REAL, " +
                    IngredientEntry.INGREDIENT_MEASURE + " TEXT, " +
                    IngredientEntry.INGREDIENT + " TEXT " +
                    "); ";

    private static final String CREATE_TABLE_STEP =
            "CREATE TABLE " + StepEntry.TABLE_NAME + " (" +
                    StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    StepEntry.RECIPE_ID + " INTEGER NOT NULL, " +
                    StepEntry.STEP_ID + " INTEGER, " +
                    StepEntry.STEP_SHORT_DESC + " TEXT, " +
                    StepEntry.STEP_DESCRIPTION + " TEXT, " +
                    StepEntry.STEP_VIDEO_URL + " TEXT, " +
                    StepEntry.STEP_THUMBNAIL_URL + " TEXT " +
                    "); ";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECIPE);
        db.execSQL(CREATE_TABLE_INGREDIENT);
        db.execSQL(CREATE_TABLE_STEP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            //old version = 1
            Log.i(TAG, "upgrading DB from version "+oldVersion);
            //alter
        }
    }


}

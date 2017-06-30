package id.dekz.bakingapp.database.contract;

import android.net.Uri;
import android.provider.BaseColumns;

import static id.dekz.bakingapp.util.Constant.AUTHORITY;

/**
 * Created by DEKZ on 6/29/2017.
 */

public class RecipeContract {

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_RECIPES = "recipes";

    public static final class RecipeEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();

        public static final String TABLE_NAME = "recipes";

        public static final String RECIPE_ID = "recipe_id";
        public static final String RECIPE_NAME = "recipe_name";
        public static final String RECIPE_SERVINGS = "recipe_servings";
        public static final String RECIPE_IMAGE = "recipe_image";
    }
}

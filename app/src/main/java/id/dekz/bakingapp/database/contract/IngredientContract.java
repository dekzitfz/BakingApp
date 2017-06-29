package id.dekz.bakingapp.database.contract;

import android.net.Uri;
import android.provider.BaseColumns;

import static id.dekz.bakingapp.util.Constant.AUTHORITY;

/**
 * Created by DEKZ on 6/29/2017.
 */

public class IngredientContract {

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_INGREDIENTS = "ingredients";

    public static final class IngredientEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();

        public static final String TABLE_NAME = "ingredients";

        public static final String RECIPE_ID = "recipe_id";
        public static final String INGREDIENT_QUANTITY = "ingredient_quantity";
        public static final String INGREDIENT_measure = "ingredient_measure";
        public static final String INGREDIENT = "ingredient";
    }
}

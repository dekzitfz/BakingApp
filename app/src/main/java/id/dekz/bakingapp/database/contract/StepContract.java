package id.dekz.bakingapp.database.contract;

import android.net.Uri;
import android.provider.BaseColumns;

import static id.dekz.bakingapp.util.Constant.AUTHORITY;

/**
 * Created by DEKZ on 6/29/2017.
 */

public class StepContract {

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_STEPS = "steps";

    public static final class StepEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STEPS).build();

        public static final String TABLE_NAME = "steps";

        public static final String RECIPE_ID = "recipe_id";
        public static final String STEP_ID = "step_id";
        public static final String STEP_SHORT_DESC = "step_short_description";
        public static final String STEP_DESCRIPTION = "step_description";
        public static final String STEP_VIDEO_URL = "step_video_url";
        public static final String STEP_THUMBNAIL_URL = "step_thumbnail_url";
    }
}

package id.dekz.bakingapp.features.recipelist;

import java.util.List;

import id.dekz.bakingapp.basemvp.BaseView;
import id.dekz.bakingapp.model.Recipe;

/**
 * Created by DEKZ on 6/27/2017.
 */

public interface RecipeListView extends BaseView {
    void onDataReceived(List<Recipe> data);
    void onFailure();
    void onWarningMessageReceived(String message);
}

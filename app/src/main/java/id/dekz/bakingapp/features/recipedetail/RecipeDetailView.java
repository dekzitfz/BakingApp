package id.dekz.bakingapp.features.recipedetail;

import android.support.v4.app.FragmentManager;

import id.dekz.bakingapp.basemvp.BaseView;
import id.dekz.bakingapp.model.Recipe;

/**
 * Created by DEKZ on 7/2/2017.
 */

public interface RecipeDetailView extends BaseView {
    void bindData(Recipe recipe);
    FragmentManager getFragmentManagerFromActivity();
}

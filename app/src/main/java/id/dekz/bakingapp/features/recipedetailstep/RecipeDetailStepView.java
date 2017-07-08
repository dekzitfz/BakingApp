package id.dekz.bakingapp.features.recipedetailstep;

import android.content.Context;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;

import id.dekz.bakingapp.basemvp.BaseView;
import id.dekz.bakingapp.model.Step;

/**
 * Created by DEKZ on 7/4/2017.
 */

public interface RecipeDetailStepView extends BaseView {
    void bindData(Step step);
    Context getContextFromFragment();
    void onPlayerSet(SimpleExoPlayer player, MediaSource mediaSource);
}

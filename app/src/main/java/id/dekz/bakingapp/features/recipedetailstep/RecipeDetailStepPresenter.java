package id.dekz.bakingapp.features.recipedetailstep;

import android.net.Uri;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import id.dekz.bakingapp.basemvp.BasePresenter;
import id.dekz.bakingapp.model.Step;

/**
 * Created by DEKZ on 7/4/2017.
 */

public class RecipeDetailStepPresenter implements BasePresenter<RecipeDetailStepView> {

    private static final String TAG = RecipeDetailStepPresenter.class.getSimpleName();
    private RecipeDetailStepView view;
    private Gson gson = new Gson();
    private SimpleExoPlayer player;

    @Override
    public void onAttach(RecipeDetailStepView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        player.stop();
        player.release();
        player = null;
        gson = null;
        view = null;
    }

    void getStepModel(String json){
        view.bindData(gson.fromJson(json, Step.class));
    }

    void setupPlayer(Uri uri){
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(view.getContextFromFragment()),
                new DefaultTrackSelector()
        );

        String userAgent = Util.getUserAgent(view.getContextFromFragment(), TAG);
        MediaSource mediaSource = new ExtractorMediaSource(
                uri,
                new DefaultDataSourceFactory(view.getContextFromFragment(), userAgent),
                new DefaultExtractorsFactory(),
                null,
                null
        );

        view.onPlayerSet(player, mediaSource);
    }
}

package id.dekz.bakingapp.features.recipedetailstep;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

import butterknife.BindBool;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.model.Step;
import id.dekz.bakingapp.util.Constant;
import id.dekz.bakingapp.util.URLUtils;

/**
 * Created by DEKZ on 7/4/2017.
 */

public class RecipeDetailStepFragment extends Fragment implements RecipeDetailStepView {

    private static final String TAG = RecipeDetailStepFragment.class.getSimpleName();
    private static final String JSON_STRING = "json_string";
    private static final String PLAYER_POSITION = "player_position";
    private Unbinder unbinder;
    private RecipeDetailStepPresenter presenter;
    private StepNavigationClickListener navigationClickListener;
    private SimpleExoPlayer mPlayer;
    private long playerPosition = 0;
    private String json;

    @BindView(R.id.player)SimpleExoPlayerView playerView;
    @BindView(R.id.tv_step_description)TextView description;
    @BindView(R.id.tv_unselected_step)TextView unselectedStepView;
    @BindView(R.id.view_detail_step)LinearLayout selectedStepView;
    @BindView(R.id.fab_next)FloatingActionButton nextButton;
    @BindView(R.id.fab_previous)FloatingActionButton previousButton;
    @BindView(R.id.tv_step_position)TextView stepPosition;
    @BindView(R.id.img_step)ImageView imageStep;

    @BindBool(R.bool.isLandscape)boolean isLandsacpe;
    @BindBool(R.bool.isTablet)boolean isTablet;

    public interface StepNavigationClickListener{
        void onNavigateStep(int targetPosition, int totalPosition);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            navigationClickListener = (StepNavigationClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement StepNavigationClickListener");
        }
    }

    public static RecipeDetailStepFragment newInstance(String json,
                                                       int currentStep,
                                                       int totalStep,
                                                       int previousStep,
                                                       int nextStep){
        RecipeDetailStepFragment fragment = new RecipeDetailStepFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_STEP, json);
        bundle.putInt(Constant.KEY_CURRENT_STEP, currentStep);
        bundle.putInt(Constant.KEY_TOTAL_STEPS, totalStep);
        bundle.putInt(Constant.KEY_PREVIOUS_STEP, previousStep);
        bundle.putInt(Constant.KEY_NEXT_STEP, nextStep);
        fragment.setArguments(bundle);

        return fragment;
    }

    public RecipeDetailStepFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_detail_step, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if(savedInstanceState!=null){
            json = savedInstanceState.getString(JSON_STRING);
            playerPosition = savedInstanceState.getLong(PLAYER_POSITION);
        }else{
            json = getArguments().getString(Constant.KEY_STEP);
        }

        onAttachView();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putString(JSON_STRING, json);
        if(mPlayer != null){
            outState.putLong(PLAYER_POSITION, mPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
        if(savedInstanceState!=null){
            json = savedInstanceState.getString(JSON_STRING);
            playerPosition = savedInstanceState.getLong(PLAYER_POSITION);
        }else{
            json = getArguments().getString(Constant.KEY_STEP);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPlayer != null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        unbinder.unbind();
        onDetachView();
    }

    @Override
    public void onAttachView() {
        presenter = new RecipeDetailStepPresenter();
        presenter.onAttach(this);

        if(json != null){
            selectedStepView.setVisibility(View.VISIBLE);
            unselectedStepView.setVisibility(View.GONE);
            presenter.getStepModel(json);
        }else{
            //first open from tablet
            selectedStepView.setVisibility(View.GONE);
            unselectedStepView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    public void bindData(Step step) {
        final int totalSteps = getArguments().getInt(Constant.KEY_TOTAL_STEPS);

        description.setText(step.getDescription());
        stepPosition.setText(step.getId()+"/"+totalSteps);

        presenter.checkMedia(step.getVideoURL(), step.getThumbnailURL());

        if(totalSteps==0){
            //twopane = true
            nextButton.setVisibility(View.GONE);
            previousButton.setVisibility(View.GONE);
            stepPosition.setVisibility(View.GONE);
        }

        if(step.getId() == 0){
            previousButton.setVisibility(View.INVISIBLE);
        }else if(step.getId() == totalSteps){
            nextButton.setVisibility(View.INVISIBLE);
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationClickListener.onNavigateStep(
                        getArguments().getInt(Constant.KEY_NEXT_STEP),
                        totalSteps
                );
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationClickListener.onNavigateStep(
                        getArguments().getInt(Constant.KEY_PREVIOUS_STEP),
                        totalSteps
                );
            }
        });
    }

    @Override
    public Context getContextFromFragment() {
        return getActivity();
    }

    @Override
    public void onPlayerSet(SimpleExoPlayer player, final MediaSource mediaSource) {

        playerView.setVisibility(View.VISIBLE);
        imageStep.setVisibility(View.GONE);

        mPlayer = player;


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isLandsacpe && !isTablet){
                    //handset landscape mode
                    //fullscreen

                    playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                }

                playerView.setPlayer(mPlayer);
                mPlayer.prepare(mediaSource);
                mPlayer.seekTo(playerPosition);
                mPlayer.setPlayWhenReady(true);
            }
        });
    }

    @Override
    public void onImageSet() {
        playerView.setVisibility(View.GONE);
        imageStep.setVisibility(View.VISIBLE);
    }

    @Override
    public ImageView getImageView() {
        return imageStep;
    }

    @Override
    public void onNomediaAvailable() {
        playerView.setVisibility(View.GONE);
        imageStep.setVisibility(View.GONE);
    }
}

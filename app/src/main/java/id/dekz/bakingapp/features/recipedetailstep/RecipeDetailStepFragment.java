package id.dekz.bakingapp.features.recipedetailstep;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.model.Step;
import id.dekz.bakingapp.util.Constant;

/**
 * Created by DEKZ on 7/4/2017.
 */

public class RecipeDetailStepFragment extends Fragment implements RecipeDetailStepView {

    private Unbinder unbinder;
    private RecipeDetailStepPresenter presenter;
    private StepNavigationClickListener navigationClickListener;

    @BindView(R.id.player)SimpleExoPlayerView player;
    @BindView(R.id.tv_step_description)TextView description;
    @BindView(R.id.tv_unselected_step)TextView unselectedStepView;
    @BindView(R.id.view_detail_step)LinearLayout selectedStepView;
    @BindView(R.id.fab_next)FloatingActionButton nextButton;
    @BindView(R.id.fab_previous)FloatingActionButton previousButton;
    @BindView(R.id.tv_step_position)TextView stepPosition;

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
                                                       @Nullable int currentStep,
                                                       @Nullable int totalStep){
        RecipeDetailStepFragment fragment = new RecipeDetailStepFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_STEP, json);
        bundle.putInt(Constant.KEY_CURRENT_STEP, currentStep);
        bundle.putInt(Constant.KEY_TOTAL_STEPS, totalStep);
        fragment.setArguments(bundle);

        return fragment;
    }

    public RecipeDetailStepFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail_step, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        onAttachView();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        onDetachView();
    }

    @Override
    public void onAttachView() {
        presenter = new RecipeDetailStepPresenter();
        presenter.onAttach(this);

        String json = getArguments().getString(Constant.KEY_STEP);
        if(json != null){
            selectedStepView.setVisibility(View.VISIBLE);
            unselectedStepView.setVisibility(View.GONE);
            presenter.getStepModel(json);
        }else{
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
        description.setText(step.getDescription());
        final int currentPos = getArguments().getInt(Constant.KEY_CURRENT_STEP);
        final int totalSteps = getArguments().getInt(Constant.KEY_TOTAL_STEPS);
        stepPosition.setText(currentPos+"/"+totalSteps);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationClickListener.onNavigateStep(currentPos+1, totalSteps);
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationClickListener.onNavigateStep(currentPos-1, totalSteps);
            }
        });
    }
}

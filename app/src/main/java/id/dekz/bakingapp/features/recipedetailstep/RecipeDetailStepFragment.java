package id.dekz.bakingapp.features.recipedetailstep;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @BindView(R.id.player)SimpleExoPlayerView player;
    @BindView(R.id.tv_step_description)TextView description;

    public static RecipeDetailStepFragment newInstance(String json){
        RecipeDetailStepFragment fragment = new RecipeDetailStepFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_STEP, json);
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
            presenter.getStepModel(json);
        }
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    public void bindData(Step step) {
        description.setText(step.getDescription());
    }
}

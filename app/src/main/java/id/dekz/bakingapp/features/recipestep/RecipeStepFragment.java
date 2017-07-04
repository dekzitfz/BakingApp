package id.dekz.bakingapp.features.recipestep;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.adapter.StepAdapter;
import id.dekz.bakingapp.features.recipedetail.RecipeDetailActivity;
import id.dekz.bakingapp.features.recipedetailstep.RecipeDetailStepFragment;
import id.dekz.bakingapp.model.Recipe;
import id.dekz.bakingapp.model.Step;
import id.dekz.bakingapp.util.Constant;

/**
 * Created by DEKZ on 7/4/2017.
 */

public class RecipeStepFragment extends Fragment implements RecipeStepView, StepAdapter.OnStepClick {

    private RecipeStepPresenter presenter;
    private Unbinder unbinder;
    private StepAdapter stepAdapter;

    @BindView(R.id.tv_ingredients)TextView ingredients;
    @BindView(R.id.rv_steps)RecyclerView rvStep;

    public RecipeStepFragment() {
    }

    public static RecipeStepFragment newInstance(String json){
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_RECIPE, json);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        onAttachView();
        return rootView;
    }

    @Override
    public void onAttachView() {
        presenter = new RecipeStepPresenter();
        presenter.onAttach(this);

        String json = getArguments().getString(Constant.KEY_RECIPE, null);
        if(json != null){
            setupRV();
            presenter.getRecipeModel(json);
        }
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        onDetachView();
    }

    @Override
    public void bindData(Recipe recipe) {
        ingredients.setText(presenter.getEachIngredient(recipe.getIngredients()));
        stepAdapter.replaceAll(recipe.getSteps());
        stepAdapter.addClickListener(this);
    }

    @Override
    public FragmentManager getFragmentManagerFromFragment() {
        return getFragmentManager();
    }

    private void setupRV(){
        stepAdapter = new StepAdapter();
        rvStep.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvStep.setAdapter(stepAdapter);
        rvStep.addItemDecoration(new DividerItemDecoration(rvStep.getContext(),
                LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onStepClicked(Step step) {
        presenter.addFragment(presenter.getDetailStepFragment(new Gson().toJson(step)));
    }
}

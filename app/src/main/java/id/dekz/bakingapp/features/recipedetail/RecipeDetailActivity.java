package id.dekz.bakingapp.features.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.adapter.StepAdapter;
import id.dekz.bakingapp.features.recipedetailstep.RecipeDetailStepFragment;
import id.dekz.bakingapp.features.recipestep.RecipeStepFragment;
import id.dekz.bakingapp.model.Recipe;

import static android.support.v7.recyclerview.R.attr.layoutManager;

/**
 * Created by DEKZ on 7/2/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailView,
        RecipeStepFragment.OnStepSelected,
        RecipeDetailStepFragment.StepNavigationClickListener {

    private RecipeDetailPresenter presenter;
    private String jsonStr;
    private boolean isTwoPane = false;

    @BindView(R.id.toolbar)Toolbar toolbar;

    @Nullable @BindView(R.id.container)FrameLayout container;

    //container for tab
    @Nullable @BindView(R.id.container_left)FrameLayout containerLeft;
    @Nullable @BindView(R.id.container_right)FrameLayout containerRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        jsonStr = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        onAttachView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDetachView();
    }

    @Override
    public void onAttachView() {
        presenter = new RecipeDetailPresenter();
        presenter.onAttach(this);

        if(container == null){
            isTwoPane = true;
        }

        if(jsonStr != null){
            presenter.getRecipeModel(jsonStr);
            if(isTwoPane){
                presenter.addFragments(
                        presenter.getStepFragment(jsonStr, isTwoPane),
                        presenter.getStepDetailFragment(null, 0, 0, 0, 0)
                );
            }else{
                presenter.addFragment(presenter.getStepFragment(jsonStr, isTwoPane));
            }
        }
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    public void bindData(Recipe recipe) {
        getSupportActionBar().setTitle(recipe.getName());
    }

    @Override
    public int getContainerID() {
        return R.id.container;
    }

    @Override
    public FragmentManager getFragmentManagerFromActivity() {
        return getSupportFragmentManager();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(isTwoPane){
            RecipeDetailActivity.this.finish();
        }else{
            if(getSupportFragmentManager().getBackStackEntryCount() > 0){
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    //only for twopane
    @Override
    public void onstepselected(String stepJson) {
        presenter.changeFragmentRight(presenter.getStepDetailFragment(stepJson, 0, 0, 0, 0));
    }

    //only called from handset with arrow nav
    @Override
    public void onNavigateStep(int targetPosition, int totalPosition) {
        presenter.replaceFragment(
                presenter.getStepDetailFragment(
                        presenter.getStepJsonByIndex(jsonStr, targetPosition),
                        targetPosition,
                        totalPosition,
                        presenter.getPreviousStepIDByTargetID(jsonStr, targetPosition),
                        presenter.getNextStepIDByTargetID(jsonStr, targetPosition)
                )
        );
    }
}

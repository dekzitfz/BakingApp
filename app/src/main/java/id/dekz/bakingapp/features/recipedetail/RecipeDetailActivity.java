package id.dekz.bakingapp.features.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.features.recipedetailstep.RecipeDetailStepFragment;
import id.dekz.bakingapp.features.recipestep.RecipeStepFragment;
import id.dekz.bakingapp.model.Recipe;

/**
 * Created by DEKZ on 7/2/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity
        implements RecipeDetailView,
        RecipeStepFragment.OnStepSelected,
        RecipeDetailStepFragment.StepNavigationClickListener {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String JSON_STRING = "json_string";
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
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if(savedInstanceState != null){
            jsonStr = savedInstanceState.getString(JSON_STRING);
        }else{
            jsonStr = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        }

        onAttachView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
        outState.putString(JSON_STRING, jsonStr);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        onDetachView();
    }

    @Override
    public void onAttachView() {
        Log.d(TAG, "onAttachView");
        presenter = new RecipeDetailPresenter();
        presenter.onAttach(this);

        if(container == null){isTwoPane = true;}


        if(jsonStr != null){
            presenter.getRecipeModel(jsonStr);
            if(isTwoPane){
                presenter.addFragments(
                        presenter.getStepFragment(jsonStr, isTwoPane),
                        presenter.getStepDetailFragment(null, 0, 0, 0, 0)
                );
            }else{
                Log.d(TAG, "fragment added");
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
        Log.d(TAG, "recipename--> "+recipe.getName());
        if(getSupportActionBar() ==  null){
            Log.w(TAG, "actionbar is null!!");
        }else{
            getSupportActionBar().setTitle(recipe.getName());
        }
    }

    @Override
    public FragmentManager getFragmentManagerFromActivity() {
        return getSupportFragmentManager();
    }

    @Override
    public void onBackPressed() {
        if(isTwoPane){
            RecipeDetailActivity.this.finish();
        }else{
            if (getSupportFragmentManager().getBackStackEntryCount() > 0){
                getSupportFragmentManager().popBackStack();
            }else {
                RecipeDetailActivity.this.finish();
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

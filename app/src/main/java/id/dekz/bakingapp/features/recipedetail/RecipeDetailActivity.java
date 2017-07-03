package id.dekz.bakingapp.features.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.adapter.StepAdapter;
import id.dekz.bakingapp.model.Recipe;

import static android.support.v7.recyclerview.R.attr.layoutManager;

/**
 * Created by DEKZ on 7/2/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailView {

    private RecipeDetailPresenter presenter;
    private String jsonStr;
    private StepAdapter stepAdapter;

    @BindView(R.id.tv_ingredients)TextView ingredients;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.rv_steps)RecyclerView rvStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        jsonStr = getIntent().getStringExtra(Intent.EXTRA_TEXT);

        setupRV();
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

        if(jsonStr != null) presenter.getRecipeModel(jsonStr);
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    public void bindData(Recipe recipe) {
        getSupportActionBar().setTitle(recipe.getName());
        ingredients.setText(presenter.getEachIngredient(recipe.getIngredients()));
        stepAdapter.replaceAll(recipe.getSteps());
    }

    private void setupRV(){
        stepAdapter = new StepAdapter();
        rvStep.setLayoutManager(new LinearLayoutManager(this));
        rvStep.setAdapter(stepAdapter);
        rvStep.addItemDecoration(new DividerItemDecoration(rvStep.getContext(),
                LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        RecipeDetailActivity.this.finish();
    }
}

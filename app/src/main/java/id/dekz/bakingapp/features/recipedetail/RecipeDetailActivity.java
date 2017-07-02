package id.dekz.bakingapp.features.recipedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import id.dekz.bakingapp.R;

/**
 * Created by DEKZ on 7/2/2017.
 */

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailView {

    private RecipeDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

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
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }
}

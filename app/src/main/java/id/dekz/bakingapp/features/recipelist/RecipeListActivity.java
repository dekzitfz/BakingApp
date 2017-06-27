package id.dekz.bakingapp.features.recipelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import butterknife.ButterKnife;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.model.Recipe;

public class RecipeListActivity extends AppCompatActivity implements RecipeListView {

    private RecipeListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        onAttachView();
    }

    @Override
    public void onAttachView() {
        presenter = new RecipeListPresenter();
        presenter.onAttach(this);
        presenter.loadData();
    }

    @Override
    public void onDetachView() {
        presenter.onDetach();
    }

    @Override
    public void onDataReceived(List<Recipe> data) {

    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onWarningMessageReceived(String message) {

    }
}

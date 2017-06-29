package id.dekz.bakingapp.features.recipelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.adapter.RecipeAdapter;
import id.dekz.bakingapp.model.Recipe;

public class RecipeListActivity extends AppCompatActivity implements RecipeListView {

    private RecipeListPresenter presenter;
    private RecipeAdapter adapter;

    @BindView(R.id.rv_recipe)RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        setupRecyclerView();
        onAttachView();
    }

    private void setupRecyclerView(){
        adapter = new RecipeAdapter();
        rv.setAdapter(adapter);
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
        adapter.replaceAll(data);
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onWarningMessageReceived(String message) {

    }
}

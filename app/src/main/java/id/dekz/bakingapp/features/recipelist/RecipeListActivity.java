package id.dekz.bakingapp.features.recipelist;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.adapter.RecipeAdapter;
import id.dekz.bakingapp.model.Recipe;

public class RecipeListActivity extends AppCompatActivity implements RecipeListView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = RecipeListActivity.class.getSimpleName();
    private static final String KEY_SCROLL_STATE = "scroll_state";

    private RecipeListPresenter presenter;
    private RecipeAdapter adapter;
    private Parcelable layoutManagerSavedState;

    @BindView(R.id.rv_recipe)RecyclerView rv;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.swipe_refresh)SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        if(savedInstanceState != null){
            layoutManagerSavedState = savedInstanceState.getParcelable(KEY_SCROLL_STATE);
        }

        setSupportActionBar(toolbar);

        setupRecyclerView();
        onAttachView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_SCROLL_STATE, rv.getLayoutManager().onSaveInstanceState());
    }

    private void setupRecyclerView(){
        adapter = new RecipeAdapter();
        rv.setAdapter(adapter);
        swipeRefresh.setOnRefreshListener(this);
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
    protected void onDestroy() {
        super.onDestroy();
        swipeRefresh.setOnRefreshListener(null);
        onDetachView();
    }

    @Override
    public void onDataReceived(List<Recipe> data) {
        swipeRefresh.setRefreshing(false);
        adapter.replaceAll(data);
        if(layoutManagerSavedState!=null){
            rv.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
    }

    @Override
    public void onDataLoading() {
        swipeRefresh.setRefreshing(true);
    }

    @Override
    public void onWarningMessageReceived(String message) {
        swipeRefresh.setRefreshing(false);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public ContentResolver getResolver() {
        return getContentResolver();
    }

    @Override
    public Context getContext() {
        return RecipeListActivity.this;
    }

    @Override
    public LoaderManager getLoaderManagerFromActivity() {
        return getSupportLoaderManager();
    }


    @Override
    public void onRefresh() {
        presenter.loadData();
    }
}

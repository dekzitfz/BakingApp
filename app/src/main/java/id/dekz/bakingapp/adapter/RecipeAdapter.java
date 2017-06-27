package id.dekz.bakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import id.dekz.bakingapp.R;
import id.dekz.bakingapp.adapter.viewholder.RecipeViewHolder;
import id.dekz.bakingapp.model.Recipe;

/**
 * Created by DEKZ on 6/27/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {

    private List<Recipe> data = new ArrayList<>();

    public RecipeAdapter() {
    }

    public void replaceAll(List<Recipe> data){
        this.data.clear();
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.row_recipe, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

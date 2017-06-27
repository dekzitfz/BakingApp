package id.dekz.bakingapp.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.dekz.bakingapp.R;
import id.dekz.bakingapp.model.Recipe;

/**
 * Created by DEKZ on 6/27/2017.
 */

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.img_recipe)ImageView img;
    @BindView(R.id.tv_recipe_name)TextView name;
    @BindView(R.id.tv_recipe_servings)TextView servings;

    public RecipeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Recipe data){
        img.setImageResource(R.drawable.nutella_pie_img);
        name.setText(data.getName());
        servings.setText(data.getResolvedServings());
    }
}

package id.dekz.bakingapp.util;

import id.dekz.bakingapp.R;

/**
 * Created by DEKZ on 6/29/2017.
 */

public class RecipeImageGenerator {
    public static int getImage(int id){
        switch (id){
            case 1 :
                return R.drawable.nutella_pie_img;
            case 2 :
                return R.drawable.brownies_img;
            case 3 :
                return R.drawable.yellow_cake_img;
            case 4 :
                return R.drawable.cheesecake_img;
            default:
                return R.drawable.nutella_pie_img;
        }
    }
}

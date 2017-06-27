package id.dekz.bakingapp.util.rest;

import java.util.List;

import id.dekz.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by DEKZ on 6/27/2017.
 */

public interface APIService {

    @GET(".")
    Call<List<Recipe>> getRecipes();

}

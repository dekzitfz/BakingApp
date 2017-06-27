package id.dekz.bakingapp.util.rest;

import id.dekz.bakingapp.BuildConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DEKZ on 6/27/2017.
 */

public class RestClient {

    private APIService apiService;

    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(APIService.class);
    }

    public APIService getService() {
        return apiService;
    }
}

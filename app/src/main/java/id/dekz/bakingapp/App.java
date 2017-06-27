package id.dekz.bakingapp;

import android.app.Application;

import id.dekz.bakingapp.util.rest.RestClient;

/**
 * Created by DEKZ on 6/27/2017.
 */

public class App extends Application {

    private static RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();
        restClient = new RestClient();
    }

    public static RestClient getRestClient() {
        return restClient;
    }
}

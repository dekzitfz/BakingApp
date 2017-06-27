package id.dekz.bakingapp;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import id.dekz.bakingapp.util.rest.RestClient;

/**
 * Created by DEKZ on 6/27/2017.
 */

public class App extends Application {

    private static RestClient restClient;

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.BUILD_TYPE.equals("debug")){
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return;
            }
            LeakCanary.install(this);
        }

        restClient = new RestClient();
    }

    public static RestClient getRestClient() {
        return restClient;
    }
}

package id.dekz.bakingapp.features.recipedetailstep;

import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;

import java.io.IOException;

import id.dekz.bakingapp.basemvp.BasePresenter;
import id.dekz.bakingapp.model.Step;
import id.dekz.bakingapp.util.ContentTypeUtils;
import id.dekz.bakingapp.util.URLUtils;

/**
 * Created by DEKZ on 7/4/2017.
 */

public class RecipeDetailStepPresenter implements BasePresenter<RecipeDetailStepView> {

    private static final String TAG = RecipeDetailStepPresenter.class.getSimpleName();

    private RecipeDetailStepView view;
    private Gson gson = new Gson();
    private SimpleExoPlayer player;
    private Thread thread;
    private String contentType;

    @Override
    public void onAttach(RecipeDetailStepView BaseView) {
        view = BaseView;
    }

    @Override
    public void onDetach() {
        if(player!=null){
            player.stop();
            player.release();
            player = null;
        }

        thread = null;
        gson = null;
        view = null;
    }

    void getStepModel(String json){
        view.bindData(gson.fromJson(json, Step.class));
    }

    void setupPlayer(Uri uri){
        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(view.getContextFromFragment()),
                new DefaultTrackSelector()
        );

        String userAgent = Util.getUserAgent(view.getContextFromFragment(), TAG);
        MediaSource mediaSource = new ExtractorMediaSource(
                uri,
                new DefaultDataSourceFactory(view.getContextFromFragment(), userAgent),
                new DefaultExtractorsFactory(),
                null,
                null
        );

        view.onPlayerSet(player, mediaSource);
    }

    void setupImage(String imageURL){
        Glide.with(view.getContextFromFragment())
                .load(imageURL)
                .into(view.getImageView());
        view.onImageSet();
    }

    void contentChecker(final String url, final String type){
        thread = new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    contentType = URLUtils.getContentType(url);
                    Log.i(TAG, "content -->"+contentType);
                    if(type.equals("video")){
                        //check if content-type valid
                        if(ContentTypeUtils.isVideo(contentType)){
                            //video is valid
                            setupPlayer(Uri.parse(url));
                        }else if(ContentTypeUtils.isImage(contentType)){
                            //whoa! its actually an image!
                            setupImage(url);
                        }else{
                            //wtf is this content?
                            Log.w(TAG, "unknown content");
                        }
                    }else if(type.equals("image")){
                        //check if content-type valid
                        if(ContentTypeUtils.isImage(contentType)){
                            //image is valid
                            setupImage(url);
                        }else if(ContentTypeUtils.isVideo(contentType)){
                            //whoa! its actually a video!
                            setupPlayer(Uri.parse(url));
                        }else{
                            //wtf is this content?
                            Log.w(TAG, "unknown content");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    void checkMedia(String videoURL, String thumbURL){
        //check if video url available
        if(videoURL.equals("") || videoURL.length()==0){
            //video not available, check img
            if(thumbURL.equals("") || thumbURL.length()==0){
                //media is empty
                view.onNomediaAvailable();
            }else{
                //only image available, show it
                contentChecker(thumbURL, "image");
            }
        }else{
            //video avail, show video, ignore img
            contentChecker(videoURL, "video");
        }
    }
}

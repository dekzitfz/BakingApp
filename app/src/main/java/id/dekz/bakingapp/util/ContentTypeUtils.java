package id.dekz.bakingapp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DEKZ on 7/8/2017.
 */

public class ContentTypeUtils {

    public static boolean isVideo(String contentType){
        boolean result = false;
        for(String str: getVideoContentTypes()){
            if(str.equals(contentType)){
                result = true;
            }
        }

        return result;
    }

    public static boolean isImage(String contentType){
        boolean result = false;
        for(String str: getImageContentTypes()){
            if(str.equals(contentType)){
                result = true;
            }
        }

        return result;
    }

    private static List<String> getVideoContentTypes(){
        List<String> videos = new ArrayList<>();
        videos.add("video/x-flv");
        videos.add("video/mpeg");
        videos.add("video/x-m4v");
        videos.add("video/quicktime");
        videos.add("video/mp4");
        videos.add("video/x-flv");
        return videos;
    }

    private static List<String> getImageContentTypes(){
        List<String> images = new ArrayList<>();
        images.add("image/gif");
        images.add("image/jpeg");
        images.add("image/png");
        return images;
    }
}

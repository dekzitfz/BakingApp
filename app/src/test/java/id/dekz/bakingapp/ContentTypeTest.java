package id.dekz.bakingapp;

import org.junit.Test;

import id.dekz.bakingapp.util.ContentTypeUtils;

import static org.junit.Assert.assertEquals;

/**
 * Created by DEKZ on 7/8/2017.
 */

public class ContentTypeTest {
    @Test
    public void content_isCorrect() throws Exception {
        assertEquals(true, ContentTypeUtils.isVideo("video/mp4"));
        assertEquals(true, ContentTypeUtils.isImage("image/jpeg"));
    }
}

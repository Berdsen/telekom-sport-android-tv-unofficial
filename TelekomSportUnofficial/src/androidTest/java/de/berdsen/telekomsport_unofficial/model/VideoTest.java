package de.berdsen.telekomsport_unofficial.model;

import junit.framework.TestCase;

/**
 * Created by berthm on 26.09.2017.
 */
public class VideoTest extends TestCase {

    private Video testVideo;

    private final String title = "VideoTitle";
    private final String category = "VideoCategory";
    private final String description = "VideoDescription" ;
    private final String videoUrl = "VideoUrl";
    private final String videoPoster = "VideoPoster";

    public void setUp() throws Exception {
        super.setUp();

        testVideo = new Video();
        testVideo.setTitle(title);
        testVideo.setCategory(category);
        testVideo.setDescription(description);
        testVideo.setVideoUrl(videoUrl);
        testVideo.setPoster(videoPoster);
    }

    public void tearDown() throws Exception {
        testVideo = null;
    }

    public void testToString() throws Exception {
        assertEquals("Video {title='VideoTitle', description='VideoDescription', videoUrl='VideoUrl', category='VideoCategory', poster='VideoPoster'}", testVideo.toString());
    }

    public void testGetTitle() throws Exception {
        assertEquals(title, testVideo.getTitle());
    }

    public void testGetDescription() throws Exception {
        assertEquals(description, testVideo.getDescription());
    }

    public void testGetVideoUrl() throws Exception {
        assertEquals(videoUrl, testVideo.getVideoUrl());
    }

    public void testGetCategory() throws Exception {
        assertEquals(category, testVideo.getCategory());
    }

    public void testGetPoster() throws Exception {
        assertEquals(videoPoster, testVideo.getPoster());
    }

    public void testSetTitle() throws Exception {
        String newTitle = "NewTitle";

        assertEquals(title, testVideo.getTitle());
        testVideo.setTitle(newTitle);
        assertEquals(newTitle, testVideo.getTitle());
    }

    public void testSetDescription() throws Exception {
        String newDescription = "NewDescription";

        assertEquals(description, testVideo.getDescription());
        testVideo.setDescription(newDescription);
        assertEquals(newDescription, testVideo.getDescription());
    }

    public void testSetVideoUrl() throws Exception {
        String newVideoUrl = "NewVideoUrl";

        assertEquals(videoUrl, testVideo.getVideoUrl());
        testVideo.setVideoUrl(newVideoUrl);
        assertEquals(newVideoUrl, testVideo.getVideoUrl());
    }

    public void testSetCategory() throws Exception {
        String newCategory = "NewCategory";

        assertEquals(category, testVideo.getCategory());
        testVideo.setCategory(newCategory);
        assertEquals(newCategory, testVideo.getCategory());
    }

    public void testSetPoster() throws Exception {
        String newVideoPoster = "NewPoster";

        assertEquals(videoPoster, testVideo.getPoster());
        testVideo.setPoster(newVideoPoster);
        assertEquals(newVideoPoster, testVideo.getPoster());
    }

}
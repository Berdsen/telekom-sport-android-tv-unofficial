package de.berdsen.telekomsport_unofficial.model;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@PrepareForTest({ Video.class })
@RunWith(PowerMockRunner.class)
public class VideoTest {

    private Video testVideo;

    private final String title = "VideoTitle";
    private final String category = "VideoCategory";
    private final String description = "VideoDescription" ;
    private final String videoUrl = "VideoUrl";
    private final String videoPoster = "VideoPoster";

    @Before
    public void setUp() throws Exception {
        testVideo = new Video();
        testVideo.setTitle(title);
        testVideo.setCategory(category);
        testVideo.setDescription(description);
        testVideo.setVideoUrl(videoUrl);
        testVideo.setPoster(videoPoster);
    }

    @After
    public void tearDown() throws Exception {
        testVideo = null;
    }

    @Test
    public void testToString() throws Exception {
        Assert.assertEquals("Video {title='VideoTitle', description='VideoDescription', videoUrl='VideoUrl', category='VideoCategory', poster='VideoPoster'}", testVideo.toString());
    }

    @Test
    public void testGetTitle() throws Exception {
        Assert.assertEquals(title, testVideo.getTitle());
    }

    @Test
    public void testGetDescription() throws Exception {
        Assert.assertEquals(description, testVideo.getDescription());
    }

    @Test
    public void testGetVideoUrl() throws Exception {
        Assert.assertEquals(videoUrl, testVideo.getVideoUrl());
    }

    @Test
    public void testGetCategory() throws Exception {
        Assert.assertEquals(category, testVideo.getCategory());
    }

    @Test
    public void testGetPoster() throws Exception {
        Assert.assertEquals(videoPoster, testVideo.getPoster());
    }

    @Test
    public void testSetTitle() throws Exception {
        String newTitle = "NewTitle";

        Assert.assertEquals(title, testVideo.getTitle());
        testVideo.setTitle(newTitle);
        Assert.assertEquals(newTitle, testVideo.getTitle());
    }

    @Test
    public void testSetDescription() throws Exception {
        String newDescription = "NewDescription";

        Assert.assertEquals(description, testVideo.getDescription());
        testVideo.setDescription(newDescription);
        Assert.assertEquals(newDescription, testVideo.getDescription());
    }

    @Test
    public void testSetVideoUrl() throws Exception {
        String newVideoUrl = "NewVideoUrl";

        Assert.assertEquals(videoUrl, testVideo.getVideoUrl());
        testVideo.setVideoUrl(newVideoUrl);
        Assert.assertEquals(newVideoUrl, testVideo.getVideoUrl());
    }

    @Test
    public void testSetCategory() throws Exception {
        String newCategory = "NewCategory";

        Assert.assertEquals(category, testVideo.getCategory());
        testVideo.setCategory(newCategory);
        Assert.assertEquals(newCategory, testVideo.getCategory());
    }

    @Test
    public void testSetPoster() throws Exception {
        String newVideoPoster = "NewPoster";

        Assert.assertEquals(videoPoster, testVideo.getPoster());
        testVideo.setPoster(newVideoPoster);
        Assert.assertEquals(newVideoPoster, testVideo.getPoster());
    }
}
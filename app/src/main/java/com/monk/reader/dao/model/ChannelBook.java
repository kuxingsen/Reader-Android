package com.monk.reader.dao.model;

import java.util.List;

/**
 * Created by heyao on 8/6/17.
 */

public class ChannelBook {

    private StoryCate storyCate;
    private List<StoryArticle> storyArticle;

    public List<StoryArticle> getStoryArticle() {
        return storyArticle;
    }

    public void setStoryArticle(List<StoryArticle> storyArticle) {
        this.storyArticle = storyArticle;
    }
}

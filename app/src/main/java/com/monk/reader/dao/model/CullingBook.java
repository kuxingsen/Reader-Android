package com.monk.reader.dao.model;

/**
 * Created by heyao on 8/6/17.
 */

public class CullingBook {
    private StoryArticle storyArticle;
    private StoryCulling storyCulling;

    public StoryArticle getStoryArticle() {
        return storyArticle;
    }

    public void setStoryArticle(StoryArticle storyArticle) {
        this.storyArticle = storyArticle;
    }

    public StoryCulling getStoryCulling() {
        return storyCulling;
    }

    public void setStoryCulling(StoryCulling storyCulling) {
        this.storyCulling = storyCulling;
    }
}

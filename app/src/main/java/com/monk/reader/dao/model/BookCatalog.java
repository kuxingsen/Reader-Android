package com.monk.reader.dao.model;

import java.util.ArrayList;

/**
 * Created by heyao on 8/11/17.
 */

public class BookCatalog {
    private StoryArticle storyArticle;
    private ArrayList<Catalog> chapList;

    public StoryArticle getStoryArticle() {
        return storyArticle;
    }

    public void setStoryArticle(StoryArticle storyArticle) {
        this.storyArticle = storyArticle;
    }

    public ArrayList<Catalog> getChapList() {
        return chapList;
    }

    public void setChapList(ArrayList<Catalog> chapList) {
        this.chapList = chapList;
    }
}

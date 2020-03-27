package com.monk.reader.retrofit2.bean;

public class Banner {
    private Long id;
    private String picture;
    private String url;

    @Override
    public String toString() {
        return "Banner{" +
                "id=" + id +
                ", picture='" + picture + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package com.monk.reader.retrofit2.bean;


public class Book {
    private Long id;
    private String name;
    private String author;
    private String picture;
    private String introduction;
    private String upDate;
    private int length;
    private String rangeValue;
    private Long categoryId;
    private String categoryName;

    private String path;
    private String charSet;
    private long size;//字节数

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", picture='" + picture + '\'' +
                ", introduction='" + introduction + '\'' +
                ", upDate='" + upDate + '\'' +
                ", length=" + length +
                ", rangeValue='" + rangeValue + '\'' +
                ", categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", path='" + path + '\'' +
                ", charSet='" + charSet + '\'' +
                ", size=" + size +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getRangeValue() {
        return rangeValue;
    }

    public void setRangeValue(String rangeValue) {
        this.rangeValue = rangeValue;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getUpDate() {
        return upDate;
    }

    public void setUpDate(String upDate) {
        this.upDate = upDate;
    }
}

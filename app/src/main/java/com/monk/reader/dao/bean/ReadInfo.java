package com.monk.reader.dao.bean;

import com.monk.reader.retrofit2.bean.Book;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ReadInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id(autoincrement = true)
    private Long id;


    private Long bookId;
    private String bookName;
    private String bookAuthor;
    private Long bookSize;
    private String bookIntroduction;
    private String bookPicture;
    private String categoryName;
    private Long start;
    private Long duration;
    private Long updateTime;

    private Long userId;

    public ReadInfo(ShelfBook book) {
        bookId = Long.valueOf(book.getPath());
        bookName = book.getName();
        bookSize = book.getBookLen();
        bookPicture = book.getPicture().replaceAll("\\\\","/");
    }

    @Generated(hash = 1712044518)
    public ReadInfo(Long id, Long bookId, String bookName, String bookAuthor,
            Long bookSize, String bookIntroduction, String bookPicture,
            String categoryName, Long start, Long duration, Long updateTime,
            Long userId) {
        this.id = id;
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.bookSize = bookSize;
        this.bookIntroduction = bookIntroduction;
        this.bookPicture = bookPicture;
        this.categoryName = categoryName;
        this.start = start;
        this.duration = duration;
        this.updateTime = updateTime;
        this.userId = userId;
    }

    @Generated(hash = 687160188)
    public ReadInfo() {
    }

    @Override
    public String toString() {
        return "ReadInfo{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", bookAuthor='" + bookAuthor + '\'' +
                ", bookSize=" + bookSize +
                ", bookIntroduction='" + bookIntroduction + '\'' +
                ", bookPicture='" + bookPicture + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", start=" + start +
                ", duration=" + duration +
                ", updateTime=" + updateTime +
                ", userId=" + userId +
                '}';
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getBookSize() {
        return bookSize;
    }

    public void setBookSize(Long bookSize) {
        this.bookSize = bookSize;
    }

    public String getBookIntroduction() {
        return bookIntroduction;
    }

    public void setBookIntroduction(String bookIntroduction) {
        this.bookIntroduction = bookIntroduction;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPicture() {
        return bookPicture;
    }

    public void setBookPicture(String bookPicture) {
        this.bookPicture = bookPicture;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStart() {
        return this.start;
    }

    public void setStart(Long start) {
        this.start = start;
    }
}

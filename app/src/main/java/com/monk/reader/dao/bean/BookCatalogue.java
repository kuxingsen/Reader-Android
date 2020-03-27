package com.monk.reader.dao.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 章节
 */
@Entity
public class BookCatalogue {
    @Id(autoincrement = true)
    private Long id;
    private String bookCatalogue;
    private long bookCatalogueStartPos;
    private Long bookId;

    @Generated(hash = 236715025)
    public BookCatalogue(Long id, String bookCatalogue, long bookCatalogueStartPos,
            Long bookId) {
        this.id = id;
        this.bookCatalogue = bookCatalogue;
        this.bookCatalogueStartPos = bookCatalogueStartPos;
        this.bookId = bookId;
    }

    @Generated(hash = 1988414870)
    public BookCatalogue() {
    }

    @Override
    public String toString() {
        return "BookCatalogue{" +
                "id=" + id +
                ", bookCatalogue='" + bookCatalogue + '\'' +
                ", bookCatalogueStartPos=" + bookCatalogueStartPos +
                ", bookId=" + bookId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookCatalogue() {
        return bookCatalogue;
    }

    public void setBookCatalogue(String bookCatalogue) {
        this.bookCatalogue = bookCatalogue;
    }

    public long getBookCatalogueStartPos() {
        return bookCatalogueStartPos;
    }

    public void setBookCatalogueStartPos(long bookCatalogueStartPos) {
        this.bookCatalogueStartPos = bookCatalogueStartPos;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}


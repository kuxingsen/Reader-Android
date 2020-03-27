package com.monk.reader.dao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;
import org.greenrobot.greendao.DaoException;
import com.monk.reader.dao.DaoSession;
import com.monk.reader.dao.BookCatalogueDao;
import com.monk.reader.dao.ShelfBookDao;

@Entity
public class ShelfBook implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id(autoincrement = true)
    private Long id;

    private String name;
    private String path;
    private Long begin;
    private String charset;
    private Integer position;
    @ToMany(referencedJoinProperty = "bookId")
    private List<BookCatalogue> bookCatalogueList;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1373704499)
    private transient ShelfBookDao myDao;
    @Generated(hash = 1783595021)
    public ShelfBook(Long id, String name, String path, Long begin, String charset,
                     Integer position) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.begin = begin;
        this.charset = charset;
        this.position = position;
    }

    @Generated(hash = 937254149)
    public ShelfBook() {
    }

    @Override
    public String toString() {
        return "ShelfBook{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", begin=" + begin +
                ", charset='" + charset + '\'' +
                ", position=" + position +
                '}';
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getBegin() {
        return begin;
    }

    public void setBegin(Long begin) {
        this.begin = begin;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 551818233)
    public List<BookCatalogue> getBookCatalogueList() {
        if (bookCatalogueList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BookCatalogueDao targetDao = daoSession.getBookCatalogueDao();
            List<BookCatalogue> bookCatalogueListNew = targetDao
                    ._queryShelfBook_BookCatalogueList(id);
            synchronized (this) {
                if (bookCatalogueList == null) {
                    bookCatalogueList = bookCatalogueListNew;
                }
            }
        }
        return bookCatalogueList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 566957170)
    public synchronized void resetBookCatalogueList() {
        bookCatalogueList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 680168019)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getShelfBookDao() : null;
    }
}












































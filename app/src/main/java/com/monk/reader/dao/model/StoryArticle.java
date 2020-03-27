package com.monk.reader.dao.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by heyao on 8/6/17.
 */
@Entity
public class StoryArticle implements Parcelable {
    /**
     * id : 2921
     * isNewRecord : false
     * createDate : 2017-08-05 16:34:42
     * updateDate : 2017-07-29 14:56:24
     * fromPageNum : 0
     * toPageNum : 0
     * artName : 大清巨鳄
     * artCode : http://www.leduwo.com/bookinfo/86/86585.htm
     * artAuth : 塞外流云
     * artUrl : http://www.leduwo.com/bookinfo/86/86585.htm
     * newChapterName : 第六百七十七章 宝山靶场
     * newChapter : http://www.leduwo.com/book/86/86585/
     * readUrl : http://www.leduwo.com/book/86/86585/
     * cateCode : http://www.leduwo.com/ldw_lastupdate/1.htm
     * artStat : 连载中
     * resName : 乐读窝
     * lastUpdate : 17-07-08
     * isFetch : 1
     * cateName : 最近更新
     */
    @Id(autoincrement = true)
    private Long _id;
    @Unique
    private String id;
    private boolean isNewRecord;
    private String createDate;
    private String updateDate;
    private int fromPageNum;
    private int toPageNum;
    private String artName;
    private String artCode;
    private String artAuth;
    private String artUrl;
    private String newChapterName;
    private String newChapter;
    private String readUrl;
    private String cateCode;
    private String artStat;
    private String resName;
    private String lastUpdate;
    private String isFetch;
    private String cateName;
    private int readCount; // 打开次数

    public StoryArticle() {

    }

    protected StoryArticle(Parcel in) {
        id = in.readString();
        isNewRecord = in.readByte() != 0;
        createDate = in.readString();
        updateDate = in.readString();
        fromPageNum = in.readInt();
        toPageNum = in.readInt();
        artName = in.readString();
        artCode = in.readString();
        artAuth = in.readString();
        artUrl = in.readString();
        newChapterName = in.readString();
        newChapter = in.readString();
        readUrl = in.readString();
        cateCode = in.readString();
        artStat = in.readString();
        resName = in.readString();
        lastUpdate = in.readString();
        isFetch = in.readString();
        cateName = in.readString();
    }

    @Generated(hash = 995774516)
    public StoryArticle(Long _id, String id, boolean isNewRecord, String createDate, String updateDate,
                        int fromPageNum, int toPageNum, String artName, String artCode, String artAuth, String artUrl,
                        String newChapterName, String newChapter, String readUrl, String cateCode, String artStat,
                        String resName, String lastUpdate, String isFetch, String cateName, int readCount) {
        this._id = _id;
        this.id = id;
        this.isNewRecord = isNewRecord;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.fromPageNum = fromPageNum;
        this.toPageNum = toPageNum;
        this.artName = artName;
        this.artCode = artCode;
        this.artAuth = artAuth;
        this.artUrl = artUrl;
        this.newChapterName = newChapterName;
        this.newChapter = newChapter;
        this.readUrl = readUrl;
        this.cateCode = cateCode;
        this.artStat = artStat;
        this.resName = resName;
        this.lastUpdate = lastUpdate;
        this.isFetch = isFetch;
        this.cateName = cateName;
        this.readCount = readCount;
    }

    public static final Creator<StoryArticle> CREATOR = new Creator<StoryArticle>() {
        @Override
        public StoryArticle createFromParcel(Parcel in) {
            return new StoryArticle(in);
        }

        @Override
        public StoryArticle[] newArray(int size) {
            return new StoryArticle[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isNewRecord() {
        return isNewRecord;
    }

    public void setNewRecord(boolean newRecord) {
        isNewRecord = newRecord;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getFromPageNum() {
        return fromPageNum;
    }

    public void setFromPageNum(int fromPageNum) {
        this.fromPageNum = fromPageNum;
    }

    public int getToPageNum() {
        return toPageNum;
    }

    public void setToPageNum(int toPageNum) {
        this.toPageNum = toPageNum;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getArtCode() {
        return artCode;
    }

    public void setArtCode(String artCode) {
        this.artCode = artCode;
    }

    public String getArtAuth() {
        return artAuth;
    }

    public void setArtAuth(String artAuth) {
        this.artAuth = artAuth;
    }

    public String getArtUrl() {
        return artUrl;
    }

    public void setArtUrl(String artUrl) {
        this.artUrl = artUrl;
    }

    public String getNewChapterName() {
        return newChapterName;
    }

    public void setNewChapterName(String newChapterName) {
        this.newChapterName = newChapterName;
    }

    public String getNewChapter() {
        return newChapter;
    }

    public void setNewChapter(String newChapter) {
        this.newChapter = newChapter;
    }

    public String getReadUrl() {
        return readUrl;
    }

    public void setReadUrl(String readUrl) {
        this.readUrl = readUrl;
    }

    public String getCateCode() {
        return cateCode;
    }

    public void setCateCode(String cateCode) {
        this.cateCode = cateCode;
    }

    public String getArtStat() {
        return artStat;
    }

    public void setArtStat(String artStat) {
        this.artStat = artStat;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getIsFetch() {
        return isFetch;
    }

    public void setIsFetch(String isFetch) {
        this.isFetch = isFetch;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeByte((byte) (isNewRecord ? 1 : 0));
        parcel.writeString(createDate);
        parcel.writeString(updateDate);
        parcel.writeInt(fromPageNum);
        parcel.writeInt(toPageNum);
        parcel.writeString(artName);
        parcel.writeString(artCode);
        parcel.writeString(artAuth);
        parcel.writeString(artUrl);
        parcel.writeString(newChapterName);
        parcel.writeString(newChapter);
        parcel.writeString(readUrl);
        parcel.writeString(cateCode);
        parcel.writeString(artStat);
        parcel.writeString(resName);
        parcel.writeString(lastUpdate);
        parcel.writeString(isFetch);
        parcel.writeString(cateName);
    }

    public boolean getIsNewRecord() {
        return this.isNewRecord;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
    }

    public int getReadCount() {
        return this.readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }
}

package com.monk.reader.dao.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by heyao on 8/11/17.
 */

public class Catalog implements Parcelable {

    /**
     * id : 16851
     * isNewRecord : false
     * createDate : 2017-07-29 16:02:59
     * updateDate : 2017-07-29 16:02:59
     * fromPageNum : 0
     * toPageNum : 0
     * chapName : 第一章 十三行
     * chapUrl : http://www.leduwo.com/book/86/86585/19805573.html
     * chapCode : http://www.leduwo.com/book/86/86585/19805573.html
     * chapSeq : 1
     * artCode : http://www.leduwo.com/bookinfo/86/86585.htm
     * isFetch : 0
     */

    private String id;
    private boolean isNewRecord;
    private String createDate;
    private String updateDate;
    private int fromPageNum;
    private int toPageNum;
    private String chapName;
    private String chapUrl;
    private String chapCode;
    private String chapSeq;
    private String artCode;
    private String isFetch;

    protected Catalog(Parcel in) {
        id = in.readString();
        isNewRecord = in.readByte() != 0;
        createDate = in.readString();
        updateDate = in.readString();
        fromPageNum = in.readInt();
        toPageNum = in.readInt();
        chapName = in.readString();
        chapUrl = in.readString();
        chapCode = in.readString();
        chapSeq = in.readString();
        artCode = in.readString();
        isFetch = in.readString();
    }

    public static final Creator<Catalog> CREATOR = new Creator<Catalog>() {
        @Override
        public Catalog createFromParcel(Parcel in) {
            return new Catalog(in);
        }

        @Override
        public Catalog[] newArray(int size) {
            return new Catalog[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsNewRecord() {
        return isNewRecord;
    }

    public void setIsNewRecord(boolean isNewRecord) {
        this.isNewRecord = isNewRecord;
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

    public String getChapName() {
        return chapName;
    }

    public void setChapName(String chapName) {
        this.chapName = chapName;
    }

    public String getChapUrl() {
        return chapUrl;
    }

    public void setChapUrl(String chapUrl) {
        this.chapUrl = chapUrl;
    }

    public String getChapCode() {
        return chapCode;
    }

    public void setChapCode(String chapCode) {
        this.chapCode = chapCode;
    }

    public String getChapSeq() {
        return chapSeq;
    }

    public void setChapSeq(String chapSeq) {
        this.chapSeq = chapSeq;
    }

    public String getArtCode() {
        return artCode;
    }

    public void setArtCode(String artCode) {
        this.artCode = artCode;
    }

    public String getIsFetch() {
        return isFetch;
    }

    public void setIsFetch(String isFetch) {
        this.isFetch = isFetch;
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
        parcel.writeString(chapName);
        parcel.writeString(chapUrl);
        parcel.writeString(chapCode);
        parcel.writeString(chapSeq);
        parcel.writeString(artCode);
        parcel.writeString(isFetch);
    }
}

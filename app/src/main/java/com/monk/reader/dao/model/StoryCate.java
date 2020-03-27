package com.monk.reader.dao.model;

/**
 * Created by heyao on 8/6/17.
 */

public class StoryCate {
    /**
     * id : 40
     * isNewRecord : false
     * createDate : 2017-08-06 13:38:37
     * updateDate : 2017-08-06 12:41:10
     * fromPageNum : 0
     * toPageNum : 0
     * cateCode : http://www.leduwo.com/ldw_lastupdate/1.htm
     * cateName : 最近更新
     * cateUrl : http://www.leduwo.com/ldw_lastupdate/1.htm
     * resId : 2
     * resName : 乐读窝小说网
     * channelCode : c_002
     * isFetch : 1
     * channelName : 女频
     */
    private String id;
    private boolean isNewRecord;
    private String createDate;
    private String updateDate;
    private int fromPageNum;
    private int toPageNum;
    private String cateCode;
    private String cateName;
    private String cateUrl;
    private String resId;
    private String resName;
    private String channelCode;
    private String isFetch;
    private String channelName;

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

    public String getCateCode() {
        return cateCode;
    }

    public void setCateCode(String cateCode) {
        this.cateCode = cateCode;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getCateUrl() {
        return cateUrl;
    }

    public void setCateUrl(String cateUrl) {
        this.cateUrl = cateUrl;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getIsFetch() {
        return isFetch;
    }

    public void setIsFetch(String isFetch) {
        this.isFetch = isFetch;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}

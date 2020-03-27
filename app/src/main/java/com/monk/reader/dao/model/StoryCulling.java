package com.monk.reader.dao.model;

/**
 * Created by heyao on 8/6/17.
 */

public class StoryCulling {

    private String id;
    private boolean isNewRecord;
    private String remarks;
    private String createDate;
    private String updateDate;
    private int fromPageNum;
    private int toPageNum;
    private String cullingDesc;
    private String channelCode;
    private String channelName;
    private String cateCode;
    private String artCode;
    private String cateName;
    private String artName;
    private String image;
    private String imageSrc;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    public String getCullingDesc() {
        return cullingDesc;
    }

    public void setCullingDesc(String cullingDesc) {
        this.cullingDesc = cullingDesc;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getCateCode() {
        return cateCode;
    }

    public void setCateCode(String cateCode) {
        this.cateCode = cateCode;
    }

    public String getArtCode() {
        return artCode;
    }

    public void setArtCode(String artCode) {
        this.artCode = artCode;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
}

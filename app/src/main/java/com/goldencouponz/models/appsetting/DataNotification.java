package com.goldencouponz.models.appsetting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataNotification {
    @SerializedName("group_type")
    @Expose
    private String groupType;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title_ar")
    @Expose
    private String titleAr;
    @SerializedName("title_en")
    @Expose
    private String titleEn;
    @SerializedName("details_ar")
    @Expose
    private String detailsAr;
    @SerializedName("details_en")
    @Expose
    private String detailsEn;
    @SerializedName("url")
    @Expose
    private String url;

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getDetailsAr() {
        return detailsAr;
    }

    public void setDetailsAr(String detailsAr) {
        this.detailsAr = detailsAr;
    }

    public String getDetailsEn() {
        return detailsEn;
    }

    public void setDetailsEn(String detailsEn) {
        this.detailsEn = detailsEn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

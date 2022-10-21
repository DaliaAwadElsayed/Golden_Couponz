package com.goldencouponz.models.appsetting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AboutApp{
    @SerializedName("title_ar")
    @Expose
    private String titleAr;
    @SerializedName("details_ar")
    @Expose
    private String detailsAr;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("title_en")
    @Expose
    private String titleEn;
    @SerializedName("details_en")
    @Expose
    private String detailsEn;
    @SerializedName("topics")
    @Expose
    private List<AboutApp> topics = null;

    public List<AboutApp> getTopics() {
        return topics;
    }

    public void setTopics(List<AboutApp> topics) {
        this.topics = topics;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getDetailsAr() {
        return detailsAr;
    }

    public void setDetailsAr(String detailsAr) {
        this.detailsAr = detailsAr;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getDetailsEn() {
        return detailsEn;
    }

    public void setDetailsEn(String detailsEn) {
        this.detailsEn = detailsEn;
    }
}

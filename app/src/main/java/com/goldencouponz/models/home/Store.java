package com.goldencouponz.models.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Store {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("store_link")
    @Expose
    private String storeLink;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("store_coupons_count")
    @Expose
    private Integer storeCouponsCount;
    @SerializedName("is_favorite")
    @Expose
    private Integer isFavorite;
    @SerializedName("store_sliders")
    @Expose
    private List<Slider> storeSliders = null;
    @SerializedName("store_categories")
    @Expose
    private List<StoreCategory> storeCategories = null;
    @SerializedName("store_coupons")
    @Expose
    private List<StoreCategory> store_coupons = null;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStoreLink() {
        return storeLink;
    }

    public void setStoreLink(String storeLink) {
        this.storeLink = storeLink;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getStoreCouponsCount() {
        return storeCouponsCount;
    }

    public void setStoreCouponsCount(Integer storeCouponsCount) {
        this.storeCouponsCount = storeCouponsCount;
    }

    public Integer getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Integer isFavorite) {
        this.isFavorite = isFavorite;
    }

    public List<Slider> getStoreSliders() {
        return storeSliders;
    }

    public void setStoreSliders(List<Slider> storeSliders) {
        this.storeSliders = storeSliders;
    }

    public List<StoreCategory> getStoreCategories() {
        return storeCategories;
    }

    public void setStoreCategories(List<StoreCategory> storeCategories) {
        this.storeCategories = storeCategories;
    }
}

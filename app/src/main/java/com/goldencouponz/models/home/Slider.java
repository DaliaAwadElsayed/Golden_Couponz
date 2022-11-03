package com.goldencouponz.models.home;

import com.goldencouponz.models.store.StoreCoupons;
import com.goldencouponz.models.store.StoreProduct;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Slider {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("store_id")
    @Expose
    private Integer storeId;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("store_link")
    @Expose
    private String storeLink;
    @SerializedName("is_favorite")
    @Expose
    private Integer isFavorite;
    @SerializedName("store_coupons")
    @Expose
    private List<StoreCoupons> storeCoupons = null;
    @SerializedName("store_products")
    @Expose
    private List<StoreProduct> storeProducts = null;
    @SerializedName("store_sliders")
    @Expose
    private List<Slider> storeSliders = null;
    @SerializedName("store_categories")
    @Expose
    private List<Category> storeCategories = null;

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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getStoreLink() {
        return storeLink;
    }

    public void setStoreLink(String storeLink) {
        this.storeLink = storeLink;
    }

    public Integer getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Integer isFavorite) {
        this.isFavorite = isFavorite;
    }

    public List<StoreCoupons> getStoreCoupons() {
        return storeCoupons;
    }

    public void setStoreCoupons(List<StoreCoupons> storeCoupons) {
        this.storeCoupons = storeCoupons;
    }

    public List<StoreProduct> getStoreProducts() {
        return storeProducts;
    }

    public void setStoreProducts(List<StoreProduct> storeProducts) {
        this.storeProducts = storeProducts;
    }

    public List<Slider> getStoreSliders() {
        return storeSliders;
    }

    public void setStoreSliders(List<Slider> storeSliders) {
        this.storeSliders = storeSliders;
    }

    public List<Category> getStoreCategories() {
        return storeCategories;
    }

    public void setStoreCategories(List<Category> storeCategories) {
        this.storeCategories = storeCategories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

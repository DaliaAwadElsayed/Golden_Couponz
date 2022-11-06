package com.goldencouponz.models.store;

import com.goldencouponz.models.home.Category;
import com.goldencouponz.models.home.Store;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreProduct {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("store_id")
    @Expose
    private Integer storeId;
    @SerializedName("brand_id")
    @Expose
    private Integer brandId;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("sub_category_id")
    @Expose
    private Integer subCategoryId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("product_link")
    @Expose
    private String productLink;
    @SerializedName("coupon")
    @Expose
    private String coupon;
    @SerializedName("video_link")
    @Expose
    private String videoLink;
    @SerializedName("expire_date")
    @Expose
    private String expireDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("product_country")
    @Expose
    private ProductCountry productCountry;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("sub_category")
    @Expose
    private Category subCategory;
    @SerializedName("store")
    @Expose
    private Store store;
    @SerializedName("data")
    @Expose
    private List<StoreProduct> data = null;
    public Integer getId() {
        return id;
    }

    public Store getStore() {
        return store;
    }

    public List<StoreProduct> getData() {
        return data;
    }

    public void setData(List<StoreProduct> data) {
        this.data = data;
    }

    public void setStore(Store store) {
        this.store = store;
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

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
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

    public String getProductLink() {
        return productLink;
    }

    public void setProductLink(String productLink) {
        this.productLink = productLink;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public ProductCountry getProductCountry() {
        return productCountry;
    }

    public void setProductCountry(ProductCountry productCountry) {
        this.productCountry = productCountry;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Category subCategory) {
        this.subCategory = subCategory;
    }
}

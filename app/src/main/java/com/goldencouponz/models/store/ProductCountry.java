package com.goldencouponz.models.store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCountry {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("store_product_id")
    @Expose
    private Integer storeProductId;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("discount_price")
    @Expose
    private String discountPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStoreProductId() {
        return storeProductId;
    }

    public void setStoreProductId(Integer storeProductId) {
        this.storeProductId = storeProductId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }
}

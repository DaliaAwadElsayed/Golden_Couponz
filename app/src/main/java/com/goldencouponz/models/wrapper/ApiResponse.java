package com.goldencouponz.models.wrapper;


import com.goldencouponz.models.appsetting.Country;
import com.goldencouponz.models.appsetting.Notification;
import com.goldencouponz.models.home.Category;
import com.goldencouponz.models.home.Slider;
import com.goldencouponz.models.home.Store;
import com.goldencouponz.models.store.Brand;
import com.goldencouponz.models.store.StoreCoupons;
import com.goldencouponz.models.store.StoreProduct;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("countries")
    @Expose
    private List<Country> countries = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("profile_photo_url")
    @Expose
    private String profilePhotoUrl;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("sliders")
    @Expose
    private List<Slider> sliders = null;
    @SerializedName("stores")
    @Expose
    private List<Store> stores = null;
    @SerializedName("store")
    @Expose
    private Store store = null;
    @SerializedName("products")
    @Expose
    private StoreProduct products;
    @SerializedName("product")
    @Expose
    private StoreProduct singleProduct;
    @SerializedName("coupons")
    @Expose
    private List<StoreCoupons> coupons = null;
    @SerializedName("coupon")
    @Expose
    private StoreCoupons coupon = null;
    @SerializedName("notifications")
    @Expose
    private List<Notification> notifications = null;
    @SerializedName("brands")
    @Expose
    private List<Brand> brands = null;

    public ApiResponse() {
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public StoreProduct getSingleProduct() {
        return singleProduct;
    }

    public void setSingleProduct(StoreProduct singleProduct) {
        this.singleProduct = singleProduct;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<StoreCoupons> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<StoreCoupons> coupons) {
        this.coupons = coupons;
    }

    public StoreProduct getProducts() {
        return products;
    }

    public void setProducts(StoreProduct products) {
        this.products = products;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

    public List<Slider> getSliders() {
        return sliders;
    }

    public void setSliders(List<Slider> sliders) {
        this.sliders = sliders;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public StoreCoupons getCoupon() {
        return coupon;
    }

    public void setCoupon(StoreCoupons coupon) {
        this.coupon = coupon;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }
}


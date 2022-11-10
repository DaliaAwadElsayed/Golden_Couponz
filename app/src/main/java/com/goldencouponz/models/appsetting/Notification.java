package com.goldencouponz.models.appsetting;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("read_at")
    @Expose
    private String readAt;
    @SerializedName("data")
    @Expose
    private DataNotification data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReadAt() {
        return readAt;
    }

    public void setReadAt(String readAt) {
        this.readAt = readAt;
    }

    public DataNotification getData() {
        return data;
    }

    public void setData(DataNotification data) {
        this.data = data;
    }
}

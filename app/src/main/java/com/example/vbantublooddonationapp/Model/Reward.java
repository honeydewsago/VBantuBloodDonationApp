package com.example.vbantublooddonationapp.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "reward_table")
public class Reward {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rewardID")
    public int rewardID;

    @ColumnInfo(name = "discount")
    public String discount;

    @ColumnInfo(name = "minSpend")
    public int minSpend;

    @ColumnInfo(name = "storeName")
    public String storeName;

    @ColumnInfo(name = "quantity")
    public int quantity;

    @ColumnInfo(name = "points")
    public int points;

    @ColumnInfo(name = "status")
    public String status;

    public Reward(String discount, int minSpend, String storeName, int quantity, int points, String status) {
        this.discount = discount;
        this.minSpend = minSpend;
        this.storeName = storeName;
        this.quantity = quantity;
        this.points = points;
        this.status = status;
    }

    public int getRewardID() {
        return rewardID;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getMinSpend() {
        return minSpend;
    }

    public void setMinSpend(int minSpend) {
        this.minSpend = minSpend;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

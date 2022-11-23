package com.example.vbantublooddonationapp.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reward_transaction_table")
public class RewardTransaction {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "reward_transID")
    public int reward_transID;

    @ColumnInfo(name = "rewardID")
    public int rewardID;

    @ColumnInfo(name = "userID")
    public int userID;

    @ColumnInfo(name = "claimDate")
    public String claimDate;

    @ColumnInfo(name = "expiryDate")
    public String expiryDate;

    @ColumnInfo(name = "redeemDate")
    public String redeemDate;

    @ColumnInfo(name = "status")
    public String status;

    public RewardTransaction(int rewardID, int userID, String claimDate, String expiryDate, String redeemDate, String status) {
        this.rewardID = rewardID;
        this.userID = userID;
        this.claimDate = claimDate;
        this.expiryDate = expiryDate;
        this.redeemDate = redeemDate;
        this.status = status;
    }

    public int getReward_transID() {
        return reward_transID;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public String getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(String redeemDate) {
        this.redeemDate = redeemDate;
    }

    public int getRewardID() {
        return rewardID;
    }

    public void setRewardID(int rewardID) {
        this.rewardID = rewardID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

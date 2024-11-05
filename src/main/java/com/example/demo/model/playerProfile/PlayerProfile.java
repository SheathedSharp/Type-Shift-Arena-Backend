package com.example.demo.model.playerProfile;

import com.example.demo.model.User;
import jakarta.persistence.*;

@Entity
@Table(name = "player_profile")
@Inheritance(strategy = InheritanceType.JOINED) // 使用JOINED策略
public class PlayerProfile extends User {

    @Column(name = "img_src")  // 映射数据库表的列名
    private String imgSrc;      // 头像

    @Column(name = "user_level") // 映射数据库表的列名
    private String userLevel;    // 用户等级

    @Column(name = "victories_number") // 映射数据库表的列名
    private int victoriesNumber;  // 胜利场数

    @Column(name = "speed")      // 映射数据库表的列名
    private double speed;        // 均速

    @Column(name = "win_rate")   // 映射数据库表的列名
    private double winRate;      // 胜率

    @Column(name = "accuracy_rate") // 映射数据库表的列名
    private double accuracyRate;  // 准确率

    // Getter和Setter方法

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public int getVictoriesNumber() {
        return victoriesNumber;
    }

    public void setVictoriesNumber(int victoriesNumber) {
        this.victoriesNumber = victoriesNumber;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    public double getAccuracyRate() {
        return accuracyRate;
    }

    public void setAccuracyRate(double accuracyRate) {
        this.accuracyRate = accuracyRate;
    }
}




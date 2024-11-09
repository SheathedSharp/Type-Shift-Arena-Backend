package com.example.demo.model.user;

import com.example.demo.model.User;
import jakarta.persistence.*;

@Entity
@Table(name = "player_profile")
public class PlayerProfile {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "userLevel") 
    private String userLevel = "无等级";    // 用户等级（段位）

    @Column(name = "win") 
    private int win;  // 胜利场数

    @Column(name = "speed")      
    private double speed;        // 均速

    @Column(name = "winRate") 
    private double winRate;      // 胜率

    @Column(name = "accuracyRate")
    private double accuracyRate;  // 准确率

    @Column(name = "rankScore")
    private int rankScore = 500; // 初始设置为500分

    @Column(name = "matchesPlayed")
    private int matchesPlayed = 0; // 总比赛场数

    // Getter和Setter方法
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public int getwin() {
        return win;
    }

    public void setwin(int win) {
        this.win = win;
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

    public int getRankScore() {
        return rankScore;
    }

    public void setRankScore(int rankScore) {
        this.rankScore = rankScore;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    // 更新统计数据的方法
    public void updateStats(double speed, boolean isVictory, double accuracy) {
        this.matchesPlayed++;
        if (isVictory) {
            this.win++;
        }
        
        // 更新平均速度
        this.speed = ((this.speed * (this.matchesPlayed - 1)) + speed) / this.matchesPlayed;
        
        // 更新胜率
        this.winRate = (double) this.win / this.matchesPlayed * 100;
        
        // 更新准确率
        this.accuracyRate = ((this.accuracyRate * (this.matchesPlayed - 1)) + accuracy) / this.matchesPlayed;
        
        // 更新段位积分 (示例：胜利+25，失败-20)
        this.rankScore += isVictory ? 100 : -50;
        
        // 更新等级 (示例：基于段位积分)
        updateUserLevel();
    }

    private void updateUserLevel() {
        if (rankScore < 1000) userLevel = "倔强青铜";
        else if (rankScore < 1500) userLevel = "不屈白银";
        else if (rankScore < 2000) userLevel = "荣耀黄金";
        else if (rankScore < 2500) userLevel = "华贵铂金";
        else if (rankScore < 3000) userLevel = "耀眼翡翠"; 
        else if (rankScore < 3500) userLevel = "璀璨钻石";
        else if (rankScore < 4000) userLevel = "超凡大师";
        else userLevel = "最强王者";
    }
}




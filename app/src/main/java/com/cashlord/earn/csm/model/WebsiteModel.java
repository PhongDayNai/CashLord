package com.cashlord.earn.csm.model;

import java.io.Serializable;

public class WebsiteModel implements Serializable {
    public String id;
    public String isVisitEnable;
    public String visitTitle;
    public String visitLink;
    public String visitCoin;
    public String visitTimer;
    public String browser;
    public String description;
    public String logo;
    public String packages;

    // Constructor
    public WebsiteModel(String id, String isVisitEnable, String visitTitle, String visitLink,
                        String visitCoin, String visitTimer, String browser,
                        String description, String logo, String packages) {
        this.id = id;
        this.isVisitEnable = isVisitEnable;
        this.visitTitle = visitTitle;
        this.visitLink = visitLink;
        this.visitCoin = visitCoin;
        this.visitTimer = visitTimer;
        this.browser = browser;
        this.description = description;
        this.logo = logo;
        this.packages = packages;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsVisitEnable() {
        return isVisitEnable;
    }

    public void setIsVisitEnable(String isVisitEnable) {
        this.isVisitEnable = isVisitEnable;
    }

    public String getVisitTitle() {
        return visitTitle;
    }

    public void setVisitTitle(String visitTitle) {
        this.visitTitle = visitTitle;
    }

    public String getVisitLink() {
        return visitLink;
    }

    public void setVisitLink(String visitLink) {
        this.visitLink = visitLink;
    }

    public String getVisitCoin() {
        return visitCoin;
    }

    public void setVisitCoin(String visitCoin) {
        this.visitCoin = visitCoin;
    }

    public String getVisitTimer() {
        return visitTimer;
    }

    public void setVisitTimer(String visitTimer) {
        this.visitTimer = visitTimer;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }
}

package com.comps413f.gym;

public class Action {
    private String actionName;
    private String days;
    private String description;
    private String haveImage;
    private String organs;
    private String references;
    private String times;
    private String usage;
    private String zActionID;
    private String haveChecked;


    public Action() {
    }

    public Action(String actionName, String days, String description,String haveImage, String organs, String references, String times, String usage, String zActionID,String haveChecked) {
        this.actionName = actionName;
        this.days = days;
        this.description = description;
        this.haveImage = haveImage;
        this.organs = organs;
        this.references = references;
        this.times = times;
        this.usage = usage;
        this.zActionID = zActionID;
        this.haveChecked = haveChecked;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHaveImage() {
        return haveImage;
    }

    public void setHaveImage(String haveImage) {
        this.haveImage = haveImage;
    }

    public String getOrgans() {
        return organs;
    }

    public void setOrgans(String organs) {
        this.organs = organs;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getzActionID() {
        return zActionID;
    }

    public void setzActionID(String zActionID) {
        this.zActionID = zActionID;
    }

    public String getHaveChecked() {
        return haveChecked;
    }

    public void setHaveChecked(String haveChecked) {
        this.haveChecked = haveChecked;
    }
}

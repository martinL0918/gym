package com.comps413f.gym;

public class Action {
    private String name;
    private String description;
    private String organs;
    private String times;
    private String usage;
    private String references;
    private String days;

    private Action(){
        super();
    }
    public Action(String name, String description, String organs, String times, String usage, String references, String days) {
        this.name = name;
        this.description = description;
        this.organs = organs;
        this.times = times;
        this.usage = usage;
        this.references = references;
        this.days = days;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrgans() {
        return organs;
    }

    public void setOrgans(String organs) {
        this.organs = organs;
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

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}

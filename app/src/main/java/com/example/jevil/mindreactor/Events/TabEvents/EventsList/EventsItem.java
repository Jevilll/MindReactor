package com.example.jevil.mindreactor.Events.TabEvents.EventsList;

/**
 * Created by Jevil on 02.10.2016.
 */

public class EventsItem {
    private String name, benefit, type;
    private int mark;
    private int bdItemID;
    public static EventsItem selectedItem;

    public EventsItem(String name, int mark, int bdItemID, String benefit, String type) {
        this.name = name;
        this.mark = mark;
        this.bdItemID = bdItemID;
        this.benefit = benefit;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getMark() {
        return mark;
    }

    public int getBDitemID() {
        return bdItemID;
    }

    public void setName(String val) {
        name = val;
    }

    public void setMark(int val) {
        mark = val;
    }

    public void setBDitemID(int val) {
        bdItemID = val;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }
    public String getType(){
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
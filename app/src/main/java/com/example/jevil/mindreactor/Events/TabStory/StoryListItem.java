package com.example.jevil.mindreactor.Events.TabStory;

public class StoryListItem {
    private String name, type, benefit;
    private int bdItemID, mark, minutes;
    private long time;
    private float marks;
    public static StoryListItem selectedItem;
    private String firstRecord;

    StoryListItem(String name, int mark, int bdItemID, String type, int minutes, long time, float marks, String benefit, String firstRecord) {
        this.name = name;
        this.mark = mark;
        this.bdItemID = bdItemID;
        this.type = type;
        this.minutes = minutes;
        this.time = time;
        this.marks = marks;
        this.benefit = benefit;
        this.firstRecord = firstRecord;
    }

    String getFirstRecord() {
        return firstRecord;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getMark() {
        return mark;
    }

    public int getBDitemID() {
        return bdItemID;
    }

    public int getMinutes() {
        return minutes;
    }

    public long getTime() {return time;}

    public String getBenefit(){return benefit;}

    float getMakrs() {
        return marks;
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
}
package com.example.jevil.mindreactor.Statistic.ExpandedStatistic;


/**
 * Created by Jevil on 05.11.2016.
 */

public class StatisticListItem {
    private String name, type, benefit;
    private int bdItemID, mark, minutes;
    private long time;
    private float marks;

    public StatisticListItem(String name, int mark, String type, int minutes, float marks, String benefit) {
        this.name = name;
        this.mark = mark;
        this.type = type;
        this.minutes = minutes;
        this.marks = marks;
        this.benefit = benefit;
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
//    public long getNominalTime() {return nominalTime;}

    public String getBenefit(){return benefit;}

    public float getMakrs() {
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
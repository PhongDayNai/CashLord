package com.cashlord.earn.helper;

public class Model {
    String desc, date, coin, time;

    public Model() {}

    public Model(String desc, String date, String coin, String time) {
        this.desc = desc;
        this.date = date;
        this.coin = coin;
        this.time = time;
    }

    public String getDesc() { return desc; }
    public String getDate() { return date; }
    public String getCoin() { return coin; }
    public String getTime() { return time; }
}

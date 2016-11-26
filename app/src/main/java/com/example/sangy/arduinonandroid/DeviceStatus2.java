package com.example.sangy.arduinonandroid;

/**
 * Created by Yung Lee on 2016-11-20.
 */

public class DeviceStatus2 {
    private int bright_sta;
    private int fall;
    private int status;
    private int status_change;
    private int bright_set;
    private int connection_cycle;

    public int getBright_sta() {
        return bright_sta;
    }

    public void setBright_sta(int bright_sta) {
        this.bright_sta = bright_sta;
    }

    public int getFall() {
        return fall;
    }

    public void setFall(int fall) {
        this.fall = fall;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus_change() {
        return status_change;
    }

    public void setStatus_change(int status_change) {
        this.status_change = status_change;
    }

    public int getBright_set() {
        return bright_set;
    }

    public void setBright_set(int bright_set) {
        this.bright_set = bright_set;
    }

    public int getConnection_cycle() {
        return connection_cycle;
    }

    public void setConnection_cycle(int connection_cycle) {
        this.connection_cycle = connection_cycle;
    }
}
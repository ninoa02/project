package com.example.sangy.arduinonandroid;

/**
 * Created by Yung Lee on 2016-11-20.
 */

public class DeviceStatus {
    final static String SERVER = "http://192.168.0.134:8080/candle/";
    private static int andCycle = 1000;
    private static int device_no;
    private static int bright_sta;
    private static int fall;
    private static int status;
    private static int status_change;
    private static int bright_set;
    private static int connection_cycle;
    private static int alarm_set;
    private static String alarm_start;
    private static String alarm_end;

    public static int getAndCycle() {
        return andCycle;
    }

    public static void setAndCycle(int andCycle) {
        DeviceStatus.andCycle = andCycle;
    }

    public static int getAlarm_set() {
        return alarm_set;
    }

    public static void setAlarm_set(int alarm_set) {
        DeviceStatus.alarm_set = alarm_set;
    }

    public static String getAlarm_start() {
        return alarm_start;
    }

    public static void setAlarm_start(String alarm_start) {
        DeviceStatus.alarm_start = alarm_start;
    }

    public static String getAlarm_end() {
        return alarm_end;
    }

    public static void setAlarm_end(String alarm_end) {
        DeviceStatus.alarm_end = alarm_end;
    }

    public static int getDevice_no() {
        return device_no;
    }

    public static void setDevice_no(int device_no) {
        DeviceStatus.device_no = device_no;
    }

    public static int getBright_sta() {
        return bright_sta;
    }

    public static void setBright_sta(int bright_sta) {
        DeviceStatus.bright_sta = bright_sta;
    }

    public static int getFall() {
        return fall;
    }

    public static void setFall(int fall) {
        DeviceStatus.fall = fall;
    }

    public static int getStatus() {
        return status;
    }

    public static void setStatus(int status) {
        DeviceStatus.status = status;
    }

    public static int getStatus_change() {
        return status_change;
    }

    public static void setStatus_change(int status_change) {
        DeviceStatus.status_change = status_change;
    }

    public static int getBright_set() {
        return bright_set;
    }

    public static void setBright_set(int bright_set) {
        DeviceStatus.bright_set = bright_set;
    }

    public static int getConnection_cycle() {
        return connection_cycle;
    }

    public static void setConnection_cycle(int connection_cycle) {
        DeviceStatus.connection_cycle = connection_cycle;
    }
}
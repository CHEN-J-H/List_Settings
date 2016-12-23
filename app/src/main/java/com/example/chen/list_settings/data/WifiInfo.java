package com.example.chen.list_settings.data;

/**
 * Created by Chen on 2016/12/21.
 */

public class WifiInfo {
    private String BSSID;
    private String SSID;
    private String capabilities;
    private int level;

    public WifiInfo(String BSSID, String SSID, String capabilities, int level) {
        this.BSSID = BSSID;
        this.SSID = SSID;
        this.capabilities = capabilities;
        this.level = level;
    }

    @Override
    public String toString() {
        return "WifiInfo{" +
                "BSSID='" + BSSID + '\'' +
                ", SSID='" + SSID + '\'' +
                ", capabilities='" + capabilities + '\'' +
                ", level=" + level +
                '}';
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

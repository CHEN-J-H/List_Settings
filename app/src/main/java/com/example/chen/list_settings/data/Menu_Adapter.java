package com.example.chen.list_settings.data;

/**
 * Created by Chen on 2016/12/19.
 */

public class Menu_Adapter {
    private int mIconDefault;
    private String mTextArray;

    public Menu_Adapter(int mIconDefault, String mTextArray) {
        this.mIconDefault = mIconDefault;
        this.mTextArray = mTextArray;
    }

    @Override
    public String toString() {
        return "Menu_Adapter{" +
                "mIconDefault=" + mIconDefault +
                ", mTextArray='" + mTextArray + '\'' +
                '}';
    }

    public int getmIconDefault() {
        return mIconDefault;
    }

    public void setmIconDefault(int mIconDefault) {
        this.mIconDefault = mIconDefault;
    }

    public String getmTextArray() {
        return mTextArray;
    }

    public void setmTextArray(String mTextArray) {
        this.mTextArray = mTextArray;
    }
}

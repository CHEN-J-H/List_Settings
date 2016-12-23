package com.example.chen.list_settings.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.chen.list_settings.R;
import com.example.chen.list_settings.config.Constants;

import java.lang.reflect.Field;

/**
 * Created by Chen on 2016/12/16.
 */

public class Fragment_About extends Fragment {
    private View 				mFragmentAbout;
    private TextView 			mDeviceMac;
    private TextView 			mDeviceNum;
    private TextView 			mVersionNum;
    private String 				deviceNum 		= "";
    private SharedPreferences mSharePref;
    private Context 			mContext;
    private String 				mVersion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mFragmentAbout == null) {
            mFragmentAbout = inflater.inflate(R.layout.fragment_about,
                    container, false);
            mContext = mFragmentAbout.getContext();



            mDeviceMac = (TextView) mFragmentAbout.findViewById(R.id.device_mac);

            mDeviceNum = (TextView) mFragmentAbout.findViewById(R.id.device_num);

            mVersionNum = (TextView) mFragmentAbout.findViewById(R.id.version_number);

            mDeviceMac.setText(getMacAddress());
            mVersion = getVersionNum();
            if(mVersion!=null){
                mVersionNum.setText(mVersion);
            }else{
                mVersionNum.setText(R.string.getting);
            }
        }
        return mFragmentAbout;
    }



    private String getVersionNum(){//获取版本号
        try{
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            String version = info.versionName;
            return "V" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getMacAddress() {
        WifiManager wifi = (WifiManager) getActivity().getSystemService(
                Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }//获取wifi地址


    public  void setDeviceNum(String value){
        deviceNum = value;
    }

    @Override
    public void onStart() {
        mSharePref = this.getActivity().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        setDeviceNum(mSharePref.getString("deviceNum" , null));
        if(deviceNum != null){
            mDeviceNum.setText(deviceNum);
        }else{
            mDeviceNum.setText(R.string.getting);
        }
        super.onStart();
    }
    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            if(childFragmentManager!=null){
                childFragmentManager.setAccessible(true);
                childFragmentManager.set(this, null);
            }

        } catch (NoSuchFieldException e) {
            //throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            //throw new RuntimeException(e);
        }
    }

}




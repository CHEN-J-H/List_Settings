package com.example.chen.list_settings.adapter;

import android.content.Context;

import android.net.wifi.WifiManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chen.list_settings.R;
import com.example.chen.list_settings.data.WifiInfo;

import java.util.ArrayList;


/**
 * Created by Chen on 2016/12/21.
 */

public class WiFiInfoAdapter extends BaseAdapter {
    private ArrayList<WifiInfo> wifiInfos;
    private Context context;
    private LayoutInflater inflater;
    private int level;

    public WiFiInfoAdapter(Context context,ArrayList<WifiInfo> wifiInfo) {
        this.context=context;
        this.wifiInfos=wifiInfo;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return wifiInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return wifiInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder=null;
       WifiInfo info=wifiInfos.get(position);
        if(convertView!=null&& convertView.getTag() != null){
            holder = (ViewHolder) convertView.getTag();
        }else {
            convertView=inflater.inflate(R.layout.wifi_info_adapter, null);
            holder=new ViewHolder(convertView);
            convertView.getTag();
        }

        ImageView wifi_level = holder.getMenuIcon();
        TextView wifi_ssid = holder.getMenuText();
        TextView wifi_capabilities = holder.getwifi_capabilitiesText();
        wifi_ssid.setText(info.getSSID());

        if(info.getCapabilities().contains("WEP")){
            wifi_capabilities.setText("使用了"+"WEP/WPA"+"方式保护");
        }
        if(info.getCapabilities().contains("PSK")){
            wifi_capabilities.setText("使用了"+"WPA2-PSK"+"方式保护");
        }
        if(info.getCapabilities().contains("EAP")){
            wifi_capabilities.setText("使用了"+"EAP"+"方式保护");
        }
        if(info.getCapabilities().contains("ESS")){
            wifi_capabilities.setText("使用了"+"ESS"+"方式保护");
        }

        level= WifiManager.calculateSignalLevel(info.getLevel(),5);
        if(info.getCapabilities().contains("WEP")||info.getCapabilities().contains("PSK")||
                info.getCapabilities().contains("EAP")){
            wifi_level.setImageResource(R.drawable.wifi_signal_lock);
        }else{
            wifi_level.setImageResource(R.drawable.wifi_signal_open);
        }
        wifi_level.setImageLevel(level);
        //判断信号强度，显示对应的指示图标  
        return convertView;
    }

    public final class ViewHolder {
        public View baseView;
        public ImageView wifi_level;
        public TextView wifi_ssid;
        public TextView wifi_capabilities;

        public ViewHolder(View view) {
            baseView = view;
        }

        public ImageView getMenuIcon() {
            if (wifi_level == null) {
                ImageView imageView = (ImageView) baseView.findViewById(R.id.wifi_level);
                wifi_level = imageView;
            }
            return wifi_level;
        }

        public TextView getMenuText() {
            if (wifi_ssid == null) {
                TextView textView = (TextView) baseView.findViewById(R.id.ssid);
                wifi_ssid = textView;
            }
            return wifi_ssid;
        }

        public TextView getwifi_capabilitiesText() {
            if (wifi_capabilities == null) {
                TextView capabilities= (TextView) baseView.findViewById(R.id.capabilities);
                wifi_capabilities = capabilities;
            }
            return wifi_capabilities;
        }
    }

}

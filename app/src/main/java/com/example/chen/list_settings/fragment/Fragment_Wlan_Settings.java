package com.example.chen.list_settings.fragment;

import android.app.Fragment;
import android.media.Image;
import android.net.wifi.ScanResult;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chen.list_settings.adapter.WiFiInfoAdapter;
import com.example.chen.list_settings.data.WifiInfo;
import com.example.chen.list_settings.dialog.WifiPswDialog;
import  com.example.chen.list_settings.view.SwitchButton;

import com.example.chen.list_settings.R;
import com.example.chen.list_settings.util.WifiAdmin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen on 2016/12/16.
 */

public class Fragment_Wlan_Settings extends Fragment {
    private View fragment_Wlan;
    private ArrayList<WifiInfo> wifiArray;
    private WiFiInfoAdapter wifiInfoAdapter;
    private ListView listWifi;

    private ProgressBar updateProgress;
    private ImageButton updateButton;
    private String wifiPassword = null;

    private WifiManager wifiManager;
    private WifiAdmin wiFiAdmin;
    private List<ScanResult> list;
    private ScanResult mScanResult;
    private StringBuffer sb = new StringBuffer();
    final Handler refreshWifiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    getAllNetWorkList();
                    updateProgress.setVisibility(View.INVISIBLE);
                    updateButton.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        wiFiAdmin = new WifiAdmin(getActivity());
        if (fragment_Wlan == null) {
            fragment_Wlan = inflater.inflate(R.layout.fragment_wifi, container, false);
            listWifi = (ListView) fragment_Wlan.findViewById(R.id.listWifi);
            updateProgress = (ProgressBar) fragment_Wlan.findViewById(R.id.updateProgress);
            updateProgress.setVisibility(View.INVISIBLE);
            updateButton = (ImageButton) fragment_Wlan.findViewById(R.id.updateButton);
            updateButton.setVisibility(View.VISIBLE);
            updateButton.setOnClickListener(new MyOnClickListener());

            SwitchButton switchWifi = (SwitchButton) fragment_Wlan.findViewById(R.id.switchWifi);
            wifiManager = (WifiManager) getActivity().getSystemService(getActivity().WIFI_SERVICE);
            switchWifi.setChecked(wifiManager.isWifiEnabled());
            switchWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    wifiManager.setWifiEnabled(isChecked);
                    // 更新WiFi列表
                    if (isChecked) {
                        listWifi.setVisibility(View.VISIBLE);
                        updateProgress.setVisibility(View.VISIBLE);
                        updateButton.setVisibility(View.INVISIBLE);
                        new Thread(new refreshWifiThread()).start();
                    } else {
                        listWifi.setVisibility(View.GONE);
                    }
                }
            });

        }
        return fragment_Wlan;
    }
    public class refreshWifiThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                Message message = new Message();
                message.what = 1;
                refreshWifiHandler.sendMessage(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.updateButton:
                    updateButton.setVisibility(View.INVISIBLE);
                    updateProgress.setVisibility(View.VISIBLE);
                    new Thread(new refreshWifiThread()).start();
                    break;
                default:
                    break;
            }
        }

    }
    public void getAllNetWorkList() {

        wifiArray = new ArrayList<WifiInfo>();
        if (sb != null) {
            sb = new StringBuffer();
        }
        wiFiAdmin.startScan();
        list = wiFiAdmin.getWifiList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                mScanResult = list.get(i);
                WifiInfo wifiInfo = new WifiInfo(mScanResult.BSSID,
                        mScanResult.SSID, mScanResult.capabilities,
                        mScanResult.level);
                Log.i("chenjh",wifiInfo.toString());
                wifiArray.add(wifiInfo);
            }

            wifiInfoAdapter = new WiFiInfoAdapter(getActivity(),
                    wifiArray);
            listWifi.setAdapter(wifiInfoAdapter);

            //
            wiFiAdmin.getConfiguration();

            listWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                String wifiItemSSID = null;

                public void onItemClick(android.widget.AdapterView<?> parent,
                                        android.view.View view, int position, long id) {



                    // 连接WiFi
                    wifiItemSSID = list.get(position).SSID;
                    int wifiItemId = wiFiAdmin.IsConfiguration("\""
                            + list.get(position).SSID + "\"");
                    if (wifiItemId != -1) {
                        if (wiFiAdmin.ConnectWifi(wifiItemId)) {
                            // 连接已保存密码的WiFi
                            Toast.makeText(getActivity(), "正在连接",
                                    Toast.LENGTH_SHORT).show();
                            updateButton.setVisibility(View.INVISIBLE);
                            updateProgress.setVisibility(View.VISIBLE);
                            new Thread(new refreshWifiThread()).start();
                        }
                    } else {
                        // 没有配置好信息，配置
                        WifiPswDialog pswDialog = new WifiPswDialog(
                                getActivity(),
                                new WifiPswDialog.OnCustomDialogListener() {
                                    @Override
                                    public void back(String str) {
                                        wifiPassword = str;
                                        if (wifiPassword != null) {
                                            int netId = wiFiAdmin
                                                    .AddWifiConfig(list,
                                                            wifiItemSSID,
                                                            wifiPassword);
                                            if (netId != -1) {
                                                wiFiAdmin.getConfiguration();// 添加了配置信息，要重新得到配置信息
                                                if (wiFiAdmin
                                                        .ConnectWifi(netId)) {
                                                    // 连接成功，刷新UI
                                                    Toast.makeText(getActivity(), "连接成功",
                                                            Toast.LENGTH_SHORT).show();
                                                    updateProgress
                                                            .setVisibility(View.VISIBLE);
                                                    updateButton
                                                            .setVisibility(View.INVISIBLE);
                                                    new Thread(
                                                            new refreshWifiThread())
                                                            .start();
                                                }
                                            } else {
                                                // 网络连接错误
                                            }
                                        } else {
                                        }
                                    }
                                });
                        pswDialog.show();
                    }
                }
            });
        }
    }
}

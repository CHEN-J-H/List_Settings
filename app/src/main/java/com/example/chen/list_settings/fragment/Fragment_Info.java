package com.example.chen.list_settings.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chen.list_settings.R;

/**
 * Created by Chen on 2016/12/16.
 */

public class Fragment_Info extends Fragment {
    private  View FragmentInfo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(FragmentInfo==null){
            FragmentInfo=inflater.inflate(R.layout.fragment_info,container,false);
        }
        return FragmentInfo;
    }
}

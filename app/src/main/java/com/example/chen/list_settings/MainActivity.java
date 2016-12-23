package com.example.chen.list_settings;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.chen.list_settings.adapter.MenuAdapter;
import com.example.chen.list_settings.config.Constants;
import com.example.chen.list_settings.data.Menu_Adapter;
import com.example.chen.list_settings.fragment.Fragment_About;
import com.example.chen.list_settings.fragment.Fragment_Info;
import com.example.chen.list_settings.fragment.Fragment_Wlan_Settings;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity {
    private final String 	KEY_LAST_MENU = "last_menu_index";
    private SharedPreferences mSharePref;
    private ListView listView;
    private MenuAdapter mMenuAdapter;
    private List<Menu_Adapter> Menus;
    private Fragment mInfoFragment = null;
    private Fragment mWiFiFragment = null;
    private Fragment mAboutFragment = null;
    public int			 		mMenuIndex 				= 0;

    private int mCurrentHighlight = 0;

    public boolean 		mIsMenuListRequestFocus	= false;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(mMenuAdapter!=null){
                mMenuAdapter.setSelectItem(msg.what);
                mMenuAdapter.notifyDataSetInvalidated();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Mens_add();
        initView();
        listView.setAdapter(mMenuAdapter);
        listView.setOnItemClickListener(mOnItemClickLis);

        listView.setOnFocusChangeListener(focusListener);
        mMenuAdapter.setSelectItem(mMenuIndex);
        Fragment fragment = getFragmentByIndex(mMenuIndex);
        if (!fragment.isAdded()) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.settings_content, fragment);
            ft.commit();
        }

        mCurrentHighlight = mMenuIndex;
    }



    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View view, boolean focus) {
            if(view.getId() == R.id.image_return){
                if(focus && mMenuAdapter!=null){
                    mMenuAdapter.setSelectItem(-1);
                    mMenuAdapter.notifyDataSetChanged();
                }
            }else if(view.getId() == R.id.settings_menu){
                if(focus && mMenuAdapter!=null){
                    mIsMenuListRequestFocus = true;
                    mMenuAdapter.setSelectItem(mCurrentHighlight);
                    mMenuAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private void initView() {
        mMenuAdapter = new MenuAdapter(Menus, this);

        listView = (ListView) findViewById(R.id.settings_menu);
        mSharePref=getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    private void Mens_add() {
        Menus = new ArrayList<Menu_Adapter>();
        Menus.add(new Menu_Adapter(R.drawable.menu_icon_info, "相框信息"));
        Menus.add(new Menu_Adapter(R.drawable.menu_icon_settings, "Wifi设置"));
        Menus.add(new Menu_Adapter(R.drawable.menu_icon_about, "关于"));
        Log.d("info", "M" + Menus.toString());
    }


    private Fragment getFragmentByIndex(int index) {
        Fragment currentFragment = null;
        switch (index) {
            case 0:
            default:
                if (mInfoFragment == null) {
                    mInfoFragment = new Fragment_Info();
                }
                currentFragment = mInfoFragment;
                break;
            case 1:
                if (mWiFiFragment == null) {
                    mWiFiFragment = new Fragment_Wlan_Settings();
                }
                currentFragment = mWiFiFragment;
                break;
            case 2:
                if (mAboutFragment == null) {
                    mAboutFragment = new Fragment_About();
                }
                currentFragment = mAboutFragment;
                break;

        }
        return currentFragment;
    }
    private AdapterView.OnItemClickListener mOnItemClickLis = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
            if (mCurrentHighlight == i) {
                return;
            }
            try {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment oldFragment = getFragmentByIndex(mCurrentHighlight);
                oldFragment.onStop();
                Fragment newFragment = getFragmentByIndex(i);
                if (newFragment.isAdded()) {
                    newFragment.onStart();
                }else{
                    ft.add(R.id.settings_content, newFragment);
                }
                ft.hide(oldFragment);
                ft.show(newFragment);
                ft.commit();
            } catch (Exception e) {
            }

            mCurrentHighlight = i;
            saveLastMenuIndex(i);
            refreshListView(i);
        }
    };


    private int getLastMenuIndex() {
        return mSharePref.getInt(KEY_LAST_MENU, 0);
    }

    private void saveLastMenuIndex(int value) {
        if (value >= 0 && value <= Menus.size()) {
            mSharePref.edit().putInt(KEY_LAST_MENU, value).commit();
        }
    }

    private void refreshListView(int index) {
        if(mHandler!=null){
            mHandler.sendEmptyMessage(index);
        }
    }
    public void setItemClick(int i) {
        if (mCurrentHighlight == i) {
            return;
        }

        try {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment oldFragment = getFragmentByIndex(mCurrentHighlight);
            oldFragment.onStop();
            Fragment newFragment = getFragmentByIndex(i);
            if (newFragment.isAdded()) {
                newFragment.onStart();
            }else{
                ft.add(R.id.settings_content, newFragment);
            }
            ft.hide(oldFragment);
            ft.show(newFragment);
            ft.commit();
        } catch (Exception e) {
        }

        mCurrentHighlight = i;

        saveLastMenuIndex(i);
        if(mMenuAdapter!=null){
            mMenuAdapter.setSelectItem(i);
            mMenuAdapter.notifyDataSetChanged();
        }
    }
    private AdapterView.OnItemSelectedListener mSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterview, View view, int i,
                                   long l) {
            if(mIsMenuListRequestFocus&&listView!=null){
                listView.setSelection(mCurrentHighlight);
                mIsMenuListRequestFocus = false;
                return;
            }

            setItemClick(i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    };
}

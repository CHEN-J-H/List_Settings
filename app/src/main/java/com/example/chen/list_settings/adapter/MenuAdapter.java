package com.example.chen.list_settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chen.list_settings.R;
import com.example.chen.list_settings.data.Menu_Adapter;

import java.util.List;

/**
 * Created by Chen on 2016/12/16.
 */
public class MenuAdapter extends BaseAdapter {
    private List<Menu_Adapter> menus;
    private Context context;
    private LayoutInflater inflater;
    private int mSelectItem = -1;

    public MenuAdapter(List<Menu_Adapter> menus, Context context) {
        this.menus = menus;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView != null && convertView.getTag() != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.list_setting_adapter,null);
            holder = new ViewHolder(convertView);
            convertView.getTag();
        }
        ImageView menuIcon = holder.getMenuIcon();
        TextView menuText = holder.getMenuText();

        menuIcon.setImageResource(menus.get(position).getmIconDefault());
        menuText.setText(menus.get(position).getmTextArray());

        if (position == mSelectItem) {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.menu_selected));
        } else {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
        return convertView;
    }


    public final class ViewHolder {
        public View baseView;
        public ImageView menuIcon;
        public TextView menuText;

        public ViewHolder(View view) {
            baseView = view;
        }

        public ImageView getMenuIcon() {
            if (menuIcon == null) {
                ImageView imageView = (ImageView) baseView.findViewById(R.id.settings_menu_icon);
                menuIcon = imageView;
            }
            return menuIcon;
        }

        public TextView getMenuText() {
            if (menuText == null) {
                TextView textView = (TextView) baseView.findViewById(R.id.settings_menu_text);
                menuText = textView;
            }
            return menuText;
        }
    }

    public void setSelectItem(int selectItem) {
        this.mSelectItem = selectItem;
    }


}

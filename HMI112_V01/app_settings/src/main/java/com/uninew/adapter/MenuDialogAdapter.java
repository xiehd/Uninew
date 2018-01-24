package com.uninew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uninew.settings.R;

import java.util.List;


public class MenuDialogAdapter extends BaseAdapter {
    private List<MenuItemData> itemDatas;
    private LayoutInflater inflater;
    private Context context;

    public MenuDialogAdapter(Context context, List<MenuItemData> itemDatas) {
        this.itemDatas = itemDatas;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return itemDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.dialog_menu_item, null, false);
            viewHold = new ViewHold();
            viewHold.menuButton = (TextView) convertView.findViewById(R.id.menu_button_icon);
            viewHold.menuText = (TextView) convertView.findViewById(R.id.menu_button_text);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        convertView.setTag(viewHold);
        viewHold.menuButton.setBackgroundResource(itemDatas.get(position).getMenuIcon());
        viewHold.menuText.setText(itemDatas.get(position).getMenuText());
        return convertView;
    }

    private static final class ViewHold {
        public TextView menuButton;
        public TextView menuText;
    }


    /**
     * item数据填充实体类
     */
    public static class MenuItemData {
        private int menuIcon;
        private String menuText;

        public MenuItemData(int menuIcon, String menuText) {
            this.menuIcon = menuIcon;
            this.menuText = menuText;
        }

        public int getMenuIcon() {
            return menuIcon;
        }

        public void setMenuIcon(int menuIcon) {
            this.menuIcon = menuIcon;
        }

        public String getMenuText() {
            return menuText;
        }

        public void setMenuText(String menuText) {
            this.menuText = menuText;
        }
    }
}

package com.uninew.car.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.uninew.car.R;
import com.uninew.car.audio.TtsUtil;
import com.uninew.car.db.contacts.Contact;
import com.uninew.car.dialog.PromptDialog;
import com.uninew.car.phone.PhoneModel;
import com.uninew.car.until.PinyinUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/9/30 0030.
 */

public class ContactsAdapter extends BaseAdapter<Contact> {


    public ContactsAdapter(List<Contact> datas, Context context) {
        super(datas, context, R.layout.contacts_item_view);
    }

    @Override
    public void bindData(BaseViewHolder holder, final Contact contact) {
        // 进行分组, 比较上一个拼音的首字母和自己是否一致, 如果不一致, 就显示tv_index
        String currentLetter = PinyinUtil.getPinyin(contact.getContact()).charAt(0) + "";
        String indexStr = null;
        if (holder.getPosition() == 0) {
            // 1. 如果是第一位
            indexStr = currentLetter;
        } else {
            // 获取上一个拼音
            String preLetter = PinyinUtil.getPinyin(super.getItem(holder.getPosition() - 1).getContact()).charAt(0) + "";
            if (!TextUtils.equals(currentLetter, preLetter)) {
                // 2. 当跟上一个不同时, 赋值, 显示
                indexStr = currentLetter;
            }
        }
        holder.setVisibility(R.id.tv_index, indexStr == null ? View.GONE : View.VISIBLE);
        holder.setText(R.id.tv_index, indexStr);
        holder.setText(R.id.tv_name, contact.getContact());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(mCallBack != null){
                 mCallBack.onClick(v,contact);
             }
            }
        });
    }
    public ContactsItemCallBack mCallBack;

    public void setContactsItemCallBack(ContactsItemCallBack callBack){
        this.mCallBack = callBack;
    }
    public interface ContactsItemCallBack{
        void onClick(View v,Contact contact);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}

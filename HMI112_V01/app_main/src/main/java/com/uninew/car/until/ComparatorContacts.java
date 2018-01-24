package com.uninew.car.until;

import android.text.TextUtils;

import com.uninew.car.db.contacts.Contact;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class ComparatorContacts implements Comparator<Contact> {

    @Override
    public int compare(Contact o1, Contact o2) {
        if (o1 != null && !TextUtils.isEmpty(o1.getContact())
                && o2 != null && !TextUtils.isEmpty(o2.getContact())) {
            String s1 = PinyinUtil.getPinyin(o1.getContact()).charAt(0) + "";
            String s2 = PinyinUtil.getPinyin(o2.getContact()).charAt(0) + "";
            return Collator.getInstance(Locale.CHINESE).compare(s1, s2);
        }
        return 0;
    }

    public static List sort(List<Contact> strList) {
        ComparatorContacts comp = new ComparatorContacts();
        Collections.sort(strList, comp);
        return strList; //返回排序后的列表
    }
}

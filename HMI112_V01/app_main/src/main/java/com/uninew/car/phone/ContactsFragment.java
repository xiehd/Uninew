package com.uninew.car.phone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.uninew.car.R;
import com.uninew.car.adapter.ContactsAdapter;
import com.uninew.car.db.contacts.Contact;
import com.uninew.car.db.revenue.Revenue;
import com.uninew.car.until.PinyinUtil;
import com.uninew.car.view.FancyIndexer;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class ContactsFragment extends Fragment implements ContactsContract.View ,
        ContactsAdapter.ContactsItemCallBack{

    private ContactsPresenter mPrasenter;
    private View mView;
    private ListView lv_contacts_show;
    private EditText et_search;
    private ImageView iv_search;
    private TextView tv_search;
    private ContactsAdapter mAdapter;
    private FancyIndexer mFancyIndexer;
    private List<Contact> mContacts;

    private static volatile ContactsFragment INSTANCE;

    public static ContactsFragment getInstance() {
        if (INSTANCE != null) {

        } else {
            synchronized (ContactsFragment.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ContactsFragment();
                }
            }
        }
        return INSTANCE;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_contacts, container, false);
            init();
        }
        return mView;
    }

    private void init() {
        if (this.getActivity() != null) {
            mPrasenter = new ContactsPresenter(this.getActivity().getApplicationContext(), this);
        }
        initView();
        mFancyIndexer.setOnTouchLetterChangedListener(new FancyIndexer.OnTouchLetterChangedListener() {

            @Override
            public void onTouchLetterChanged(String letter) {
                System.out.println("letter: " + letter);
//				Util.showToast(getApplicationContext(), letter);

//				showLetter(letter);
                if(mContacts == null || mContacts.isEmpty()){
                    return;
                }
                // 从集合中查找第一个拼音首字母为letter的索引, 进行跳转
                for (int i = 0; i < mContacts.size(); i++) {
                    Contact contact  = mContacts.get(i);
                    String s = PinyinUtil.getPinyin(contact.getContact()).charAt(0) + "";
                    if(TextUtils.equals(s, letter)){
                        // 匹配成功, 中断循环, 跳转到i位置
                        lv_contacts_show.setSelection(i);
                        break;
                    }
                }
            }
        });
        et_search.addTextChangedListener(mTextWatcher);
    }

    private void initView() {
        lv_contacts_show = (ListView) mView.findViewById(R.id.lv_contacts_show);
        mFancyIndexer = (FancyIndexer) mView.findViewById(R.id.bar);
        et_search = (EditText) mView.findViewById(R.id.et_contacts_search);
        iv_search = (ImageView) mView.findViewById(R.id.iv_contacts_search);
        tv_search = (TextView) mView.findViewById(R.id.tv_contacts_search);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPrasenter != null) {
            mPrasenter.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPrasenter != null) {
            mPrasenter.stop();
        }
    }

    @Override
    public void setPresenter(ContactsContract.Presenter presenter) {

    }

    @Override
    public void showContacts(List<Contact> contacts) {
        if(contacts == null || contacts.isEmpty()){
            return;
        }
        mContacts  = contacts;
        mAdapter = new ContactsAdapter(contacts, this.getActivity());
        mAdapter.setContactsItemCallBack(this);
        lv_contacts_show.setAdapter(mAdapter);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = s.toString().trim();
            Log.d("phone",str);
            if(str == null || TextUtils.isEmpty(str)){
                if(mPrasenter != null) {
                    mPrasenter.start();
                }
                iv_search.setVisibility(View.VISIBLE);
                tv_search.setVisibility(View.VISIBLE);
            }else {
                iv_search.setVisibility(View.GONE);
                tv_search.setVisibility(View.GONE);
                if(mPrasenter != null) {
                    mPrasenter.search(str);
                }
            }
        }
    };

    @Override
    public void onClick(View v, Contact contact) {
        mPrasenter.call(contact);
    }
}

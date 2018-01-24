package com.uninew.car.messages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.uninew.car.R;
import com.uninew.car.adapter.MessagesAdapter;
import com.uninew.car.db.message.CarMessage;
import com.uninew.car.db.message.MessageKey;
import com.uninew.car.orders.OrdersContrat;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public class MessagesActivity extends Activity implements MessagesContrat.View,
        MessagesAdapter.OnMessageItemClickListener {

    private MessagesContrat.Presenter mPresenter;
    private LinearLayout ll_msg_bt_communique;
    private LinearLayout ll_msg_bt_event;
    private LinearLayout ll_msg_bt_question;
//    private LinearLayout ll_msg_communique;
//    private LinearLayout ll_msg_event;
//    private LinearLayout ll_msg_question;
    private ListView lv_show_msgs;
    private MessagesAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_messages);
        init();
    }

    private void init() {
        mPresenter = new MessagePresenter(this, this.getApplicationContext());
        initView();
        ll_msg_bt_communique.setBackgroundResource(R.mipmap.button_p);
        ll_msg_bt_event.setBackgroundResource(R.mipmap.button_s);
        ll_msg_bt_question.setBackgroundResource(R.mipmap.button_s);
//        ll_msg_communique.setVisibility(View.VISIBLE);
//        ll_msg_event.setVisibility(View.GONE);
//        ll_msg_question.setVisibility(View.GONE);
    }

    private void initView() {
        ll_msg_bt_communique = (LinearLayout) findViewById(R.id.ll_msg_bt_communique);
        ll_msg_bt_event = (LinearLayout) findViewById(R.id.ll_msg_bt_event);
        ll_msg_bt_question = (LinearLayout) findViewById(R.id.ll_msg_bt_question);
//        ll_msg_communique = (LinearLayout) findViewById(R.id.ll_msg_communique);
//        ll_msg_event = (LinearLayout) findViewById(R.id.ll_msg_event);
//        ll_msg_question = (LinearLayout) findViewById(R.id.ll_msg_question);
        lv_show_msgs = (ListView) findViewById(R.id.lv_show_msgs);
    }

    @Override
    public void showMessages(List<CarMessage> messages) {
        mAdapter = new MessagesAdapter(messages, this.getApplicationContext());
        mAdapter.setOnMessageItemClickListener(this);
        lv_show_msgs.setAdapter(mAdapter);
    }

    @Override
    public void showDetailedMessage(CarMessage message) {
        Intent intent = new Intent(this, DetailsMessageActivity.class);
        intent.putExtra(DetailsMessageActivity.CARMESSAGE_INTENT_KEY, message);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);// 淡出淡入动画效果
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    @Override
    public void answerFinish() {

    }

    @Override
    public void setMessageType(int type) {
        switch (type) {
            case MessageKey.MessageTypeKey.COMMUNIQUE_TYPE:
                ll_msg_bt_communique.setBackgroundResource(R.mipmap.button_p);
                ll_msg_bt_event.setBackgroundResource(R.mipmap.button_s);
                ll_msg_bt_question.setBackgroundResource(R.mipmap.button_s);
//                ll_msg_communique.setVisibility(View.VISIBLE);
//                ll_msg_event.setVisibility(View.GONE);
//                ll_msg_question.setVisibility(View.GONE);
                break;
            case MessageKey.MessageTypeKey.EVENT_TYPE:
                ll_msg_bt_communique.setBackgroundResource(R.mipmap.button_s);
                ll_msg_bt_event.setBackgroundResource(R.mipmap.button_p);
                ll_msg_bt_question.setBackgroundResource(R.mipmap.button_s);
//                ll_msg_communique.setVisibility(View.GONE);
//                ll_msg_event.setVisibility(View.VISIBLE);
//                ll_msg_question.setVisibility(View.GONE);
                break;
            case MessageKey.MessageTypeKey.QUESTION_TYPE:
                ll_msg_bt_communique.setBackgroundResource(R.mipmap.button_s);
                ll_msg_bt_event.setBackgroundResource(R.mipmap.button_s);
                ll_msg_bt_question.setBackgroundResource(R.mipmap.button_p);
//                ll_msg_communique.setVisibility(View.GONE);
//                ll_msg_event.setVisibility(View.GONE);
//                ll_msg_question.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void setPresenter(MessagesContrat.Presenter presenter) {

    }

    public void onChangeShowMsg(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_msg_bt_communique:
                mPresenter.changeShowMessageType(MessageKey.MessageTypeKey.COMMUNIQUE_TYPE);
                ll_msg_bt_communique.setBackgroundResource(R.mipmap.button_p);
                ll_msg_bt_event.setBackgroundResource(R.mipmap.button_s);
                ll_msg_bt_question.setBackgroundResource(R.mipmap.button_s);
//                ll_msg_communique.setVisibility(View.VISIBLE);
//                ll_msg_event.setVisibility(View.GONE);
//                ll_msg_question.setVisibility(View.GONE);
                break;
            case R.id.ll_msg_bt_event:
                mPresenter.changeShowMessageType(MessageKey.MessageTypeKey.EVENT_TYPE);
                ll_msg_bt_communique.setBackgroundResource(R.mipmap.button_s);
                ll_msg_bt_event.setBackgroundResource(R.mipmap.button_p);
                ll_msg_bt_question.setBackgroundResource(R.mipmap.button_s);
//                ll_msg_communique.setVisibility(View.GONE);
//                ll_msg_event.setVisibility(View.VISIBLE);
//                ll_msg_question.setVisibility(View.GONE);
                break;
            case R.id.ll_msg_bt_question:
                mPresenter.changeShowMessageType(MessageKey.MessageTypeKey.QUESTION_TYPE);
                ll_msg_bt_communique.setBackgroundResource(R.mipmap.button_s);
                ll_msg_bt_event.setBackgroundResource(R.mipmap.button_s);
                ll_msg_bt_question.setBackgroundResource(R.mipmap.button_p);
//                ll_msg_communique.setVisibility(View.GONE);
//                ll_msg_event.setVisibility(View.GONE);
//                ll_msg_question.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onItemClick(CarMessage item, int viewId, int position) {
        mPresenter.showDetailedMessage(item);
    }
}

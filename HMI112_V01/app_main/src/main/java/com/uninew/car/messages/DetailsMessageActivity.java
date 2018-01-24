package com.uninew.car.messages;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.IdRes;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.uninew.car.R;
import com.uninew.car.db.message.CarMessage;
import com.uninew.car.db.message.MessageKey;
import com.uninew.car.view.LineDividerTextView;

import java.util.List;

public class DetailsMessageActivity extends Activity implements DetailsMessageContrat.View, RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private LinearLayout ll_communique;
    private TextView tv_communique_time;
    private LineDividerTextView tv_communique_content;
    private LinearLayout ll_event_view;
    private LinearLayout ll_msg_question;
    private TextView tv_event_time;
    private TextView tv_event_content;
    private RadioGroup rg_answer;
    private RadioButton rb_answer_1;
    private RadioButton rb_answer_2;
    private Button bt_answer;

    private DetailsMessageContrat.Presenter mPresenter;
    private List<CarMessage.AnswerMsg> answerMsgs;
    private CarMessage mCarMessage;
    private int mAnswerId = -1;

    public static final String CARMESSAGE_INTENT_KEY = "carMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details_message);
        init();
    }

    private void init() {
        mPresenter = new DetailsMessagePresenter(this, this.getApplicationContext());
        initView();
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        mCarMessage = (CarMessage) intent.getSerializableExtra(CARMESSAGE_INTENT_KEY);
        if (mCarMessage == null) {
            finish();
            return;
        }
        Log.d("msg",mCarMessage.toString());
        if(mCarMessage.getAnswerSelectgList() != null) {
            mPresenter.setAnswerSelectgList(mCarMessage.getAnswerSelectgList());
        }
        mPresenter.setTime(mCarMessage.getTime());
        mPresenter.setMessageType(mCarMessage.getMessageType());
        mPresenter.setContent(mCarMessage.getContent());
        mPresenter.start();
        initListener();
    }

    private void initListener() {
        rg_answer.setOnCheckedChangeListener(this);
        bt_answer.setOnClickListener(this);
    }

    private void initView() {
        ll_communique = (LinearLayout) findViewById(R.id.ll_msg_communique);
        ll_event_view = (LinearLayout) findViewById(R.id.ll_msg_event_view);
        tv_communique_content = (LineDividerTextView) findViewById(R.id.tv_msg_communique_content);
        tv_communique_time = (TextView) findViewById(R.id.tv_msg_communique_time);
        tv_event_content = (TextView) findViewById(R.id.tv_msg_event_content);
        ll_msg_question = (LinearLayout) findViewById(R.id.ll_msg_question);
        tv_event_time = (TextView) findViewById(R.id.tv_msg_event_time);
        rg_answer = (RadioGroup) findViewById(R.id.rg_msg_answer);
        rb_answer_1 = (RadioButton) findViewById(R.id.rb_msg_answer_1);
        rb_answer_2 = (RadioButton) findViewById(R.id.rb_msg_answer_2);
        bt_answer = (Button) findViewById(R.id.bt_msg_answer);
    }

    @Override
    public void setPresenter(DetailsMessageContrat.Presenter presenter) {

    }

    @Override
    public void showTime(String time) {
        tv_event_time.setText(time);
        tv_communique_time.setText(time);
    }

    @Override
    public void showMessageType(int messageType) {
        switch (messageType) {
            case MessageKey.MessageTypeKey.COMMUNIQUE_TYPE:
                ll_event_view.setVisibility(View.GONE);
                bt_answer.setVisibility(View.GONE);
                ll_communique.setVisibility(View.VISIBLE);
                break;
            case MessageKey.MessageTypeKey.EVENT_TYPE:
                ll_event_view.setVisibility(View.VISIBLE);
                bt_answer.setVisibility(View.VISIBLE);
                ll_communique.setVisibility(View.GONE);
                rg_answer.setVisibility(View.GONE);
                ll_msg_question.setVisibility(View.GONE);
                bt_answer.setText(getString(R.string.confirm));
                break;
            case MessageKey.MessageTypeKey.QUESTION_TYPE:
                ll_event_view.setVisibility(View.VISIBLE);
                ll_msg_question.setVisibility(View.VISIBLE);
                if(mCarMessage.getAnswerState() == MessageKey.MessageAnswerState.UNANSWERED) {
                    bt_answer.setVisibility(View.VISIBLE);
                }else{
                    bt_answer.setVisibility(View.GONE);
                }
                ll_communique.setVisibility(View.GONE);
                bt_answer.setText(getString(R.string.submit));
                break;
        }
    }

    @Override
    public void showContent(String content) {
        tv_event_content.setText(content);
        tv_communique_content.setText(content);
    }

    @Override
    public void showAnswerSelectgList(List<CarMessage.AnswerMsg> answerSelectgList) {
        if (answerSelectgList == null || answerSelectgList.isEmpty()) {
            return;
        }
        answerMsgs = answerSelectgList;
        int size = answerSelectgList.size();
        for (int i = 0; i < size; i++) {
            CarMessage.AnswerMsg answer = answerSelectgList.get(i);
            if (i == 0) {
                rb_answer_1.setText(answer.getAnswerContent());
                rb_answer_2.setVisibility(View.GONE);
                rb_answer_1.setVisibility(View.VISIBLE);
            }
            if (i == 1) {
                rb_answer_2.setText(answer.getAnswerContent());
                rb_answer_2.setVisibility(View.VISIBLE);
                rb_answer_1.setVisibility(View.VISIBLE);
            }
            if (i > 1) {
                RadioButton rb = new RadioButton(this.getApplicationContext());
                rb.setId(answer.getId());
                rb.setButtonDrawable(R.drawable.answer_selector);
                rb.setCompoundDrawablePadding(10);
                rb.setTextColor(getResources().getColor(R.color.text_p_color));
                rb.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                rb.setPadding(3, 3, 3, 3);
                rb.setText(answer.getAnswerContent());
                RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.WRAP_CONTENT, 1);
                rg_answer.addView(rb, lp);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);// 淡出淡入动画效果
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (answerMsgs == null || answerMsgs.isEmpty()) {
            return;
        }
        if (checkedId == R.id.rb_msg_answer_1) {
            CarMessage.AnswerMsg answer = answerMsgs.get(0);
            if (rb_answer_1.isChecked()) {
                mAnswerId = answer.getAnswerId();
            }
        } else if (checkedId == R.id.rb_msg_answer_2) {
            CarMessage.AnswerMsg answer = answerMsgs.get(1);
            if (rb_answer_2.isChecked()) {
                mAnswerId = answer.getAnswerId();
            }
        } else {
            mAnswerId = checkedId;
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bt_msg_answer:
                if (mCarMessage == null || mAnswerId == -1 || mCarMessage.getMassgeId() == -1) {
                    return;
                }
                mPresenter.answerAction(mCarMessage.getMassgeId(), mAnswerId);
                mAnswerId = -1;
                break;
        }
    }
}

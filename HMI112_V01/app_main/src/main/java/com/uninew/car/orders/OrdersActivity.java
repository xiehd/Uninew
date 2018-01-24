package com.uninew.car.orders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.uninew.car.R;
import com.uninew.car.adapter.OrdersAdapter;
import com.uninew.car.db.order.Order;
import com.uninew.car.db.order.OrderKey;

import java.util.List;

/**
 * Created by Administrator on 2017/9/12 0012.
 */

public class OrdersActivity extends Activity implements OrdersContrat.View, OrdersAdapter.OnOrderItemClickListener {

    private static final String TAG = "OrdersActivity";
    private static final boolean D = true;
    private OrdersContrat.Presenter mPresenter;
    private ListView lv_show_orders;
    private OrdersAdapter mAdapter;
    private LinearLayout ll_orders_bade;
    private LinearLayout ll_orders_finish;
    private LinearLayout ll_orders_other;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_orders);
        init();
    }

    public void onChangeShowOrder(View view) {
        int id = view.getId();
        view.setBackgroundResource(R.mipmap.button_p);
        switch (id) {
            case R.id.ll_orders_bade:
                mPresenter.changeShowOrderState(OrderKey.OrderStateKey.BADE_STATE);
                ll_orders_finish.setBackgroundResource(R.mipmap.button_s);
                ll_orders_other.setBackgroundResource(R.mipmap.button_s);
                break;
            case R.id.ll_orders_finish:
                mPresenter.changeShowOrderState(OrderKey.OrderStateKey.FINISH_STATE);
                ll_orders_bade.setBackgroundResource(R.mipmap.button_s);
                ll_orders_other.setBackgroundResource(R.mipmap.button_s);
                break;
            case R.id.ll_orders_other:
                mPresenter.changeShowOrderState(new int[]{
                        OrderKey.OrderStateKey.FAILURE_STATE,
                        OrderKey.OrderStateKey.DRIVER_CANCEL_STATE,
                        OrderKey.OrderStateKey.PASSENGER_CANCEL_STATE,
                        OrderKey.OrderStateKey.OTHER_STATE
                });
                ll_orders_finish.setBackgroundResource(R.mipmap.button_s);
                ll_orders_bade.setBackgroundResource(R.mipmap.button_s);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.stop();
    }

    private void init() {
        mPresenter = new OrdersPresenter(this, this.getApplicationContext());
        initView();
        mPresenter.start();
        ll_orders_bade.setBackgroundResource(R.mipmap.button_p);
        ll_orders_finish.setBackgroundResource(R.mipmap.button_s);
        ll_orders_other.setBackgroundResource(R.mipmap.button_s);
    }

    private void initView() {
        lv_show_orders = (ListView) findViewById(R.id.lv_show_orders);
        ll_orders_bade = (LinearLayout) findViewById(R.id.ll_orders_bade);
        ll_orders_finish = (LinearLayout) findViewById(R.id.ll_orders_finish);
        ll_orders_other = (LinearLayout) findViewById(R.id.ll_orders_other);
    }


    @Override
    public void setPresenter(OrdersContrat.Presenter presenter) {

    }

    @Override
    public void showOrders(List<Order> orders) {
        initAdapter(orders);
    }

    private void initAdapter(List<Order> orders) {
        mAdapter = new OrdersAdapter(orders, this.getApplicationContext());
        mAdapter.setOnOrderItemClickListener(this);
        lv_show_orders.setAdapter(mAdapter);
    }

    @Override
    public void showDetailedOrder(Order order) {
        Intent intent = new Intent(this, DetailsOrderActivity.class);
        intent.putExtra(DetailsOrderActivity.ORDER_INTENT_KEY, order);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);// 淡出淡入动画效果
    }

    @Override
    public void answerFinish() {

    }

    @Override
    public void onItemClick(Order item, int viewId, int position) {
        mPresenter.showDetailedOrder(item);
        if (D) Log.d(TAG, "position:" + position + "/n" + item.toString());
    }
}

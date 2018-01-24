package com.uninew.car.messages;

import com.uninew.car.base.BasePresenter;
import com.uninew.car.base.BaseView;
import com.uninew.car.db.message.CarMessage;
import com.uninew.car.db.order.Order;
import com.uninew.car.orders.OrdersContrat;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public interface MessagesContrat {
    interface View extends BaseView<MessagesContrat.Presenter> {
        void showMessages(List<CarMessage> messages);

        void showDetailedMessage(CarMessage message);

        void answerFinish();

        void setMessageType(int type);
    }

    interface Presenter extends BasePresenter {

        void changeShowMessageType(int type);

        void changeMessageState(int id,int state,String finishTime);

        void confirmFinish(int position);

        void cancelMessage(int position);

        void showDetailedMessage(CarMessage message);
    }
}

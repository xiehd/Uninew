package com.uninew.car.evaluation;

import android.content.Context;

import com.uninew.car.db.revenue.Revenue;
import com.uninew.car.db.revenue.RevenueLocalDataSource;
import com.uninew.car.db.revenue.RevenueLocalSource;

/**
 * Created by Administrator on 2017/9/25 0025.
 */

public class EvaluationModel {
    private Context mContext;
    private RevenueLocalSource mDBRevenue;

    public EvaluationModel(Context context) {
        mContext = context;
        mDBRevenue = RevenueLocalDataSource.getInstance(context);
    }

    /**
     * 改变星级评价
     *
     * @param time         上车时间
     * @param evaluateStar 星级
     */
    public void changeEvaluateStar(String time, int evaluateStar) {
//        mDBRevenue.changeEvaluateStart(time, evaluateStar);
    }

    private void saveRevenue(Revenue revenue) {
        mDBRevenue.saveDBData(revenue);
    }
}

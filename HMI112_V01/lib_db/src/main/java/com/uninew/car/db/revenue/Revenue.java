package com.uninew.car.db.revenue;

import com.uninew.car.db.location.LocationMessage;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Administrator on 2017/8/30 0030.
 */

public class Revenue implements Serializable {
    private int id;
    /* 空车转重车的位置信息 */
    private byte[] upCarLocation;
    /* 重车转空车的位置信息 */
    private byte[] downLocation;
    /* 运营ID */
    private int revenueId;
    /* 评价ID */
    private int evaluationId;
    /* 评价选项 */
    private int evaluation;
    /* 评价选项扩展 */
    private int evaluationExtended;
    /* 电召订单Id */
    private int orderId;
    /* 运营数据*/
    private byte[] revenueDatas;

    private String time;

    public Revenue() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getUpCarLocation() {
        return upCarLocation;
    }

    public void setUpCarLocation(byte[] upCarLocation) {
        this.upCarLocation = upCarLocation;
    }

    public byte[] getDownLocation() {
        return downLocation;
    }

    public void setDownLocation(byte[] downLocation) {
        this.downLocation = downLocation;
    }

    public int getRevenueId() {
        return revenueId;
    }

    public void setRevenueId(int revenueId) {
        this.revenueId = revenueId;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public int getEvaluationExtended() {
        return evaluationExtended;
    }

    public void setEvaluationExtended(int evaluationExtended) {
        this.evaluationExtended = evaluationExtended;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public byte[] getRevenueDatas() {
        return revenueDatas;
    }

    public void setRevenueDatas(byte[] revenueDatas) {
        this.revenueDatas = revenueDatas;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Revenue{" +
                "id=" + id +
                ", upCarLocation=" + Arrays.toString(upCarLocation) +
                ", downLocation=" + Arrays.toString(downLocation) +
                ", revenueId=" + revenueId +
                ", evaluationId=" + evaluationId +
                ", evaluation=" + evaluation +
                ", evaluationExtended=" + evaluationExtended +
                ", orderId=" + orderId +
                ", revenueDatas=" + Arrays.toString(revenueDatas) +
                ", time='" + time + '\'' +
                '}';
    }
}


package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 运营数据上传
 * Created by Administrator on 2017/8/20 0020.
 */

public class T_OperationDataReport extends BaseBean {

    /**
     * 空转重时车位置基础信息
     */
    private T_LocationReport upCarLocation;
    /**
     * 重转空时车位置基础信息
     */
    private T_LocationReport downCarLocation;
    /**
     * 运营ID
     */
    private int operationId;
    /**
     * 评价ID
     */
    private int evaluationId;
    /**
     * 评价选项  0x00:未评价；0x01:满意；0x02：一般；0x03：不满意
     */
    private int evaluationOption;
    /**
     * 评价选项扩展
     */
    private int evaluationExtended;
    /**
     * 电召订单ID
     */
    private int orderId;
    /**
     * 营运数据内容
     */
    private byte[] operationDatas;

    public T_OperationDataReport(){

    }

    public T_OperationDataReport(T_LocationReport upCarLocation, T_LocationReport downCarLocation,
                                 int operationId, int evaluationId, int evaluationOption,
                                 int evaluationExtended, int orderId, byte[] operationDatas) {
        this.upCarLocation = upCarLocation;
        this.downCarLocation = downCarLocation;
        this.operationId = operationId;
        this.evaluationId = evaluationId;
        this.evaluationOption = evaluationOption;
        this.evaluationExtended = evaluationExtended;
        this.orderId = orderId;
        this.operationDatas = operationDatas;
    }

    @Override
    public int getTcpId() {
        return this.tcpId;
    }

    @Override
    public void setTcpId(int tcpId) {
        this.tcpId = tcpId;
    }

    @Override
    public int getTransportId() {
        return this.transportId;
    }

    @Override
    public void setTransportId(int transportId) {
        this.transportId = transportId;
    }

    @Override
    public String getSmsPhoneNumber() {
        return this.smsPhonenumber;
    }

    @Override
    public void setSmsPhoneNumber(String smsPhonenumber) {
        this.smsPhonenumber = smsPhonenumber;
    }

    @Override
    public int getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Override
    public int getMsgId() {
        return BaseMsgID.OPERATION_DATA_REPORT;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.write(upCarLocation.getDataBytes());
            out.write(downCarLocation.getDataBytes());
            out.writeInt(operationId);
            out.writeInt(evaluationId);
            out.writeByte(evaluationOption);
            out.writeShort(evaluationExtended);
            out.writeInt(orderId);
            out.write(operationDatas);
            out.flush();
            datas = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return datas;
    }

    @Override
    public Object getDataPacket(byte[] datas) {
        return null;
    }

    public T_LocationReport getUpCarLocation() {
        return upCarLocation;
    }

    public T_LocationReport getDownCarLocation() {
        return downCarLocation;
    }

    public int getOperationId() {
        return operationId;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public int getEvaluationOption() {
        return evaluationOption;
    }

    public int getEvaluationExtended() {
        return evaluationExtended;
    }

    public int getOrderId() {
        return orderId;
    }

    public byte[] getOperationDatas() {
        return operationDatas;
    }

    public void setUpCarLocation(T_LocationReport upCarLocation) {
        this.upCarLocation = upCarLocation;
    }

    public void setDownCarLocation(T_LocationReport downCarLocation) {
        this.downCarLocation = downCarLocation;
    }

    public void setOperationId(int operationId) {
        this.operationId = operationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public void setEvaluationOption(int evaluationOption) {
        this.evaluationOption = evaluationOption;
    }

    public void setEvaluationExtended(int evaluationExtended) {
        this.evaluationExtended = evaluationExtended;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setOperationDatas(byte[] operationDatas) {
        this.operationDatas = operationDatas;
    }


}

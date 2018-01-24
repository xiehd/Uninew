package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * 摄像头图像上传
 * Created by Administrator on 2017/8/19 0019.
 */

public class T_PictureUpload extends BaseBean{

    private int upReason;//上传原因
    private int image;//图像?
    private int carmeraId;//摄像头ID
    private int imageSize;//图片大小
    private int startPosition;//起始地址
    private byte[] datas;//位置图像数据包

    public T_PictureUpload() {
    }

    public T_PictureUpload(int upReason, int image, int carmeraId, int imageSize, int startPosition, byte[] datas) {
        this.upReason = upReason;
        this.image = image;
        this.carmeraId = carmeraId;
        this.imageSize = imageSize;
        this.startPosition = startPosition;
        this.datas = datas;
    }

    public int getUpReason() {
        return upReason;
    }

    public void setUpReason(int upReason) {
        this.upReason = upReason;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getCarmeraId() {
        return carmeraId;
    }

    public void setCarmeraId(int carmeraId) {
        this.carmeraId = carmeraId;
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public byte[] getDatas() {
        return datas;
    }

    public void setDatas(byte[] datas) {
        this.datas = datas;
    }

    @Override
    public int getTcpId() {
        return this.tcpId;
    }

    @Override
    public void setTcpId(int tcpId) {
        this.tcpId=tcpId;
    }

    @Override
    public int getTransportId() {
        return this.transportId;
    }

    @Override
    public void setTransportId(int transportId) {
        this.transportId=transportId;
    }

    @Override
    public String getSmsPhoneNumber() {
        return this.smsPhonenumber;
    }

    @Override
    public void setSmsPhoneNumber(String smsPhonenumber) {
        this.smsPhonenumber=smsPhonenumber;
    }

    @Override
    public int getSerialNumber() {
        return this.serialNumber;
    }

    @Override
    public void setSerialNumber(int serialNumber) {
        this.serialNumber=serialNumber;
    }

    @Override
    public int getMsgId() {
        return BaseMsgID.CAMERA_PICTURE_UPLOAD;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] buffer = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeShort(upReason);
            out.writeInt(image);
            out.writeByte(carmeraId);
            out.writeInt(imageSize);
            out.writeInt(startPosition);
            out.write(datas);
            out.flush();
            buffer = stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
        return buffer;
    }

    @Override
    public Object getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            upReason = in.readUnsignedShort();
            image = in.readInt();
            carmeraId = in.readUnsignedByte();
            imageSize = in.readInt();
            startPosition = in.readInt();
            int size = in.available();
            datas  = new byte[size];
            in.read(datas);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (stream != null) {
                    stream.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "T_PictureUpload{" +
                "upReason=" + upReason +
                ", image=" + image +
                ", carmeraId=" + carmeraId +
                ", imageSize=" + imageSize +
                ", startPosition=" + startPosition +
                ", datas=" + Arrays.toString(datas) +
                '}';
    }
}

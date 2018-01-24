package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 摄像头立即拍摄命令
 * Created by Administrator on 2017/8/19 0019.
 */

public class P_CamaraShoot extends BaseBean {

    private int cameraId;//摄像头ID
    private int shootCmd;//拍照命令
    private int shootInterval;//拍照间隔或录像间隔
    private int saveFlag;//保存标志
    private int resolution;//分辨率
    private int imageVideoQuality;//图片/视频质量
    private int brightness;//亮度0-255
    private int contrast;//对比度0-127
    private int saturation;//饱和度0-127
    private int chroma;//色度0-255


    public P_CamaraShoot() {
    }

    public P_CamaraShoot(byte[] body) {
        getDataPacket(body);
    }

    public P_CamaraShoot(int cameraId, int shootCmd, int shootInterval, int saveFlag,
                         int resolution, int imageVideoQuality, int brightness,
                         int contrast, int saturation, int chroma) {
        this.cameraId = cameraId;
        this.shootCmd = shootCmd;
        this.shootInterval = shootInterval;
        this.saveFlag = saveFlag;
        this.resolution = resolution;
        this.imageVideoQuality = imageVideoQuality;
        this.brightness = brightness;
        this.contrast = contrast;
        this.saturation = saturation;
        this.chroma = chroma;
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
        return BaseMsgID.CAMERA_SHOOT_NOW_COMMOND;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
           out.writeByte(cameraId);
            out.writeShort(shootCmd);
            out.writeShort(shootInterval);
            out.writeByte(saveFlag);
            out.writeByte(resolution);
            out.writeByte(imageVideoQuality);
            out.writeByte(brightness);
            out.writeByte(contrast);
            out.writeByte(saturation);
            out.writeByte(chroma);
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
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            cameraId = in.readUnsignedByte();
            shootCmd = in.readUnsignedShort();
            shootInterval = in.readUnsignedShort();
            saveFlag = in.readUnsignedByte();
            resolution = in.readUnsignedByte();
            imageVideoQuality = in.readUnsignedByte();
            brightness = in.readUnsignedByte();
            contrast = in.readUnsignedByte();
            saturation = in.readUnsignedByte();
            chroma = in.readUnsignedByte();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
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

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public int getShootCmd() {
        return shootCmd;
    }

    public void setShootCmd(int shootCmd) {
        this.shootCmd = shootCmd;
    }

    public int getShootInterval() {
        return shootInterval;
    }

    public void setShootInterval(int shootInterval) {
        this.shootInterval = shootInterval;
    }

    public int getSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(int saveFlag) {
        this.saveFlag = saveFlag;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public int getImageVideoQuality() {
        return imageVideoQuality;
    }

    public void setImageVideoQuality(int imageVideoQuality) {
        this.imageVideoQuality = imageVideoQuality;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getContrast() {
        return contrast;
    }

    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public int getChroma() {
        return chroma;
    }

    public void setChroma(int chroma) {
        this.chroma = chroma;
    }
}

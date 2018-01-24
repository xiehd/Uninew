package com.uninew.net.JT905.bean;

import com.uninew.net.JT905.common.BaseMsgID;
import com.uninew.net.JT905.common.ProtocolTool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 事件设置类
 * 该类既可以作为一个事件集合亦可以作为一个单独的事件项
 * 事件集合调用参数：事件个数和事件项列表
 * 事件项调用参数： 事件ID和事件内容
 * Created by Administrator on 2017/9/13 0019.
 */

public class P_EventSet extends BaseBean {

    /**事件个数*/
    private int eventCount;
    /**事件项列表*/
    private List<Event> eventList;

    public P_EventSet() {
    }

    public P_EventSet(byte[] body) {
        getDataPacket(body);
    }

    public P_EventSet(int eventCount, List<Event> eventList) {
        this.eventCount = eventCount;
        this.eventList = eventList;
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
        return BaseMsgID.EVENT_SET;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
           out.writeByte(eventCount);
            for (Event event:eventList){
                out.writeByte(event.eventId);
                out.write(ProtocolTool.stringToByte(event.eventContent,ProtocolTool.CHARSET_905,20));
            }
            out.flush();
            datas = stream.toByteArray();
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
        return datas;
    }

    @Override
    public Object getDataPacket(byte[] datas) {
        ByteArrayInputStream stream = new ByteArrayInputStream(datas);
        DataInputStream in = new DataInputStream(stream);
        try {
            eventCount=in.readByte();
            Event event ;
            int eventId;
            String str;
            eventList=new ArrayList<>();
            for (int i=0;i<eventCount;i++){
                event=new Event();
                eventId=in.readByte();
                event.setEventId(eventId);
                byte[] b=new byte[20];
                in.read(b);
                str=new String(b, ProtocolTool.CHARSET_905);
                event.setEventContent(str);
                eventList.add(event);
            }
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


    public class Event{
        /**事件ID*/
        private int eventId;
        /**事件内容*/
        private String eventContent;

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }

        public String getEventContent() {
            return eventContent;
        }

        public void setEventContent(String eventContent) {
            this.eventContent = eventContent;
        }
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public String toString() {
        return "P_EventSet{" +
                "eventCount=" + eventCount +
                ", eventList=" + eventList +
                '}';
    }
}

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
 * 设置電話本
 * Created by Administrator on 2017/8/19 0019.
 */

public class P_PhoneBookSet extends BaseBean{

    /**联系人总数*/
    private int contactCount;
    /**联系人列表*/
    private List<Contact> contactList;

    /**
     * 电话本联系人信息
     */
    public class Contact{
        /**标志：1：呼入，2：呼出，3：呼入/呼出*/
        private int mark;
        /**电话号码*/
        private String phoneNumber;
        /**联系人*/
        private String contactName;

        public Contact() {
        }

        public Contact(int mark, String phoneNumber, String contactName) {
            this.mark = mark;
            this.phoneNumber = phoneNumber;
            this.contactName = contactName;
        }

        public int getMark() {
            return mark;
        }

        public void setMark(int mark) {
            this.mark = mark;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        @Override
        public String toString() {
            return "Contact{" +
                    "mark=" + mark +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", contactName='" + contactName + '\'' +
                    '}';
        }
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
        return BaseMsgID.SET_PHONE_BOOK;
    }

    @Override
    public byte[] getDataBytes() {
        byte[] datas = null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
           out.writeByte(contactCount);
           for (Contact contact:contactList){
               out.writeByte(contact.mark);
               out.write(ProtocolTool.stringToByte(contact.phoneNumber,ProtocolTool.CHARSET_905,20));
               out.write(ProtocolTool.stringToByte(contact.contactName,ProtocolTool.CHARSET_905,10));
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
        try {
            contactList = new ArrayList<>();
            contactCount=datas[0];
            int[] ids=new int[contactCount];//记录每个string结束的id位置
            int j=0;
            for (int i=1;i<datas.length;i++){
                if (datas[i]==0x00){
                    ids[j]=i;
                    j++;
                }
            }
            int position=1;
            Contact contact=null;
            for (int k=0;k< datas.length;k++){//联系人数目
                contact=new Contact();
                contact.setMark(datas[position]);
                position=2;
                byte[] a=new byte[ids[k]-position];
                System.arraycopy(datas,position,a,0,ids[k]-position);
                contact.setPhoneNumber(ProtocolTool.byteToString(a,ProtocolTool.CHARSET_905));
                position=ids[k];
                byte[] b=new byte[ids[k+1]-position];
                System.arraycopy(datas,position,b,0,ids[k+1]-position);
                contact.setPhoneNumber(ProtocolTool.byteToString(b,ProtocolTool.CHARSET_905));
                contactList.add(contact);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public P_PhoneBookSet(byte[] buffers) {
        getDataPacket(buffers);
    }

    public int getContactCount() {
        return contactCount;
    }

    public void setContactCount(int contactCount) {
        this.contactCount = contactCount;
    }

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    @Override
    public String toString() {
        return "P_PhoneBookSet{" +
                "contactCount=" + contactCount +
                ", contactList=" + contactList +
                '}';
    }
}

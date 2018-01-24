package com.uninew.net.JT905.protocol;

import android.util.Log;

import com.uninew.net.JT905.common.BaseMsgID;

import java.util.HashMap;
import java.util.Map;

/**
 * 重发消息列表和对应的应答消息
 * Created by Administrator on 2017/8/22 0022.
 */

public class  ReSendMap {

    public Map<Integer,Integer> reSendMsgMap;

    public ReSendMap() {
        reSendMsgMap = new HashMap<>();
        init();
    }

    /**
     * 配置需要补传的消息和应答消息
     */
    private void init(){
        //位置上报
        reSendMsgMap.put(BaseMsgID.LOCATION_INFORMATION_REPORT,BaseMsgID.PLATFORM_GENERAL_ANS);
        //事件上报
        reSendMsgMap.put(BaseMsgID.EVENT_REPORT,BaseMsgID.PLATFORM_GENERAL_ANS);
    }

    /**
     * 根据上传消息Id判断该消息是否需要补传
     * @param msgId
     * @return
     */
    public boolean isNeedReSend(int msgId){
        int result=0;
        if (!reSendMsgMap.isEmpty()){
            Log.i("tt","isNeedReSend,msgId="+msgId);
            if (reSendMsgMap.containsKey(msgId)){
                Log.i("tt","isNeedReSend---true");
                return true;
            }
        }
        return false;
    }

    /**
     * 根据下发消息Id判断该消息是否为重发应答
     * @param msgId
     * @return
     */
    public boolean isNeedReSendAns(int msgId){
        int result=0;
        if (!reSendMsgMap.isEmpty()){
            if (reSendMsgMap.containsValue(msgId)){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据下发消息Id判断该消息是否为重发应答
     * @param msgId
     * @return 注意保证该值为唯一的。
     */
    public int getKeyByValue(int msgId){
        if (!reSendMsgMap.isEmpty()){
            for (Map.Entry<Integer, Integer> m :reSendMsgMap.entrySet())  {
                System.out.println(m.getKey()+"\t"+m.getValue());
                if (msgId==m.getValue()){
                    return m.getKey();
                }
            }
        }
        return 0;
    }
}

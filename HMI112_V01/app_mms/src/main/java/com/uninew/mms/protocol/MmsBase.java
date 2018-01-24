package com.uninew.mms.protocol;

import android.util.Log;

import com.uninew.mms.ChuanJiLed.protocol.CJLedProtocolPacket;
import com.uninew.mms.interfaces.IMMsCallBackListener;
import com.uninew.mms.interfaces.IMmsBase;
import com.uninew.net.Taximeter.protocol.TaxiStickyBagHandle;

/**
 * 接受数据沾包处理(此处处理MCU传来的不同协议)
 * Created by Administrator on 2017/11/10.
 */

public class MmsBase implements IMmsBase, TaxiStickyBagHandle.IDatasCallBack {
    private TaxiStickyBagHandle mTaxiStickyBagHandle;//沾包处理(905)
    private IMMsCallBackListener.ITaxiDatasCallBack mITaxiDatasCallBack;

    public MmsBase() {
        mTaxiStickyBagHandle = new TaxiStickyBagHandle(this);
    }

    public void setITaxiDatasCallBack(IMMsCallBackListener.ITaxiDatasCallBack mITaxiDatasCallBack) {
        this.mITaxiDatasCallBack = mITaxiDatasCallBack;
    }

    @Override
    public void onReceiveTcpDatas(int ID, byte[] datas) {
        if (datas == null) {
            Log.e("onReceiveTcpDatas", "---串口数据------datas = null");
            return;
        }
        if (ID == 0x01) {//234  1路数据
//            CJLedProtocolPacket mCJLedProtocolPacket = new CJLedProtocolPacket();
//            mCJLedProtocolPacket.getDataPacket(datas);
        } else if (ID == 0x02) {//234  2路数据
            mTaxiStickyBagHandle.dataput(datas);
        } else if (ID == 0x03) {//234  3路数据

        }

    }

    @Override
    public void datasCallBack(byte[] bytes) {
        if (mITaxiDatasCallBack != null) {
            mITaxiDatasCallBack.taxiDateCallBack(bytes);
        }
    }


}

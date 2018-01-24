package com.uninew.mms.tools;

public interface DefineTYRoadSigns {
	
	/**编码格式*/
	public String ENCODE_GB2312="GB2312";
	public String ENCODE_ASCII="ASCII";
	
	/**开始结束标志*/
	public byte MSG_FLAG = 0x7e;
	/**转义标志*/
	public byte ESCAPE = 0x7d;
	/**7E转义标志*/
	public byte FOLLOW_ESCAPE_7E = 0x5e;
	/**7D转义标志*/
	public byte FOLLOW_ESCAPE_7D = 0x5d;
	
	/**地址：标准分配0x00-单机通讯*/
	public byte DEFAULT_ADDRESS=0x00;
	/**协议地址：0x10表示本报站器与图岳电子路牌之间的通讯协议*/
	public byte DEFAULT_PROTOCOLID=0x10;
	/**CMD-电子路牌信息为0x03*/
	public byte DEFAULT_CMD=0x03;
	/**几字路牌-默认0x04*/
	public byte DEFAULT_WHATBYTE=0x04;
	/**显示风格-默认0x02*/
	public byte DEFAULT_SHOWTYPE=0x02;
	
	
}

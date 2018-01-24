package com.uninew.net.JT905.comm.common;

import java.io.Serializable;

public class BaseMessageBean implements Serializable{

	private static final long serialVersionUID = 6968144653405551933L;

	/**消息ID*/
	private int msgId;
	
	/**消息体对象*/
	private Object object;

	public BaseMessageBean() {
		super();
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public BaseMessageBean(int msgId, Object object) {
		super();
		this.msgId = msgId;
		this.object = object;
	}

	public int getMsgId() {
		return msgId;
	}

	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}


}

package com.uninew.net.JT905.comm.common;

public interface DefineNetAction {

	/**与NET交互Action,Net接收*/
	String NETLinkProtocolClient = "Com.NETLink.Protocol.Client";
	
	/**与NET交互Action，Net发出*/
	String NETLinkProtocolServer = "Com.NETLink.Protocol.Server";

	/**与MMS交互Action,MMS接收*/
	String MMSLinkProtocolClient = "Com.MMSLink.Protocol.Client";

	/**与MMS交互Action，MMS发出*/
	String MMSLinkProtocolServer = "Com.MMSLink.Protocol.Server";
	
	/**对应的键值*/
	String Key_Object="object";
	
	
}

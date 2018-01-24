package com.uninew.net.JT905.comm.common;

import java.io.Serializable;

public class PlatformMsg implements Serializable {
	private static final long serialVersionUID = -4921644695184265265L;
	/** IP地址 */
	private String ip;
	/** 端口号 */
	private int port;
	/** 域名 */
	private String domainName;

	public PlatformMsg() {
		super();
	}

	public PlatformMsg(String ip, int port, String domainName) {
		super();
		this.ip = ip;
		this.port = port;
		this.domainName = domainName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	@Override
	public String toString() {
		return "PlatformMsg [ip=" + ip + ", port=" + port + ", domainName="
				+ domainName + "]";
	}

}

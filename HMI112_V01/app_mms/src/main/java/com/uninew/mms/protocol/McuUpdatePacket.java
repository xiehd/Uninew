package com.uninew.mms.protocol;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class McuUpdatePacket {
   private int total_pack;
   private byte[] data;
   private int pack_no;
public McuUpdatePacket(int total_pack,int pack_no ,  byte[] data) {
	super();
	this.total_pack = total_pack;
	this.data = data;
	this.pack_no = pack_no;
}
public int getTotal_pack() {
	return total_pack;
}
public void setTotal_pack(int total_pack) {
	this.total_pack = total_pack;
}
public byte[] getData() {
	return data;
}
public void setData(byte[] data) {
	this.data = data;
}
public int getPack_no() {
	return pack_no;
}
public void setPack_no(int pack_no) {
	this.pack_no = pack_no;
}
 public byte[] tobytes(){
	 ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
	 DataOutputStream out=new DataOutputStream(outputStream);
	 try {
		out.write(total_pack);
		out.write(pack_no);
		out.write(data);
		out.flush();
		return outputStream.toByteArray();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 return null;
 }  
}

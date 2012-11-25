package main;

import java.util.Date;

public class HoldRequest {
	
	int hid;
	int bid;
	int callNumber;
	Date issuedDate;
	
	public HoldRequest(){
	}
	
	public HoldRequest(int hid, int bid, int callNumber, Date issuedDate){
		this.hid = hid;
		this.bid = bid;
		this.callNumber = callNumber;
		this.issuedDate = issuedDate;
	}
	
	public int getHid(){
		return this.hid;
	}
	public int getBid(){
		return this.bid;
	}
	public int getCallNumber(){
		return this.callNumber;
	}
	public Date getIssuedDate(){
		return this.issuedDate;
	}

}

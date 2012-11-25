package main;

import java.util.Date;

public class Borrowing {
	
	int borid;
	int bid;
	int callNumber;
	int copyNo;
	Date outDate;
	Date inDate;
	
	public Borrowing(){
	}
	public Borrowing(int borid, int bid, int callNumber, int copyNo, Date outDate, Date inDate){
		this();
		this.borid = borid;
		this.bid = bid;
		this.callNumber = callNumber;
		this.copyNo = copyNo;
		this.outDate = outDate;
		this.inDate = inDate;
	}
	public int getBorid(){
		return this.borid;
	}
	public int getBid(){
		return this.bid;
	}
	public int getCallNumber(){
		return this.callNumber;
	}
	public int getCopyNo(){
		return this.copyNo;
	}
	public Date getOutDate(){
		return this.outDate;
	}
	public Date getInDate(){
		return this.inDate;
	}
	
	public void printRecord(){
		// print record with items and due date.
	}
}

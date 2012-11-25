package main;

import java.util.Date;

public class Fine {
	
	int fid;
	int amount;
	Date issuedDate;
	Date paidDate;
	int borid;
	
	public Fine(){
	}
	
	public Fine(int fid, int amount, Date issuedDate, Date paidDate, int borid){
		this.fid = fid;
		this.amount = amount;
		this.issuedDate = issuedDate;
		this.paidDate = paidDate;
		this.borid = borid;
	}
	
	public int getFid(){
		return this.fid;
	}
	public int getAmount(){
		return this.fid;
	}
	public Date getIssuedDate(){
		return this.issuedDate;
	}
	public Date getPaidDate(){
		return this.paidDate;
	}
	public int getBorid(){
		return this.borid;
	}
	
	
}

package main;

import java.util.Date;

public class Borrower {

	private int bid;
	private String password;
	private String name;
	private String address;
	private int phone;
	private String emailAddress;
	private int sinOrStNo;
	private Date expiryDate;
	private String type;
	private int bookTimeLimit;

	public Borrower(){
	}

	public Borrower (int bid, String password, String name, String address, int phone, 
			String emailAddress, int sinOrStNo, Date expiryDate, String type) {
		this();
		this.bid = bid;
		this.password = password;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.emailAddress = emailAddress;
		this.sinOrStNo = sinOrStNo;
		this.expiryDate = expiryDate;
		this.type = type;
		
		if(type.equals("Student")){
			this.bookTimeLimit = 2;
		} else if(type.equals("Faculty")){
			this.bookTimeLimit = 12;
		} else if(type.equals("Staff")){
			this.bookTimeLimit = 6;
		}
		
	}

	public int getBid(){
		return this.bid;
	}
	public String getPassword(){
		return this.password;
	}
	public String getName(){
		return this.name;
	}
	public String getAddress(){
		return this.address;
	}
	public int getPhone(){
		return this.phone;
	}
	public String getEmail(){
		return this.emailAddress;
	}
	public int getSinOrStNo(){
		return this.sinOrStNo;
	}
	public Date getExpiryDate(){
		return this.expiryDate;
	}
	public String getType(){
		return this.type;
	}
	public int getBookTimeLimit(){
		return this.bookTimeLimit;
	}
	
	
	public Book[] searchBooks(String keyword){
		return null;
	}
	public void checkAccount(){
		// print account info
	}
	public void placeHoldRequest(){
		HoldRequest holdRequest = new HoldRequest();
	}
	public void payFine(Fine fine){
	}

}

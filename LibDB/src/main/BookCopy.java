package main;

public class BookCopy {
	Book book;
	int copyNo;
	String status;
	
	public BookCopy(){
	}
	public BookCopy(Book book, int copyNo, String status){
		this();
		this.book = book;
		this.copyNo = copyNo;
		this.status = status;
	}
	public String getStatus(){
		return status;
	}

}

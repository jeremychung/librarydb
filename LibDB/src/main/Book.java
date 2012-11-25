package main;

public class Book {

	private String callNumber;
	private int isbn;
	private String title;
	private String mainAuthor;
	private String publisher;
	private int year;

	public Book(){
	}

	public Book(String callNumber, int isbn, String title, String mainAuthor, String publisher, int year){
		this.callNumber = callNumber;
		this.isbn = isbn;
		this.title = title;
		this.mainAuthor = mainAuthor;
		this.publisher = publisher;
		this.year = year;
	}

	public String getCallNumber(){
		return this.callNumber;
	}
	public int getIsbn(){
		return this.isbn;
	}
	public String getTitle(){
		return this.title;
	}
	public String getMainAuthor(){
		return this.mainAuthor;
	}
	public String getPublisher(){
		return this.publisher;
	}
	public int getYear(){
		return this.year;
	}

}

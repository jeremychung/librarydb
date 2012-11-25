package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Librarian {
	private Connection con;
	private BufferedReader in;
	
	public Librarian(Connection con, BufferedReader in){
		this.con = con;
		this.in = in;
	}
	
	
	public void addBook(){
		
		String callNumber;
		int isbn;
		String title;
		String mainAuthor;
		String publisher;
		int year;

		PreparedStatement  ps;

		try{
			ps = con.prepareStatement("INSERT INTO book VALUES (?,?,?,?,?,?)");

			System.out.print("\nCall Number: ");
			callNumber = in.readLine();
			ps.setString(1, callNumber);

			System.out.print("\nISBN: ");
			isbn = Integer.parseInt(in.readLine());
			ps.setInt(2, isbn);

			System.out.print("\nTitle: ");
			title = in.readLine();
			ps.setString(3, title);
			
			System.out.print("\nMain Author: ");
			mainAuthor = in.readLine();
			ps.setString(4, mainAuthor);
			
			System.out.print("\nPublisher: ");
			publisher = in.readLine();
			ps.setString(5, publisher);
			
			System.out.print("\nYear: ");
			year = Integer.parseInt(in.readLine());
			ps.setInt(6, year);
			
			ps.executeUpdate();

			// commit work 
			con.commit();

			ps.close();
		}
		catch (IOException e)
		{
			System.out.println("IOException!");
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
			try 
			{
				// undo the insert
				con.rollback();	
			}
			catch (SQLException ex2)
			{
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
		}
	}
	public void checkedOutItems(){
		
	}
	public void popularItems(){
		
	}
}

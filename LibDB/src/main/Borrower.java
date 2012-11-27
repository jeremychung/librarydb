package main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.JOptionPane;

public class Borrower {
	private int bid;
	private String password;

	public void searchBooks(String title, String author, String subject){

		PreparedStatement  ps;
		ResultSet  rs;

		String titleStatement = "";
		String authStatement = "";
		String subStatement = "";

		if(!title.equals("")){
			titleStatement = 
					"(SELECT callNumber" +
							"FROM Book" +
							"WHERE title LIKE '%" +title+ "%')" +
							"UNION";
		}
		if(!author.equals("")){
			authStatement = 
					"(SELECT callNumber" +
							"FROM HasAuthor" +
							"WHERE name LIKE '%" +author+ "%')" +
							"UNION";
		}
		if(!subject.equals("")){
			subStatement = 
					"(SELECT callNumber" +
							"FROM Book" +
							"WHERE title LIKE '%" +subject+ "%')" +
							"UNION";
		}
		
		String queryString = titleStatement + authStatement + subStatement;
		queryString = queryString.substring(0, queryString.length()-5);
		
		try
		{
			ps = LibDB.con.prepareStatement(queryString);

			rs = ps.executeQuery();

			while(rs.next()){
				int callNumber = rs.getInt("callNumber");
				
				try{
					PreparedStatement  psAuth;
					ResultSet  rsAuth;
					
					psAuth = LibDB.con.prepareStatement("");
					
					
					

				} catch(SQLException exAuth){
					JOptionPane.showMessageDialog(null,
							"Message: " + exAuth.getMessage(),
							"Error",
							JOptionPane.ERROR_MESSAGE);

				}


			}
			// close the statement; 
			// the ResultSet will also be closed
			ps.close();
		}
		catch (SQLException ex)
		{
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void checkAccount(int bid, String password){

		// out items
		int borid;
		int callNumber;
		int copyNumber;
		Date outDate;

		// outstanding fees;
		int fid;
		int ammount;
		Date issuedDate;
		Date paidDate;

		Statement  stmt;
		ResultSet  rs;

		try
		{
			stmt = LibDB.con.createStatement();

			rs = stmt.executeQuery("SELECT (bid, passw FROM borrower");

			// get info on ResultSet
			ResultSetMetaData rsmd = rs.getMetaData();

			// get number of columns
			int numCols = rsmd.getColumnCount();

			System.out.println(" ");

			// display column names;
			for (int i = 0; i < numCols; i++)
			{
				// get column name and print it

				System.out.printf("%-15s", rsmd.getColumnName(i+1));    
			}

			System.out.println(" ");

			while(rs.next())
			{

			}

			// close the statement; 
			// the ResultSet will also be closed
			stmt.close();
		}
		catch (SQLException ex)
		{
			System.out.println("Message: " + ex.getMessage());
		}	

	}
	public void placeHoldRequest(){
		int callNumber;
	}
	public void payFine(){
		int fid;
		int amount;

	}

	public boolean authenticate(int bid, String password){

		String pass = null;
		PreparedStatement  ps;
		ResultSet  rs;

		try
		{
			ps = LibDB.con.prepareStatement("SELECT password FROM borrower WHERE bid=" + bid);

			rs = ps.executeQuery();

			if(rs.next()){
				pass = rs.getString("password");

			}
			// close the statement; 
			// the ResultSet will also be closed
			ps.close();
		}
		catch (SQLException ex)
		{
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}

		if(pass.equals(password)){
			return true;
		}else {
			JOptionPane.showMessageDialog(null,
					"Incorrect Password!",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

}

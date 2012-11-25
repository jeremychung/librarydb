package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Clerk {
	private Connection con;
	private BufferedReader in;

	public Clerk(Connection con, BufferedReader in){
		this.con = con;
		this.in = in;
	}

	public void addBorrower(Borrower borrower){

		int bid = borrower.getBid();
		String password = borrower.getPassword();
		String name = borrower.getName();
		String address = borrower.getAddress();
		int phone = borrower.getPhone();
		String email = borrower.getEmail();
		int sinOrStNo = borrower.getSinOrStNo();
		Date expiryDate = borrower.getExpiryDate();
		String type = borrower.getType();

		PreparedStatement  ps;

		try{
			ps = con.prepareStatement("INSERT INTO borrower VALUES (?,?,?,?,?,?,?,?,?)");

			System.out.print("\nBorrower ID: ");
			bid = Integer.parseInt(in.readLine());
			ps.setInt(1, bid);

			System.out.print("\nPassword: ");
			password = in.readLine();
			ps.setString(2, password);

			System.out.print("\nName: ");
			name = in.readLine();
			ps.setString(3, name);
			
			System.out.print("\nAddress: ");
			address = in.readLine();
			ps.setString(4, address);
			
			System.out.print("\nPhone: ");
			phone = Integer.parseInt(in.readLine());
			ps.setInt(5, phone);

			System.out.print("\nEmail: ");
			email = in.readLine();
			ps.setString(6, email);
			
			System.out.print("\nSIN/Student#: ");
			sinOrStNo = Integer.parseInt(in.readLine());
			ps.setInt(7, sinOrStNo);
			
			System.out.print("\nExpiry Date: ");
			ps.setDate(8, (java.sql.Date) expiryDate);
			
			System.out.print("\nType: ");
			type = in.readLine();
			ps.setString(9, type);


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


	public void checkOut(int bid, int[] callNumbers){
		// check out items
	}

	public void processReturn(int bid, int callNumber){
		// process return
	}

	public void checkOverdue(){

	}

}

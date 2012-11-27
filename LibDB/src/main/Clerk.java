package main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JOptionPane;

public class Clerk {

	public static void addBorrower(String password, String name, String address, int phone, 
			String email, int sinOrStNo, Date expiryDate, String type)
	{
		PreparedStatement  ps;

		try{
			ps = LibDB.con.prepareStatement("INSERT INTO borrower VALUES (borrower_counter.nextval,?,?,?,?,?,?,?,?)");

			//ps.setInt(1, bid);
			ps.setString(1, password);
			ps.setString(2, name);
			ps.setString(3, address);
			ps.setInt(4, phone);
			ps.setString(5, email);
			ps.setInt(6, sinOrStNo);
			java.sql.Date sqlDate = new java.sql.Date(expiryDate.getTime());
			ps.setDate(7, sqlDate);
			ps.setString(8, type);

			ps.executeUpdate();
			// commit work 
			LibDB.con.commit();
			ps.close();
			
			JOptionPane.showMessageDialog(null,
					"Borrower added.",
					"Information",
					JOptionPane.INFORMATION_MESSAGE);
		}
		catch (SQLException ex)
		{
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			try 
			{
				// undo the insert
				LibDB.con.rollback();	
			}
			catch (SQLException ex2)
			{
				JOptionPane.showMessageDialog(null,
						"Message: " + ex2.getMessage(),
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public static void checkoutBooks(int bid, String callNumbers){
		String[] callNums = callNumbers.split(";");
		for(String callNumber : callNums){
			checkOut(bid, callNumber.trim());
		}
	}

	// NOTE: Some functions still needs to be implemented including checking if the duedate has passed or not
	// Will have bugs since I wrote this in a text editor
	// Table creating code needs to be edited so that inDate can be null
	public static void checkOut(int bid, String callNumber)
	{
		if(expired(bid)==false){

			PreparedStatement ps;
			ResultSet rs;

			try
			{
				// Process borrowing
				ps = LibDB.con.prepareStatement( "SELECT * FROM BookCopy WHERE callNumber = ? AND status = 'in'" );
				ps.setString( 1, callNumber);
				rs = ps.executeQuery();

				// if true, a copy is available to be borrowed.
				if(rs.next())
				{
					PreparedStatement ps2;
					ResultSet rs2;
					int copyNo = rs.getInt("copyNo");	

					try{				

						ps2 = LibDB.con.prepareStatement("INSERT into Borrowing Values (borid_counter.nextval, ?, ?, sysdate, null)");
						ps2.setInt(2, bid);
						ps2.setString( 3, callNumber);
						ps2.setInt(4, copyNo);

						ps2.executeUpdate();

						String dueDate = getDuedate( callNumber, borrowerType );	// implement this function
						System.out.println( "Due Date of " + callNumber + " is: " + dueDate + "." );	// so that clerk can give a note to the borrower.
					}catch(SQLException ex){

					}
				}
				else
				{
					System.out.println( "No copies of " + callNumber + " is available." );
				}


				// commit work 
				LibDB.con.commit();
				ps.close();
			}
			catch (SQLException ex2)
			{
				JOptionPane.showMessageDialog(null,
						"Message: " + ex2.getMessage(),
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static void processReturn(String callNumber, int copyNo)
	{
		boolean onHold = false;
		int bid;
		int borid;
		java.sql.Date sqlDate;
		String type;

		PreparedStatement ps;
		ResultSet rs;

		try
		{
			// See if on hold
			ps = LibDB.con.prepareStatement("SELECT * FROM HoldRequest WHERE callNumber = ?");

			ps.setString(1, callNumber);

			rs = ps.executeQuery();

			if (rs.next())
				onHold = true;

			// if using indate as returned date
			ps = LibDB.con.prepareStatement( "SELECT bid, borid, outDate, type FROM Borrowing, Borrower, BookCopy WHERE status = 'out' AND callNumber = ? AND copyNo = ?" );
			ps.setString(1, callNumber);
			ps.setInt(2, copyNo);

			rs = ps.executeQuery();

			bid = rs.getInt( "bid" );
			borid = rs.getInt( "borid" );
			sqlDate = rs.getDate( "outDate" );
			type = rs.getString( "type" );

			// This function has to be implemented if using latter approach
			if ( checkDueDatePassed( sqlDate, type ) )
				generateFine();	// implement


			// update status of the item
			ps = LibDB.con.prepareStatement( "UPDATE BookCopy SET status = ? WHERE callNumber = ? AND copyNo = ?" );

			if ( onHOld )
				ps.setString( 1, "on hold" );	// also send a message to the holder
			else
				ps.setString( 1, "in" );

			ps.setString( 2, callNumber );
			ps.setString( 3, copyNo );

			ps.executeUpdate();

			// commit work 
			LibDB.con.commit();
			ps.close();
		}
		catch ( SQLException ex )
		{
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void checkOverdue()
	{
		Dictionary dueItem = new Hashtable();
		Statement stmt;
		ResultSet rs;

		int bid;
		String callNumber;
		String copyNo;
		String name;
		String eamilAddress;

		SimpleDateFormat fm = new SimpleDateFormat( "dd/MM/yy" ); // Edit as needed
		java.sql.Date sqlDate;
		java.util.Date utilDate;

		try
		{
			Statement stmt = LibDB.con.createStatement();

			// If we decide not to use indate as duedate, this line of code need to be changed.
			// Our table definition needs to be changed so that inDate can be null.
			ResultSet rs = stmt.executeQuery( "SELECT callNumber, copyNo, bid, name, emailAddress, inDate FROM Borrowing, Borrower WHERE sysdate > inDate and status = 'out'" );

			if ( rs.next() )
			{
				System.out.println( "Items that are past due are:" );
				System.out.println();
			}
			else
			{
				System.out.println( "No items are past due." );
			}

			while ( rs.next() )
			{
				bid = rs.getInt( "bid" );
				callNumber = rs.getString( "callNumber" );
				copyNo = rs.getString( "copyNo" );
				name = rs.getString( "name" );
				emailAddress = rs.getString( "emailAddress" );

				sqlDate = rs.getDate( "inDate" );	// get SQL Date type date, may not need it

				/*
				utilDate.setTime( sqlDate.getTime() );	// change date into Java Date type

				dateString = fm.format( utilDate );	// parse the date into a string using the format specified in fm
				 */

				// Change format to make it look nicer later
				System.out.println( "Item Call Number: " + callNumber );
				System.out.println( "Item Copy Number: " + copyNo );
				System.out.println( "Borrower ID: " + bid );
				System.out.println( "Name: " + name );
				System.out.println( "Email: " + emailAddress );
				System.out.println();
			}
		}
		catch ( SQLException ex )
		{
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public boolean onHold(String callNumber){
		boolean onHold = true;
		PreparedStatement ps;
		ResultSet rs;
		try{
			// See if on hold
			ps = LibDB.con.prepareStatement("SELECT * FROM HoldRequest WHERE callNumber = ?");

			ps.setString(1, callNumber);

			rs = ps.executeQuery();

			if(rs.next()){
				onHold = true;
			}else{
				onHold = false;
			}
		}catch(SQLException ex){
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return onHold;
	}

	public static boolean expired(int bid){

		boolean expired = true;
		PreparedStatement  ps;
		ResultSet  rs;

		try
		{
			ps = LibDB.con.prepareStatement("SELECT * FROM Borrower WHERE bid = ? AND expiryDate <= sysdate");

			rs = ps.executeQuery();

			if(!rs.next()){
				JOptionPane.showMessageDialog(null,
						"Account expired or does not exist!",
						"Error",
						JOptionPane.ERROR_MESSAGE);
				expired = true;
			}else 
				expired = false;

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
		return expired;
	}

}

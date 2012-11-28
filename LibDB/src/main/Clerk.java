package main;

import gui.ClerkPanel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class Clerk {

	public static void addBorrower(String password, String name, String address, int phone, 
			String email, int sinOrStNo, Date expiryDate, String type)
	{
		PreparedStatement  ps;

		try {
			ps = LibDB.con.prepareStatement("INSERT INTO borrower VALUES (bid_counter.nextval,?,?,?,?,?,?,?,?)");

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

	// TODO display/print receipt
	public static void checkoutBooks(int bid, String callNumbers) {
		// Check borrower account validity
		if (expired(bid)) {
			return;
		}

		String[] callNums = callNumbers.split(";");
		for(String callNumber : callNums) {
			checkOut(bid, callNumber.trim());
		}
	}


	// TODO remove HoldRequest once borrower picks up reserved copy.
	public static void checkOut(int bid, String callNumber) {

		PreparedStatement findCopyPs;
		ResultSet copyRs;
		PreparedStatement borPs;
		PreparedStatement statPs;

		int copyNo;
		int borrowDuration;

		// Try to find available copy
		try {
			findCopyPs = LibDB.con.prepareStatement( "SELECT * FROM BookCopy WHERE callNumber = ? AND status = 'in'" );
			findCopyPs.setString( 1, callNumber);

			copyRs = findCopyPs.executeQuery();


			// check to see if a copy is ready to be borrowed
			if(!copyRs.next()) {
				JOptionPane.showMessageDialog(null,
						"No Copies Available for " + callNumber,
						"Information",
						JOptionPane.INFORMATION_MESSAGE);
				findCopyPs.close();
				return;
			} else {
				copyNo = copyRs.getInt("copyNo");
				findCopyPs.close();

				borrowDuration = getBorrowDuration(getBorrowerType(bid));

				borPs = LibDB.con.prepareStatement("INSERT INTO Borrowing VALUES (borid_counter.nextval, ?, ?, ?, SYSDATE, SYSDATE + ?)");
				borPs.setInt(1, bid);
				borPs.setString( 2, callNumber);
				borPs.setInt(3, copyNo);
				borPs.setInt(4, borrowDuration);

				borPs.executeUpdate();
				LibDB.con.commit();
				borPs.close();

				// Update status of item copy
				statPs = LibDB.con.prepareStatement("UPDATE BookCopy SET status='out' WHERE BookCopy.callNumber = ? AND BookCopy.copyNo = ?");
				statPs.setString(1, callNumber);
				statPs.setInt(2, copyNo);

				statPs.executeUpdate();
				LibDB.con.commit();
				statPs.close();

				JOptionPane.showMessageDialog(null,
						"Checked out.",
						"Information",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			try {
				// undo the insert
				LibDB.con.rollback();
			} catch (SQLException ex2) {
				JOptionPane.showMessageDialog(null,
						"Message: " + ex2.getMessage(),
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public static void processReturn(String callNumber, int copyNo)
	{
		int bid;
		int borid;
		Date outDate;
		Date dueDate;
		java.sql.Date sqlDate;
		String type;

		PreparedStatement ps;
		ResultSet rs;

		// Find borrower of book
		try {
			// using inDate as dueDate
			ps = LibDB.con.prepareStatement(
					"SELECT borid, bid, outDate, inDate, status " +
							"FROM Borrowing, BookCopy " +
							"WHERE Borrowing.callNumber = BookCopy.callNumber AND Borrowing.CopyNo = BookCopy.copyNo AND callNumber = ? AND copyNo = ? AND status = 'out' " +
					"ORDER BY outDate DESC");
			ps.setString(1, callNumber);
			ps.setInt(2, copyNo);

			rs = ps.executeQuery();

			if (rs.next()) {
				if (rs.getString("status").equals("out")) {
					bid = rs.getInt("bid"); // Got the borrower.
					borid = rs.getInt("borid"); // For Fine
					outDate = rs.getDate("outDate"); // Also not very useful
					dueDate = rs.getDate("inDate"); // due date
				} else {
					JOptionPane.showMessageDialog(null,
							"Book is not checked out!",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					ps.close();
					return;
				}
			} else {
				JOptionPane.showMessageDialog(null,
						"Book was never checked out!",
						"Error",
						JOptionPane.ERROR_MESSAGE);
				ps.close();
				return;
			}
			ps.close();
		} catch (SQLException ex)	{
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// If book was overdue
		// Charge everybody $10
		if(new Date().after(dueDate)) {
			try {
				// Fine the borrower
				ps = LibDB.con.prepareStatement("INSERT INTO Fine VALUES (fid_counter.nextval, 10, SYSDATE, NULL, ?)");
				ps.setInt(1, borid);

				ps.executeUpdate();

				LibDB.con.commit();
				ps.close();
			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null,
						"Message: " + ex.getMessage(),
						"Error",
						JOptionPane.ERROR_MESSAGE);
				try {
					// undo the insert
					LibDB.con.rollback();
				} catch (SQLException ex2) {
					JOptionPane.showMessageDialog(null,
							"Message: " + ex2.getMessage(),
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}

		// TODO send email to hold requester
		// if book was on hold
		int bidHolding;
		if(onHold(callNumber)) {
			try {
				// Change item copy status to on hold.
				ps = LibDB.con.prepareStatement("UPDATE BookCopy SET status='on hold' WHERE BookCopy.callNumber = ? AND BookCopy.copyNo = ?");
				ps.setString( 1, callNumber);
				ps.setInt(2, copyNo);

				ps.executeUpdate();
				LibDB.con.commit();
				ps.close();

				// find hold requester and send message/email
				try {
					ps = LibDB.con.prepareStatement(
							"SELECT hid, bid, callNumber, issuedDate " +
									"FROM HoldRequest " +
									"WHERE callNumber = ? " +
							"ORDER BY issedDate ASC");
					ps.setString(1, callNumber);

					rs = ps.executeQuery();

					if (rs.next()) {
						bidHolding = rs.getInt("bid"); // get hold requester
					}

					ps.close();

				} catch (SQLException ex)	{
					JOptionPane.showMessageDialog(null,
							"Message: " + ex.getMessage(),
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

			} catch (SQLException ex) {
				JOptionPane.showMessageDialog(null,
						"Message: " + ex.getMessage(),
						"Error",
						JOptionPane.ERROR_MESSAGE);
				try {
					// undo the insert
					LibDB.con.rollback();
				} catch (SQLException ex2) {
					JOptionPane.showMessageDialog(null,
							"Message: " + ex2.getMessage(),
							"Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public static void checkOverdue()
	{
		PreparedStatement ps;
		ResultSet rs;

		int bid;
		String callNumber;
		String copyNo;
		Date outDate;
		Date dueDate;
		String name;
		String emailAddress;

		try
		{
			ps = LibDB.con.prepareStatement(
					"SELECT name, emailAddress, outDate, inDate, Borrower.bid, BookCopy.callNumber, BookCopy.copyNo " +
							"FROM Borrower, Borrowing, BookCopy " +
							"WHERE Borrowing.bid = Borrower.bid AND " +
							"Borrowing.callNumber = BookCopy.callNumber AND " +
							"Borrowing.copyNo = BookCopy.copyNo AND " +
					"inDate < TRUNC(SYSDATE)");
			rs = ps.executeQuery();

			while (rs.next())
			{
				callNumber = rs.getString("callNumber");
				copyNo = rs.getString("copyNo");
				bid = rs.getInt("bid");
				name = rs.getString("name");
				emailAddress = rs.getString("emailAddress");
				outDate = rs.getDate("outDate");
				dueDate = rs.getDate("inDate");

				ClerkPanel.overModel.insertRow(ClerkPanel.overdueTable.getRowCount(),new Object[]{callNumber, copyNo, bid, name, emailAddress, outDate, dueDate, new Boolean(false)});
			}
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

	public static boolean onHold(String callNumber) {
		boolean onHold = false;;
		PreparedStatement ps;
		ResultSet rs;
		try {
			// See if on hold
			ps = LibDB.con.prepareStatement("SELECT hid FROM HoldRequest WHERE callNumber = ?");
			ps.setString(1, callNumber);

			rs = ps.executeQuery();

			if(rs.next()){
				onHold = true;
			} else {
				onHold = false;
			}
		} catch(SQLException ex) {
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return onHold;
	}

	public static boolean expired(int bid) {

		boolean expired = true;
		PreparedStatement  ps;
		ResultSet  rs;

		try
		{
			// get expired borrowers
			ps = LibDB.con.prepareStatement("SELECT * FROM Borrower WHERE bid = ? AND expiryDate <= TRUNC(SYSDATE)");
			ps.setInt(1, bid);

			rs = ps.executeQuery();

			if (rs.next()) {
				JOptionPane.showMessageDialog(null,
						"Account expired or does not exist!",
						"Error",
						JOptionPane.ERROR_MESSAGE);
				expired = true;
			} else 
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

	private static String getBorrowerType(int bid) {

		PreparedStatement  ps;
		ResultSet  rs;
		String type;

		try
		{
			ps = LibDB.con.prepareStatement("SELECT type FROM Borrower WHERE bid = ?");
			ps.setInt(1, bid);

			rs = ps.executeQuery();

			if (rs.next()) {
				type = rs.getString("type");
				ps.close();
			} else {
				JOptionPane.showMessageDialog(null,
						"No type associated with Borrower account.",
						"Error",
						JOptionPane.ERROR_MESSAGE);
				ps.close();
				return null;
			}
			return type;

		}
		catch (SQLException ex)
		{
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

	public static int getBorrowDuration(String borrowerType) {
		int borrowDuration; // maximum borrow duration in days
		if (borrowerType.equals("Student")) {
			borrowDuration = 14;
		} else if (borrowerType.equals("Faculty")) {
			borrowDuration = 84;
		} else if (borrowerType.equals("Staff")) {
			borrowDuration = 42;
		} else {
			JOptionPane.showMessageDialog(null,
					"Unknown borrower acount type.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			borrowDuration = 0;
		}
		return borrowDuration;
	}

}

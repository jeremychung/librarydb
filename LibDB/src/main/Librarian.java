package main;

import gui.LibrarianPanel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JOptionPane;

public class Librarian {

	public static void addNewBook(int callNumber, int isbn, String title, String mainAuthor, String publisher, int year){

		PreparedStatement checkPs;
		PreparedStatement insertNewPs;
		PreparedStatement copyPs;
		PreparedStatement ps;

		ResultSet checkRs;
		ResultSet copyRs;

		try{
			checkPs = LibDB.con.prepareStatement("SELECT callNumber FROM Book WHERE Book.callNumber = ?");
			checkPs.setInt(1, callNumber);

			checkRs = checkPs.executeQuery();

			if ( !checkRs.next() ){
				insertNewPs = LibDB.con.prepareStatement("INSERT INTO Book VALUES (?,?,?,?,?,?)");

				insertNewPs.setInt(1, callNumber);
				insertNewPs.setInt(2, isbn);
				insertNewPs.setString(3, title);
				insertNewPs.setString(4, mainAuthor);
				insertNewPs.setString(5, publisher);
				insertNewPs.setInt(6, year);

				insertNewPs.executeUpdate();
				LibDB.con.commit();
				insertNewPs.close();
			}
			checkRs.close();

			int maxCopyNo = 0;

			copyPs = LibDB.con.prepareStatement("SELECT MAX(copyNo) FROM BookCopy WHERE BookCopy.callNumber = ?");
			copyPs.setInt(1, callNumber);

			copyRs = copyPs.executeQuery();

			if ( copyRs.next()) {
				maxCopyNo = copyRs.getInt("copyNo");
			}
			copyRs.close();

			maxCopyNo = maxCopyNo++;

			ps = LibDB.con.prepareStatement("INSERT INTO BookCopy VALUES (?,?,?)");
			ps.setInt(1, callNumber);
			ps.setInt(2, maxCopyNo);
			ps.setString(3, "in");

			ps.executeUpdate();
			LibDB.con.commit();
			ps.close();

		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			try {
				LibDB.con.rollback();	
			} catch (SQLException ex2) {
				JOptionPane.showMessageDialog(null,
						"Message: " + ex2.getMessage(),
						"Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public static void checkedOutItems(String subjects){
		String[] subjs = subjects.split(";");
		for(String subject : subjs){
			checkedOutItem(subject.trim());
		}
	}
	
	//Assume: inDate means dueDate
	//Assume: Empty Strings can be passed in as input if no subject

	public static void checkedOutItem(String subject){
		int callNumber;
		int copyNo;
		Date inDate;
		Date outDate;
		boolean dueFlag = false;

		PreparedStatement ps;
		ResultSet rs;

		try{
			if (subject.equals("")){
				ps = LibDB.con.prepareStatement("SELECT BookCopy.callNumber, BookCopy.copyNo, Borrowing.outDate, Borrowing.inDate " +
						"FROM BookCopy " +
						"LEFT JOIN Borrowing " +
						"ON BookCopy.callNumber=Borrowing.callNumber AND BookCopy.copyNo=Borrowing.copyNO " +
						"WHERE BookCopy.status = 'out' " +
						"ORDER BY BookCopy.callNumber");
			}else{
				ps = LibDB.con.prepareStatement("SELECT BookCopy.callNumber, BookCopy.copyNo, Borrowing.outDate, Borrowing.inDate " +
						"FROM BookCopy " +
						"LEFT JOIN Borrowing " +
						"ON BookCopy.callNumber=Borrowing.callNumber AND BookCopy.copyNo=Borrowing.copyNO " +
						"LEFT JOIN HasSubject " +
						"ON BookCopy.callNumber=HasSubject.callNumber " +
						"WHERE BookCopy.status = 'out' AND HasSubject.subject = ? " +
						"ORDER BY BookCopy.callNumber");
				ps.setString(1, subject);
			}

			rs = ps.executeQuery();

			while(rs.next()) {
				callNumber = rs.getInt("callNumber");

				copyNo = rs.getInt("copyNo");

				outDate = rs.getDate("outDate");

				Date d = new Date();
				inDate = rs.getDate("inDate");

				if ( (outDate.compareTo(d)) < 0  ) {
					dueFlag = true;
				}
				LibrarianPanel.outModel.insertRow(LibrarianPanel.viewOutTable.getRowCount(),new Object[]{callNumber, copyNo, outDate, inDate, new Boolean(dueFlag)});
			}
			rs.close();
		}catch(SQLException ex){
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void popularItems(int year, int n){
		int callNumber;
		int    isbn;
		String title;
		String mainAuthor;
		String publisher;
		int    bookyear;

		PreparedStatement ps;
		ResultSet rs;

		try{
			ps = LibDB.con.prepareStatement("SELECT * " +
					"FROM Book " +
					"WHERE callNumber IN (SELECT TOP ? callNumber, COUNT(*) " +
					"FROM Borrowing " +
					"WHERE YEAR = ? " +
					"GROUP BY callNumber " +
					"ORDER BY COUNT(*) DESC)");

			ps.setInt(1, n);
			ps.setInt(2, year);

			rs = ps.executeQuery();

			while(rs.next()) {
				callNumber = rs.getInt("callNumber");

				isbn = rs.getInt("isbn");

				title = rs.getString("title");

				mainAuthor = rs.getString("mainAuthor");

				publisher = rs.getString("publisher");

				bookyear = rs.getInt("year");

				LibrarianPanel.popModel.insertRow(LibrarianPanel.viewPopTable.getRowCount(),new Object[]{callNumber, isbn, title, mainAuthor, publisher, bookyear});
			}

			rs.close();

		}catch(SQLException ex){
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}

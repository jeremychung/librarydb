package main;

import gui.LibrarianPanel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JOptionPane;

public class Librarian {

	public static void addNewBook(String callNumber, int isbn, String title, String mainAuthor, String publisher, int year){

		PreparedStatement checkPs;
		PreparedStatement insertNewPs;
		PreparedStatement insertNewCopyPs;
		PreparedStatement addAuthPs;
		PreparedStatement copyPs;
		PreparedStatement ps;

		ResultSet checkRs;
		ResultSet copyRs;

		try{
			checkPs = LibDB.con.prepareStatement("SELECT callNumber FROM Book WHERE Book.callNumber = ?");
			checkPs.setString(1, callNumber);

			checkRs = checkPs.executeQuery();

			if ( !checkRs.next() ){
				insertNewPs = LibDB.con.prepareStatement("INSERT INTO Book VALUES (?,?,?,?,?,?)");
				insertNewPs.setString(1, callNumber);
				insertNewPs.setInt(2, isbn);
				insertNewPs.setString(3, title);
				insertNewPs.setString(4, mainAuthor);
				insertNewPs.setString(5, publisher);
				insertNewPs.setInt(6, year);

				insertNewPs.executeUpdate();
				LibDB.con.commit();
				insertNewPs.close();

				insertNewCopyPs = LibDB.con.prepareStatement("INSERT INTO BookCopy VALUES (?,?,?)");
				insertNewCopyPs.setString(1, callNumber);
				insertNewCopyPs.setInt(2, 1);
				insertNewCopyPs.setString(3, "in");

				insertNewCopyPs.executeUpdate();
				LibDB.con.commit();
				insertNewCopyPs.close();

				addAuthPs = LibDB.con.prepareStatement("INSERT INTO hasAuthor VALUES (?,?)");
				addAuthPs.setString(1, callNumber);
				addAuthPs.setString(2, mainAuthor);

				addAuthPs.executeUpdate();
				LibDB.con.commit();
				addAuthPs.close();

				JOptionPane.showMessageDialog(null,
						"Book added.",
						"Information",
						JOptionPane.INFORMATION_MESSAGE);
			}

			else{
				int maxCopyNo = 0;

				copyPs = LibDB.con.prepareStatement("SELECT MAX(copyNo) FROM BookCopy WHERE BookCopy.callNumber = ?");
				copyPs.setString(1, callNumber);

				copyRs = copyPs.executeQuery();

				if (copyRs.next()) {
					maxCopyNo = copyRs.getInt("MAX(copyNo)");

					ps = LibDB.con.prepareStatement("INSERT INTO BookCopy VALUES (?,?,?)");
					ps.setString(1, callNumber);
					ps.setInt(2, maxCopyNo+1);
					ps.setString(3, "in");

					ps.executeUpdate();
					LibDB.con.commit();
					ps.close();

					JOptionPane.showMessageDialog(null,
							"Copy added.",
							"Information",
							JOptionPane.INFORMATION_MESSAGE);
				}
				copyRs.close();

			}
			checkRs.close();

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

	public static void popularItems(int n, int year){
		String callNumber;
		int    isbn;
		String title;
		String mainAuthor;
		String publisher;
		int    bookyear;

		PreparedStatement ps;
		PreparedStatement searchPs;
		ResultSet rs;
		ResultSet searchRs;

		try{
			ps = LibDB.con.prepareStatement("SELECT callNumber, COUNT(*) as count " +
					"FROM Borrowing " +
					"WHERE TO_CHAR(outDate, 'YYYY') = ? " +
					"GROUP BY callNumber " +
					"ORDER BY count DESC ");

			ps.setInt(1, year);

			rs = ps.executeQuery();

			while(n>0 && rs.next()) {
				callNumber = rs.getString("callNumber");

				LibrarianPanel.popModel.insertRow(LibrarianPanel.viewPopTable.getRowCount(),new Object[]{callNumber});
				n--;
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

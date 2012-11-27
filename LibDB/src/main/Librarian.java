package main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
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
	//Assume: inDate means dueDate
	//Assume: Empty Strings can be passed in as input if no subject
	
	public void checkedOutItems(String subject){
		String callNumber;
		int copyNo;
		Date inDate;
		Date outDate;
		
		PreparedStatement ps;
		ResultSet rs;

		try{
			if (subject.equals("")){
				ps = LibDB.con.prepareStatement("SELECT BookCopy.callNumber, BookCopy.copyNo, Borrowing.outDate, Borrowing.inDate" +
												"FROM BookCopy" +
												"LEFT JOIN Borrowing" +
												"ON BookCopy.callNumber=Borrowing.callNumber AND BookCopy.copyNo=Borrowing.copyNO" +
												"WHERE BookCopy.status = 'out'" +
												"ORDER BY BookCopy.callNumber");
			}else{
				ps = LibDB.con.prepareStatement("SELECT BookCopy.callNumber, BookCopy.copyNo, Borrowing.outDate, Borrowing.inDate" +
												"FROM BookCopy" +
												"LEFT JOIN Borrowing" +
												"ON BookCopy.callNumber=Borrowing.callNumber AND BookCopy.copyNo=Borrowing.copyNO" +
												"LEFT JOIN HasSubject" +
												"ON BookCopy.callNumber=HasSubject.callNumber" +
												"WHERE BookCopy.status = 'out' AND HasSubject.subject = ?" +
												"ORDER BY BookCopy.callNumber");
				ps.setString(1, subject);
			}
			
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			int numCols = rsmd.getColumnCount();

			System.out.println(" ");

			for (int i = 0; i < numCols; i++) {
				System.out.printf("%-15s", rsmd.getColumnName(i+1));    
			}

			System.out.println(" ");

			while(rs.next()) {
				callNumber = rs.getString("callNumber");
				System.out.printf("%-16s", callNumber);
				
				copyNo = rs.getInt("copyNo");
				System.out.printf("%-5s", copyNo);
				
				outDate = rs.getDate("outDate");
				System.out.printf("%-10s", outDate);
				
				Date d = new Date();
				inDate = rs.getDate("inDate");
				System.out.printf("%-10s", inDate);
				if ( (outDate.compareTo(d)) < 0  ) {
					System.out.printf(" OVERDUE! ");
				}
			}
			rs.close();
		}catch(SQLException ex){
			JOptionPane.showMessageDialog(null,
					"Message: " + ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
		
//Need to find out how to flag things

	public void popularItems(int year, int n){
		String callNumber;
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

			ResultSetMetaData rsmd = rs.getMetaData();

			int numCols = rsmd.getColumnCount();

			System.out.println(" ");

			for (int i = 0; i < numCols; i++) {
				System.out.printf("%-15s", rsmd.getColumnName(i+1));    
			}

			System.out.println(" ");

			while(rs.next()) {
				callNumber = rs.getString("callNumber");
				System.out.printf("%-16s", callNumber);

				isbn = rs.getInt("isbn");
				System.out.printf("%-20s", isbn);

				title = rs.getString("title");
				System.out.printf("%-20s", title);

				mainAuthor = rs.getString("mainAuthor");
				System.out.printf("%-20s", mainAuthor);

				publisher = rs.getString("publisher");
				System.out.printf("%-20s", publisher);

				bookyear = rs.getInt("year");
				System.out.printf("%-20s", bookyear);
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

package main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Librarian {

	public static void addBook(String callNumber, int isbn, String title, String mainAuthor, String publisher, int year){

		PreparedStatement  ps;

		try{
			ps = LibDB.con.prepareStatement("INSERT INTO book VALUES (?,?,?,?,?,?)");

			ps.setString(1, callNumber);
			ps.setInt(2, isbn);
			ps.setString(3, title);
			ps.setString(4, mainAuthor);
			ps.setString(5, publisher);
			ps.setInt(6, year);

			ps.executeUpdate();
			// commit work 
			LibDB.con.commit();
			ps.close();
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
	public void checkedOutItems(){

	}

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

package main;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
	public void popularItems(){
		
	}
}

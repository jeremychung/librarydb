package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Librarian {
	private Connection con;

	public Librarian(){
		this.con = LibDB.con;
	}
	
	public void addBook(Book book){
		
		String callNumber = book.getCallNumber();
		int isbn = book.getIsbn();
		String title = book.getTitle();
		String mainAuthor = book.getMainAuthor();
		String publisher = book.getPublisher();
		int year = book.getYear();

		PreparedStatement  ps;

		try{
			ps = con.prepareStatement("INSERT INTO book VALUES (?,?,?,?,?,?)");

			ps.setString(1, callNumber);
			ps.setInt(2, isbn);
			ps.setString(3, title);
			ps.setString(4, mainAuthor);
			ps.setString(5, publisher);
			ps.setInt(6, year);

			ps.executeUpdate();
			// commit work 
			con.commit();
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
				con.rollback();	
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

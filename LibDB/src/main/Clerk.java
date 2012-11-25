package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JOptionPane;

public class Clerk {
	private Connection con;

	public Clerk(Connection con){
		this.con = con;
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

			ps.setInt(1, bid);
			ps.setString(2, password);
			ps.setString(3, name);
			ps.setString(4, address);
			ps.setInt(5, phone);
			ps.setString(6, email);
			ps.setInt(7, sinOrStNo);
			java.sql.Date sqlDate = new java.sql.Date(expiryDate.getTime());
			ps.setDate(8, sqlDate);
			ps.setString(9, type);

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


	public void checkOut(int bid, int[] callNumbers){
		// check out items
	}
	public void processReturn(int bid, int callNumber){
		// process return
	}
	public void checkOverdue(){
	}

}

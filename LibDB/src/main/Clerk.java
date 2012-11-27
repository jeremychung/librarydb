package main;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.JOptionPane;

public class Clerk {

	public static void addBorrower(String password, String name, String address, int phone, 
			String email, int sinOrStNo, Date expiryDate, String type){

		PreparedStatement  ps;

		try{
			ps = LibDB.con.prepareStatement("INSERT INTO borrower VALUES (borrower_counter.nextval,?,?,?,?,?,?,?,?)");

			//ps.setInt(1, bid);
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

	public void checkOut(int bid, int[] callNumbers){
		// check out items
	}
	public void processReturn(int bid, int callNumber){
		// process return
	}
	public void checkOverdue(){
	}

}

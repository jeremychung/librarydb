package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import main.Borrower;
import main.Clerk;

public class ClerkPanel {

	private JTextField bidField;
	private JTextField passwordField;
	private JTextField nameField;
	private JTextField addressField;
	private JTextField phoneField;
	private JTextField emailField;
	private JTextField sinOrStNoField;
	private JFormattedTextField expiryDateField;
	private JComboBox typeComboBox;
	private JPanel mainPanel;
	private Clerk clerk;

	public ClerkPanel(){
	}

	private void openAddBorrowerForm(){
		// Add borrower Form
		JPanel addBorrowerForm = new JPanel();
		// Set form layout
		addBorrowerForm.setLayout(new GridLayout(0, 2, 10, 10));
		addBorrowerForm.setBorder(new EmptyBorder(10, 10, 10, 10) );

		// Field Labels
		JLabel bidLabel = new JLabel("Bid: ");
		JLabel passwordLabel = new JLabel("Password: ");
		JLabel nameLabel = new JLabel("Name: ");
		JLabel addressLabel = new JLabel("Address: ");
		JLabel phoneLabel = new JLabel("Phone: ");
		JLabel emailLabel = new JLabel("Email Address: ");
		JLabel sinOrStNoLabel = new JLabel("SIN/Student #: ");
		JLabel expiryDateLabel = new JLabel("Expiry Date(dd/mm/yyyy): ");
		JLabel typeLabel = new JLabel("Type: ");

		// Fields
		bidField = new JTextField(10);
		passwordField = new JTextField(10);
		nameField = new JTextField(10);
		addressField = new JTextField(10);
		phoneField = new JTextField(10);
		emailField = new JTextField(10);
		sinOrStNoField = new JTextField(10);
		expiryDateField = new JFormattedTextField(new SimpleDateFormat("dd/MM/yyyy"));
		
		//typeField = new JTextField(10);
		String[] types = {"", "Student", "Faculty", "Staff"};
		typeComboBox = new JComboBox(types);
		
		// Buttons
		JButton addButton = new JButton("Add");
		JButton cancelButton = new JButton("Cancel");

		// Add components to panel
		addBorrowerForm.add(bidLabel);
		addBorrowerForm.add(bidField);
		addBorrowerForm.add(passwordLabel);
		addBorrowerForm.add(passwordField);
		addBorrowerForm.add(nameLabel);
		addBorrowerForm.add(nameField);
		addBorrowerForm.add(addressLabel);
		addBorrowerForm.add(addressField);
		addBorrowerForm.add(phoneLabel);
		addBorrowerForm.add(phoneField);
		addBorrowerForm.add(emailLabel);
		addBorrowerForm.add(emailField);
		addBorrowerForm.add(sinOrStNoLabel);
		addBorrowerForm.add(sinOrStNoField);
		addBorrowerForm.add(expiryDateLabel);
		addBorrowerForm.add(expiryDateField);
		addBorrowerForm.add(typeLabel);
		addBorrowerForm.add(typeComboBox);

		addBorrowerForm.add(addButton);
		addBorrowerForm.add(cancelButton);

		// Window
		final JFrame frame = new JFrame("Add Borrower");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(300, 400);
		//Add content to the window.
		frame.add(addBorrowerForm, BorderLayout.CENTER);
		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// Button Listeners
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				int bid = 0;
				try{
					bid = Integer.parseInt(bidField.getText());
				}
				catch(NumberFormatException numExcept){
					JOptionPane.showMessageDialog(null,
							"Invalid BID.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				};
				
				String password = passwordField.getText();
				if (password.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please fill in a password.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String name = nameField.getText();
				if (name.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please fill in a name.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String address = addressField.getText();
				if (address.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please fill in an address.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}


				int phone = 0;
				try{
					phone = Integer.parseInt(phoneField.getText());
				}
				catch(NumberFormatException numExcept){
					JOptionPane.showMessageDialog(null,
							"Invalid phone number.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				};

				String email = emailField.getText();
				if (email.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please fill in an email address.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int sinOrStNo = 0;
				try{
					sinOrStNo = Integer.parseInt(sinOrStNoField.getText());
				}
				catch(NumberFormatException numExcept){
					JOptionPane.showMessageDialog(null,
							"Invalid SIN/Student number.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				};

				Date expiryDate = (Date) expiryDateField.getValue();
				if (expiryDate.equals(null)) {
					JOptionPane.showMessageDialog(null,
							"Please fill in an expiry date.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String type = (String)typeComboBox.getSelectedItem();
				if (type.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select borrower type.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Borrower borrower = new Borrower(bid, password, name, address, phone, email, sinOrStNo, expiryDate, type);
				clerk = new Clerk();
				clerk.addBorrower(borrower);
				frame.setVisible(false);
			}
		});

		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
	}

	public JComponent getClerkPanel(){

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 2, 10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10) );

		JButton addBorrowerButton = new JButton("Add Borrower");
		JButton checkoutButton = new JButton("Checkout");
		JButton processReturnButton = new JButton("Process Return");
		JButton checkOverdueButton = new JButton("Check Overdue");

		addBorrowerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//Execute when button is pressed
				openAddBorrowerForm();
			}
		});  

		checkoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//Execute when button is pressed
			}
		});  

		processReturnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//Execute when button is pressed
			}
		});
		checkOverdueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//Execute when button is pressed
			}
		});  

		mainPanel.add(addBorrowerButton);
		mainPanel.add(checkoutButton);
		mainPanel.add(processReturnButton);
		mainPanel.add(checkOverdueButton);

		return mainPanel;

	}
}

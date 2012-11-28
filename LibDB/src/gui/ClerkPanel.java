package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import main.Clerk;

public class ClerkPanel {

	// Add Borrower form fields
	private JTextField passwordField;
	private JTextField nameField;
	private JTextField addressField;
	private JTextField phoneField;
	private JTextField emailField;
	private JTextField sinOrStNoField;
	private JFormattedTextField expiryDateField;
	private JComboBox typeComboBox;
	
	// Checkout form fields
	private JTextField bidField;
	private JTextField callNumberField;
	
	// Return form fields;
	private JTextField copyNoField;
	
	public static DefaultTableModel overModel;
	public static JTable overdueTable;
	
	private JPanel mainPanel;

	public ClerkPanel(){
	}

	private void openAddBorrowerForm(){
		// Add borrower Form
		JPanel addBorrowerForm = new JPanel();
		// Set form layout
		addBorrowerForm.setLayout(new GridLayout(0, 2, 10, 10));
		addBorrowerForm.setBorder(new EmptyBorder(10, 10, 10, 10) );

		// Field Labels
		JLabel passwordLabel = new JLabel("Password: ");
		JLabel nameLabel = new JLabel("Name: ");
		JLabel addressLabel = new JLabel("Address: ");
		JLabel phoneLabel = new JLabel("Phone: ");
		JLabel emailLabel = new JLabel("Email Address: ");
		JLabel sinOrStNoLabel = new JLabel("SIN/Student #: ");
		JLabel expiryDateLabel = new JLabel("Expiry Date(dd/mm/yyyy): ");
		JLabel typeLabel = new JLabel("Type: ");

		// Fields
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

				Clerk.addBorrower(password, name, address, phone, email, sinOrStNo, expiryDate, type);
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
	
	private void openCheckoutForm(){
		// Add checkout Form
		JPanel checkoutForm = new JPanel();
		// Set form layout
		checkoutForm.setLayout(new GridLayout(0, 1, 10, 10));
		checkoutForm.setBorder(new EmptyBorder(10, 10, 10, 10) );

		// Field Labels
		JLabel bidLabel = new JLabel("Bid: ");
		JLabel callNumberLabel = new JLabel("Call Numbers(separated by ;): ");
		// Fields
		bidField = new JTextField(10); 
		callNumberField = new JTextField(10);
		
		// Buttons
		JButton checkoutButton = new JButton("Checkout");
		JButton cancelButton = new JButton("Cancel");

		// Add components to panel
		checkoutForm.add(bidLabel);
		checkoutForm.add(bidField);
		checkoutForm.add(callNumberLabel);
		checkoutForm.add(callNumberField);		
		checkoutForm.add(checkoutButton);
		checkoutForm.add(cancelButton);

		// Window
		final JFrame frame = new JFrame("Checkout");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(200, 250);
		//Add content to the window.
		frame.add(checkoutForm, BorderLayout.CENTER);
		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// Button Listeners
		checkoutButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				int bid = 0;
				try{
					bid = Integer.parseInt(bidField.getText());
				}
				catch(NumberFormatException numExcept){
					JOptionPane.showMessageDialog(null,
							"Invalid bid.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				};
				
				String callNumber = callNumberField.getText();
				if (callNumber.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please fill in call numbers.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				Clerk.checkoutBooks(bid, callNumber);
			}
		});

		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
	}
	
	private void openReturnForm(){
		// Add checkout Form
		JPanel returnForm = new JPanel();
		// Set form layout
		returnForm.setLayout(new GridLayout(0, 2, 10, 10));
		returnForm.setBorder(new EmptyBorder(10, 10, 10, 10) );

		// Field Labels
		JLabel callNumberLabel = new JLabel("Call Number: ");
		JLabel copyNoLabel = new JLabel("Copy Number: ");
		// Fields
		callNumberField = new JTextField(10);
		copyNoField = new JTextField(10);
		
		// Buttons
		JButton returnButton = new JButton("Return");
		JButton cancelButton = new JButton("Cancel");

		// Add components to panel
		returnForm.add(callNumberLabel);
		returnForm.add(callNumberField);
		returnForm.add(copyNoLabel);
		returnForm.add(copyNoField);
		returnForm.add(returnButton);
		returnForm.add(cancelButton);

		// Window
		final JFrame frame = new JFrame("Process Return");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(300, 150);
		//Add content to the window.
		frame.add(returnForm, BorderLayout.CENTER);
		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// Button Listeners
		returnButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{				
				String callNumber = callNumberField.getText();
				if (callNumber.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please fill in call number.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int copyNo = 0;
				try{
					copyNo = Integer.parseInt(bidField.getText());
				}
				catch(NumberFormatException numExcept){
					JOptionPane.showMessageDialog(null,
							"Invalid copy number.",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				};
				
				Clerk.processReturn(callNumber, copyNo);
			}
		});

		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
	}
	
	private void openOverdueForm(){
		// Add check overdue Form
		JPanel overdueForm = new JPanel();
		
		final String[] columnNames = {"Call Number", "Copy #", "Bid", "Name", "Email Address", "Out Date", "Due Date", "Select"};

		Object[][] data = {};

		overModel = new DefaultTableModel(data,columnNames);
		
	
		// Add table to view items
		overdueTable = new JTable(overModel);
		
		TableColumn tc = overdueTable.getColumnModel().getColumn(7);  
        tc.setCellEditor(overdueTable.getDefaultEditor(Boolean.class));  
        tc.setCellRenderer(overdueTable.getDefaultRenderer(Boolean.class));
		
		
		// Add table to view items
		JScrollPane scrollPane = new JScrollPane(overdueTable);
		
		// Buttons
		JButton sendSeleButton = new JButton("Send to selected");
		JButton sendAllButton = new JButton("Send to All");
		JButton closeButton = new JButton("Close");
		// Button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(sendSeleButton);
		buttonPanel.add(sendAllButton);
		buttonPanel.add(closeButton);

		// Add components to panel
		scrollPane.setPreferredSize(new Dimension(480, 200));
		overdueForm.add(scrollPane, BorderLayout.CENTER);
		overdueForm.add(buttonPanel, BorderLayout.PAGE_END);

		// Window
		final JFrame frame = new JFrame("Overdue Items");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(500, 275);
		//Add content to the window.
		frame.add(overdueForm, BorderLayout.CENTER);
		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// Button Listeners
		sendSeleButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{			
				ArrayList<String> bids = new ArrayList<String>();
				for(int x=0; x<overModel.getRowCount(); x++){
					if(overModel.getValueAt(x, 5).equals(true)){
						bids.add((String) overModel.getValueAt(x, 3));
					}
				}
				for(String bid : bids){
					//System.out.println(bid);
				}
			}
		});
		
		sendAllButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{			
				ArrayList<String> bids = new ArrayList<String>();
				for(int x=0; x<overModel.getRowCount(); x++){
						bids.add((String) overModel.getValueAt(x, 3));
				}
				for(String bid : bids){
					//System.out.println(bid);
				}
			}
		});

		closeButton.addActionListener(new ActionListener(){
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
				openCheckoutForm();
			}
		});  

		processReturnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//Execute when button is pressed
				openReturnForm();
			}
		});
		checkOverdueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//Execute when button is pressed
				openOverdueForm();
				Clerk.checkOverdue();
			}
		});  

		mainPanel.add(addBorrowerButton);
		mainPanel.add(checkoutButton);
		mainPanel.add(processReturnButton);
		mainPanel.add(checkOverdueButton);

		return mainPanel;

	}
}

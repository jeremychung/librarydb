package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import main.Borrower;
import main.Clerk;


public class BorrowerPanel {
	
	// Search fields
	private JTextField titleField;
	private JTextField authorField;
	private JTextField subjectField;
	
	public static DefaultTableModel resultsModel;
	public static JTable resultsTable;

	private JPanel mainPanel;
	
	public BorrowerPanel(){
	}
	
	private void openSearchForm(){
		// Add borrower Form
		JPanel searchForm = new JPanel();
		// Set form layout
		searchForm.setLayout(new GridLayout(0, 2, 10, 10));
		searchForm.setBorder(new EmptyBorder(10, 10, 10, 10) );

		// Field Labels
		JLabel titleLabel = new JLabel("Title: ");
		JLabel authorLabel = new JLabel("Author: ");
		JLabel subjectLabel = new JLabel("Subject: ");
		// Fields
		titleField = new JTextField(10);
		authorField = new JTextField(10);
		subjectField = new JTextField(10);
		
		// Buttons
		JButton searchButton = new JButton("Search");
		JButton cancelButton = new JButton("Cancel");

		// Add components to panel
		searchForm.add(titleLabel);
		searchForm.add(titleField);
		searchForm.add(authorLabel);
		searchForm.add(authorField);
		searchForm.add(subjectLabel);
		searchForm.add(subjectField);
		searchForm.add(searchButton);
		searchForm.add(cancelButton);

		// Window
		final JFrame frame = new JFrame("Search");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(300, 300);
		//Add content to the window.
		frame.add(searchForm, BorderLayout.CENTER);
		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// Button Listeners
		searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				openResultsForm();
				String title = titleField.getText().trim();
				String author = authorField.getText().trim();
				String subject = subjectField.getText().trim();
				Borrower.searchBooks(title, author, subject);
			}
		});

		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
	}
	
	public static void openResultsForm(){
		// Add check overdue Form
		JPanel resultsForm = new JPanel();
		
		final String[] columnNames = {"Call Number", "Copies In", "Copies Out"};
		Object[][] data = {};

		resultsModel = new DefaultTableModel(data,columnNames);

	
		// Add table to view items
		resultsTable = new JTable(resultsModel);
		

//		resultsModel.insertRow(resultsTable.getRowCount(),new Object[]{callNumber, numCopiesIn, numCopiesOut});
//		model.insertRow(resultsTable.getRowCount(),new Object[]{"Call2", "2", "Book2", "date2", "date22", new Boolean(false)});
//		model.insertRow(resultsTable.getRowCount(),new Object[]{"Call3", "3", "Book3", "date3", "date33", new Boolean(false)});
//		model.insertRow(resultsTable.getRowCount(),new Object[]{"Call4", "4", "Book4", "date4", "date44", new Boolean(false)});
		
		
		// Add table to view items
		JScrollPane scrollPane = new JScrollPane(resultsTable);
		
		JButton closeButton = new JButton("Close");
		
		// Add components to panel
		scrollPane.setPreferredSize(new Dimension(480, 200));
		resultsForm.add(scrollPane, BorderLayout.PAGE_START);
		resultsForm.add(closeButton, BorderLayout.PAGE_END);

		// Window
		final JFrame frame = new JFrame("Search Results");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(500, 270);
		//Add content to the window.
		frame.add(resultsForm, BorderLayout.CENTER);
		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// Button Listeners
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
	}
	
	private void openAccountForm(){
		// Add checkout Form
		JPanel accountForm = new JPanel();
		// Set form layout
		accountForm.setLayout(new GridLayout(0, 2, 10, 10));
		accountForm.setBorder(new EmptyBorder(10, 10, 10, 10) );
		
		// Buttons
		JButton borrowedButton = new JButton("Borrowed Items");
		JButton finesButton = new JButton("Oustanding Fines");
		JButton holdButton = new JButton("Items on Hold");
		JButton closeButton = new JButton("Close");
		
		// Add components to panel
		accountForm.add(borrowedButton);
		accountForm.add(finesButton);
		accountForm.add(holdButton);
		accountForm.add(closeButton);

		// Window
		final JFrame frame = new JFrame("Account Information");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(300, 125);
		//Add content to the window.
		frame.add(accountForm, BorderLayout.CENTER);
		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// Button Listeners
		borrowedButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{				
				openBorrowedForm();
			}
		});
		
		finesButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				openFinesForm();
			}
		});
		
		holdButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{				
				openViewHoldForm();
			}
		});

		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
	}
	
	private void openHoldForm(){
		// Add borrower Form
		JPanel holdForm = new JPanel();
		// Set form layout
		holdForm.setLayout(new GridLayout(0, 2, 10, 10));
		holdForm.setBorder(new EmptyBorder(10, 10, 10, 10) );

		// Field Labels
		JLabel callNumberLabel = new JLabel("Call Number: ");
		// Fields
		JTextField callNumberField = new JTextField(10);
		
		// Buttons
		JButton holdButton = new JButton("Place Hold");
		JButton cancelButton = new JButton("Cancel");

		// Add components to panel
		holdForm.add(callNumberLabel);
		holdForm.add(callNumberField);
		holdForm.add(holdButton);
		holdForm.add(cancelButton);

		// Window
		final JFrame frame = new JFrame("Hold Request");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(300, 125);
		//Add content to the window.
		frame.add(holdForm, BorderLayout.CENTER);
		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// Button Listeners
		holdButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
			
			}
		});

		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
	}
	
	private void openBorrowedForm(){
		// Add check overdue Form
		JPanel borrowedForm = new JPanel();
		
		final String[] columnNames = {"Call Number", "Copy #", "Title", "Out Date", "Due Date", "Out"};
		Object[][] data = {};

		final DefaultTableModel model = new DefaultTableModel(data,columnNames);

	
		// Add table to view items
		JTable borrowedTable = new JTable(model);
		
		TableColumn tc = borrowedTable.getColumnModel().getColumn(5);  
        tc.setCellEditor(borrowedTable.getDefaultEditor(Boolean.class));  
        tc.setCellRenderer(borrowedTable.getDefaultRenderer(Boolean.class));

		model.insertRow(borrowedTable.getRowCount(),new Object[]{"Call1", "1", "Book1", "date1", "date11", new Boolean(false)});
		model.insertRow(borrowedTable.getRowCount(),new Object[]{"Call2", "2", "Book2", "date2", "date22", new Boolean(false)});
		model.insertRow(borrowedTable.getRowCount(),new Object[]{"Call3", "3", "Book3", "date3", "date33", new Boolean(false)});
		model.insertRow(borrowedTable.getRowCount(),new Object[]{"Call4", "4", "Book4", "date4", "date44", new Boolean(false)});
		
		
		// Add table to view items
		JScrollPane scrollPane = new JScrollPane(borrowedTable);
		
		JButton closeButton = new JButton("Close");
		
		// Add components to panel
		scrollPane.setPreferredSize(new Dimension(480, 200));
		borrowedForm.add(scrollPane, BorderLayout.PAGE_START);
		borrowedForm.add(closeButton, BorderLayout.PAGE_END);

		// Window
		final JFrame frame = new JFrame("Borrowed Items");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(500, 270);
		//Add content to the window.
		frame.add(borrowedForm, BorderLayout.CENTER);
		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// Button Listeners
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
	}

	private void openViewHoldForm(){
		// Add check overdue Form
		JPanel viewHoldForm = new JPanel();
		
		final String[] columnNames = {"Call Number", "Copy #", "Title", "Out Date", "Due Date", "Out"};
		Object[][] data = {};

		final DefaultTableModel model = new DefaultTableModel(data,columnNames);

	
		// Add table to view items
		JTable viewHoldTable = new JTable(model);
		
		TableColumn tc = viewHoldTable.getColumnModel().getColumn(5);  
        tc.setCellEditor(viewHoldTable.getDefaultEditor(Boolean.class));  
        tc.setCellRenderer(viewHoldTable.getDefaultRenderer(Boolean.class));

		model.insertRow(viewHoldTable.getRowCount(),new Object[]{"Call1", "1", "Book1", "date1", "date11", new Boolean(false)});
		model.insertRow(viewHoldTable.getRowCount(),new Object[]{"Call2", "2", "Book2", "date2", "date22", new Boolean(false)});
		model.insertRow(viewHoldTable.getRowCount(),new Object[]{"Call3", "3", "Book3", "date3", "date33", new Boolean(false)});
		model.insertRow(viewHoldTable.getRowCount(),new Object[]{"Call4", "4", "Book4", "date4", "date44", new Boolean(false)});
		
		
		// Add table to view items
		JScrollPane scrollPane = new JScrollPane(viewHoldTable);
		
		JButton closeButton = new JButton("Close");
		
		// Add components to panel
		scrollPane.setPreferredSize(new Dimension(480, 200));
		viewHoldForm.add(scrollPane, BorderLayout.PAGE_START);
		viewHoldForm.add(closeButton, BorderLayout.PAGE_END);

		// Window
		final JFrame frame = new JFrame("Items on Hold");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(500, 270);
		//Add content to the window.
		frame.add(viewHoldForm, BorderLayout.CENTER);
		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// Button Listeners
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
	}
	
	private void openFinesForm(){
		// Add check overdue Form
		JPanel finesForm = new JPanel();
		
		final String[] columnNames = {"Call Number", "Copy #", "Title", "Out Date", "Due Date", "Out"};
		Object[][] data = {};

		final DefaultTableModel model = new DefaultTableModel(data,columnNames);

	
		// Add table to view items
		JTable finesTable = new JTable(model);
		
		TableColumn tc = finesTable.getColumnModel().getColumn(5);  
        tc.setCellEditor(finesTable.getDefaultEditor(Boolean.class));  
        tc.setCellRenderer(finesTable.getDefaultRenderer(Boolean.class));

		model.insertRow(finesTable.getRowCount(),new Object[]{"Call1", "1", "Book1", "date1", "date11", new Boolean(false)});
		model.insertRow(finesTable.getRowCount(),new Object[]{"Call2", "2", "Book2", "date2", "date22", new Boolean(false)});
		model.insertRow(finesTable.getRowCount(),new Object[]{"Call3", "3", "Book3", "date3", "date33", new Boolean(false)});
		model.insertRow(finesTable.getRowCount(),new Object[]{"Call4", "4", "Book4", "date4", "date44", new Boolean(false)});
		
		
		// Add table to view items
		JScrollPane scrollPane = new JScrollPane(finesTable);
		
		JButton closeButton = new JButton("Close");
		
		// Add components to panel
		scrollPane.setPreferredSize(new Dimension(480, 200));
		finesForm.add(scrollPane, BorderLayout.PAGE_START);
		finesForm.add(closeButton, BorderLayout.PAGE_END);

		// Window
		final JFrame frame = new JFrame("Outstanding Fines");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(500, 270);
		//Add content to the window.
		frame.add(finesForm, BorderLayout.CENTER);
		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// Button Listeners
		closeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				frame.setVisible(false);
			}
		});
	}
	
	public JComponent getBorrowerPanel(){
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 2, 10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10) );

		JButton searchButton = new JButton("Search");
		JButton viewAccountButton = new JButton("View Account");
		JButton holdRequestButton = new JButton("Hold Request");
		JButton payFineButton = new JButton("Pay Fine");
		
		mainPanel.add(searchButton);
		mainPanel.add(viewAccountButton);
		mainPanel.add(holdRequestButton);
		mainPanel.add(payFineButton);
		
		searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				openSearchForm();
			}
		});
		viewAccountButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				openAccountForm();
			}
		});
		holdRequestButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				openHoldForm();
			}
		});
		payFineButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
			}
		});
		
		return mainPanel;
		
	}
}

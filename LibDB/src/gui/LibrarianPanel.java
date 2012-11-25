package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class LibrarianPanel {

	private JTextField callNumberField;
	private JTextField isbnField;
	private JTextField titleField;
	private JTextField mainAuthorField;
	private JTextField publisherField;
	private JTextField yearField;
	private JPanel mainPanel;

	public LibrarianPanel(){

	}
	private void openAddBookForm(){
		// Add Book Form
		JPanel addBookForm = new JPanel();
		// Set form layout
		addBookForm.setLayout(new GridLayout(0, 2, 10, 10));
		addBookForm.setBorder(new EmptyBorder(10, 10, 10, 10) );

		// Field Labels
		JLabel callNumberLabel = new JLabel("Call #: ");
		JLabel isbnLabel = new JLabel("ISBN: ");
		JLabel titleLabel = new JLabel("Title: ");
		JLabel mainAuthorLabel = new JLabel("Main Author: ");
		JLabel publisherLabel = new JLabel("Publisher: ");
		JLabel yearLabel = new JLabel("Year: ");

		// Fields
		callNumberField = new JTextField(10);
		isbnField = new JTextField(10);
		titleField = new JTextField(10);
		mainAuthorField = new JTextField(10);
		publisherField = new JTextField(10);
		yearField = new JTextField(10);

		// Buttons
		JButton addButton = new JButton("Add");
		JButton cancelButton = new JButton("Cancel");

		// Add components to panel
		addBookForm.add(callNumberLabel);
		addBookForm.add(callNumberField);
		addBookForm.add(isbnLabel);
		addBookForm.add(isbnField);
		addBookForm.add(titleLabel);
		addBookForm.add(titleField);
		addBookForm.add(mainAuthorLabel);
		addBookForm.add(mainAuthorField);
		addBookForm.add(publisherLabel);
		addBookForm.add(publisherField);
		addBookForm.add(yearLabel);
		addBookForm.add(yearField);
		addBookForm.add(addButton);
		addBookForm.add(cancelButton);

		// Window
		final JFrame frame = new JFrame("Add Book");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(300, 300);

		//Add content to the window.
		frame.add(addBookForm, BorderLayout.CENTER);

		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

		// Button Listeners
		addButton.addActionListener(new ActionListener(){
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


	public JComponent getLibrarianPanel(){

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(0, 2, 10, 10));
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10) );

		JButton addBookButton = new JButton("Add Book");
		JButton viewOutButton = new JButton("View Out Items");
		JButton viewPopularButton = new JButton("View Popular Items");

		addBookButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//Execute when button is pressed
				openAddBookForm();
			}
		});  

		viewOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//Execute when button is pressed
			}
		});  

		viewPopularButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				//Execute when button is pressed
			}
		});  

		mainPanel.add(addBookButton);
		mainPanel.add(viewOutButton);
		mainPanel.add(viewPopularButton);

		return mainPanel;
	}
}

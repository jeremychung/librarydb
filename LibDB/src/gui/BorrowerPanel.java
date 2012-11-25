package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class BorrowerPanel {
	
	private JTextField searchField;
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
		JLabel searchLabel = new JLabel("Search: ");
		// Fields
		searchField = new JTextField(10);
		
		// Buttons
		JButton searchButton = new JButton("Search");
		JButton cancelButton = new JButton("Cancel");

		// Add components to panel
		searchForm.add(searchLabel);
		searchForm.add(searchField);
		searchForm.add(searchButton);
		searchForm.add(cancelButton);

		// Window
		final JFrame frame = new JFrame("Search");
		// Window Properties
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(300, 125);
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
			}
		});

		cancelButton.addActionListener(new ActionListener(){
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
			}
		});
		holdRequestButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
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

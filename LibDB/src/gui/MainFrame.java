package gui;

import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.sql.Connection;


public class MainFrame extends JPanel {
	private Connection con;
	
	public MainFrame(Connection con) {
		super(new GridLayout(1, 1));

		JTabbedPane tabbedPane = new JTabbedPane();
		this.con = con;
		
		BorrowerPanel borrower = new BorrowerPanel();
		tabbedPane.addTab("Borrower", borrower.getBorrowerPanel());

		ClerkPanel clerk = new ClerkPanel(con);
		tabbedPane.addTab("Clerk", clerk.getClerkPanel());

		LibrarianPanel librarian = new LibrarianPanel(con);
		tabbedPane.addTab("Librarian", librarian.getLibrarianPanel());

		//Add the tabbed pane to this panel.
		add(tabbedPane);

		//The following line enables to use scrolling tabs.
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	public void loadMainFrame() {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Windows".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		}
		
		//Create and set up the window.
		JFrame frame = new JFrame("Library Database");
		frame.setPreferredSize(new Dimension(400, 150));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Add content to the window.
		frame.add(new MainFrame(con), BorderLayout.CENTER);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

		// center the frame
		Dimension d = frame.getToolkit().getScreenSize();
		Rectangle r = frame.getBounds();
		frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );
	}

}

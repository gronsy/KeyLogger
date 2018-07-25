package proj.keyLogger;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DBDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameTxt;
	private JTextField userTxt;
	private JTextField passwdTxt;
	private JTextField tableNmTxt;
	private boolean dirty=false, conFlag=false;
	private String dataBase, user, passwd, tableName;
	private static final Logger log=LoggerFactory.getLogger(LoggerWindow.class);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		log.debug("DBDlg: Entered main method.");
		try {
			log.debug("DBDlg: Trying to show window.");
			DBDlg dialog = new DBDlg();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			log.error("Failed to show window.", e);
			e.printStackTrace();
		}
		log.debug("DBDlg: Exited main method.");
	}
	
	public String getDataBase() {return dataBase;}
	public String getUser() {return user;}
	public String getPasswd() {return passwd;}
	public String getTableName() {return tableName;}
	public boolean getConFlag() {return conFlag;}
	
	private void cancelBtnAction() {
		log.debug("DBDlg: Entered cancel button method.");
		if(dirty) {
			int confirmed = JOptionPane.showOptionDialog(contentPanel,
					"Are you sure you want to close window without connecting and saving data to database?", "Are you sure?",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, new String[] { "Yes", "No" }, "No");
			if(confirmed==JOptionPane.YES_OPTION) {
				dirty=false;
				dispose();
				log.debug("DBDlg:Exiting dialog window without connecting.");
			}
		}
		log.debug("DBDlg: Exiting cancel button action.");
	}
	
	private void connectBtnAction() {
		log.debug("DBDlg: Entered connect button method.");
		
		dataBase=nameTxt.getText();
		user=userTxt.getText();
		passwd=passwdTxt.getText();
		tableName=tableNmTxt.getText();
		int confirmed = JOptionPane.showOptionDialog(contentPanel,
				"Are you sure you want to connect and save data to the database?", "Are you sure?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, new String[] { "Yes", "No" }, "No");
		
		if(dataBase.equals("")||user.equals("")||passwd.equals("")||tableName.equals("")) {
			JOptionPane.showMessageDialog(contentPanel, "One of the fields is empty!", "Error!", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(confirmed==JOptionPane.YES_OPTION) {
			dirty=false;
			conFlag=true;
			dispose();
			log.debug("DBDlg: Exited and started to connect to database.");
		}
	
		log.debug("DBDlg: Exiting connect button method.");
	}
	/**
	 * Create the dialog.
	 */
	public DBDlg() {
		log.debug("DBDlg: Started to build database dialog.");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel lblDSN = new JLabel("DSN:");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblDSN, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblDSN, 0, SpringLayout.WEST, contentPanel);
		contentPanel.add(lblDSN);
		
		JLabel lblDbUser = new JLabel("DB User:");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblDbUser, 10, SpringLayout.SOUTH, lblDSN);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblDbUser, 0, SpringLayout.WEST, lblDSN);
		contentPanel.add(lblDbUser);
		
		JLabel lblDbPassword = new JLabel("DB Password");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblDbPassword, 10, SpringLayout.SOUTH, lblDbUser);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblDbPassword, 0, SpringLayout.WEST, lblDSN);
		contentPanel.add(lblDbPassword);
		
		JLabel lblDbTableName = new JLabel("DB Table Name:");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblDbTableName, 10, SpringLayout.SOUTH, lblDbPassword);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblDbTableName, 0, SpringLayout.WEST, lblDSN);
		contentPanel.add(lblDbTableName);
		
		nameTxt = new JTextField();
		nameTxt.addFocusListener(new FocusAdapter() {
			String nameOld;
			@Override
			public void focusGained(FocusEvent e) {
				nameOld=nameTxt.getText();
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(!nameOld.equals(nameTxt.getText())) {
					dirty=true;
				}
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.NORTH, nameTxt, -5, SpringLayout.NORTH, lblDSN);
		sl_contentPanel.putConstraint(SpringLayout.WEST, nameTxt, 20, SpringLayout.EAST, lblDbTableName);
		sl_contentPanel.putConstraint(SpringLayout.EAST, nameTxt, -10, SpringLayout.EAST, contentPanel);
		contentPanel.add(nameTxt);
		nameTxt.setColumns(10);
		
		userTxt = new JTextField();
		userTxt.addFocusListener(new FocusAdapter() {
			String userOld;
			@Override
			public void focusGained(FocusEvent e) {
				userOld=userTxt.getText();
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(!userOld.equals(userTxt.getText())) {
					dirty=true;
				}
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.NORTH, userTxt, 5, SpringLayout.SOUTH, nameTxt);
		sl_contentPanel.putConstraint(SpringLayout.WEST, userTxt, 20, SpringLayout.EAST, lblDbTableName);
		sl_contentPanel.putConstraint(SpringLayout.EAST, userTxt, -10, SpringLayout.EAST, contentPanel);
		contentPanel.add(userTxt);
		userTxt.setColumns(10);
		
		passwdTxt = new JTextField();
		passwdTxt.addFocusListener(new FocusAdapter() {
			String passwdOld;
			@Override
			public void focusGained(FocusEvent e) {
				passwdOld=passwdTxt.getText();
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(!passwdOld.equals(passwdTxt.getText())) {
					dirty=true;
				}
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.NORTH, passwdTxt, 5, SpringLayout.SOUTH, userTxt);
		sl_contentPanel.putConstraint(SpringLayout.WEST, passwdTxt, 20, SpringLayout.EAST, lblDbTableName);
		sl_contentPanel.putConstraint(SpringLayout.EAST, passwdTxt, -10, SpringLayout.EAST, contentPanel);
		contentPanel.add(passwdTxt);
		passwdTxt.setColumns(10);
		
		tableNmTxt = new JTextField();
		tableNmTxt.addFocusListener(new FocusAdapter() {
			String tableNmOld;
			@Override
			public void focusGained(FocusEvent e) {
				tableNmOld=tableNmTxt.getText();
			}
			@Override
			public void focusLost(FocusEvent e) {
				if(!tableNmOld.equals(tableNmTxt.getText())) {
					dirty=true;
				}
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.NORTH, tableNmTxt, 5, SpringLayout.SOUTH, passwdTxt);
		sl_contentPanel.putConstraint(SpringLayout.WEST, tableNmTxt, 20, SpringLayout.EAST, lblDbTableName);
		sl_contentPanel.putConstraint(SpringLayout.EAST, tableNmTxt, -10, SpringLayout.EAST, contentPanel);
		contentPanel.add(tableNmTxt);
		tableNmTxt.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton connetButton = new JButton("Save");
				connetButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						log.debug("DBDlg: Clicked connect button.");
						connectBtnAction();
					}
				});
				connetButton.setActionCommand("OK");
				buttonPane.add(connetButton);
				getRootPane().setDefaultButton(connetButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						log.debug("Clicked cancel button.");
						cancelBtnAction();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		log.debug("DBDlg: Finished building database dialog.");
	}
}

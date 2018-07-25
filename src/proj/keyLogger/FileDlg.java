package proj.keyLogger;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import javax.swing.JCheckBox;

public class FileDlg extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField fileName;
	private boolean dirty=false, writeFileFlg=false;
	private String writeFileName;
	private static final Logger log=LoggerFactory.getLogger(LoggerWindow.class);

	/**
	 * Launch the application.
	 */
	
	public String getWriterFileName() {return writeFileName;}
	
	public boolean getWriteFileFlg() {return writeFileFlg;}
	public void setWriteFileFlg(boolean writeFileFlg) {this.writeFileFlg=writeFileFlg;}

	private void cancelBtnAction() {
		log.debug("FileDlg: Entered cancel button action.");
		
		if(dirty) {
			int confirmed = JOptionPane.showOptionDialog(contentPanel,
					"Are you sure you don't want to log into file?", "Are you sure?",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					null, new String[] { "Yes", "No" }, "No");
			if(confirmed==JOptionPane.YES_OPTION) {
				dirty=false;
			}
		}
		
		dispose();
		log.debug("FileDlg: Exited file dialog.");
	}
	
	public void writeInFile() {
		log.debug("FileDlg: Entered write in file method.");
		
		int confirmed = JOptionPane.showOptionDialog(contentPanel,
				"Are you sure you want to save data into this file?", "Are you sure?",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				null, new String[] { "Yes", "No" }, "No");
		
		if(confirmed==JOptionPane.YES_OPTION) {
			if(fileName.getText().equals("")) {
				JOptionPane.showMessageDialog(contentPanel, "The program will choose default name for you file.", "Warning!", JOptionPane.WARNING_MESSAGE);
			}
			writeFileFlg=true;
			dispose();
		}
		
		log.debug("FileDlg: Exited write in file method.");
	}
	
	public static void main(String[] args) {
		log.debug("FileDlg: Entered file dialog main.");
		try {
			FileDlg dialog = new FileDlg();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			log.error("FileDlg: Failed to create window.", e);
			e.printStackTrace();
		}
		log.debug("FileDlg: Exited file dialog main.");
	}

	/**
	 * Create the dialog.
	 */
	public FileDlg() {
		setTitle("File Configuration");
		log.debug("FileDlg: Started to build dialog window.");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		fileName = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, fileName, 7, SpringLayout.NORTH, contentPanel);
		fileName.addFocusListener(new FocusAdapter() {
			String fileOld;
			@Override
			public void focusGained(FocusEvent e) {
				log.debug("FileDlg: Gained focus on file name text field.");
				dirty=false;
				fileOld=fileName.getText();
			}
			@Override
			public void focusLost(FocusEvent e) {
				log.debug("FileDlg: Lost focus on file name text field.");
				if(!fileOld.equals(fileName.getText()))
					dirty=true;
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.EAST, fileName, -10, SpringLayout.EAST, contentPanel);
		contentPanel.add(fileName);
		fileName.setColumns(10);
		{
			JLabel lblFileLabel = new JLabel("File name:");
			sl_contentPanel.putConstraint(SpringLayout.WEST, fileName, 10, SpringLayout.EAST, lblFileLabel);
			sl_contentPanel.putConstraint(SpringLayout.NORTH, lblFileLabel, 10, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, lblFileLabel, 5, SpringLayout.WEST, contentPanel);
			contentPanel.add(lblFileLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						log.debug("FileDlg: Clicked OK button.");
						
						dirty=false;
						writeFileName=fileName.getText();
						writeInFile();
						
						log.debug("FileDlg: Finished proccessing log button event.");
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						log.debug("FileDlg: Clicked close button.");
						cancelBtnAction();
						log.debug("FIleDlg: Managed to close window");
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		log.debug("FileDlg: Finished building dialog window.");
	}
}

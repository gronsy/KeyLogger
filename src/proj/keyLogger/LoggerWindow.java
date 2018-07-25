package proj.keyLogger;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import org.jnativehook.GlobalScreen;
import org.jnativehook.dispatcher.SwingDispatchService;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class LoggerWindow extends JFrame {

	private JPanel contentPane;
	private JTextArea logTxt;
	private JButton logCtrlBtn;
	private JScrollPane scroll;
	private FileDlg fileDlg; 
	private KeyLogger klg;
	private boolean fileWriteFlag=false, DBWriteFlag=false, logFlg=false;
	private static final Logger log=LoggerFactory.getLogger(LoggerWindow.class);
	private FileWriter writer;
	private DBWriter dbWriter;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		log.debug("MainWnd: Entered Main method.");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					log.debug("MaindWnd: Trying to show window.");
					LoggerWindow frame = new LoggerWindow();
					frame.setVisible(true);
					log.debug("MainWnd: Managed to show window.");
				} catch (Exception e) {
					log.error("MainWnd: Failed to show window.", e);
					e.printStackTrace();
				}
			}
		});
		log.debug("MainWnd: Exited Main method.");
	}

	/**
	 * Create the frame.
	 */
	public LoggerWindow() {
		log.debug("MainWnd: Started building window.");
		setTitle("Key Logger");
		klg=new KeyLogger();
		GlobalScreen.setEventDispatcher(new SwingDispatchService());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		logCtrlBtn = new JButton("Start Logging");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, logCtrlBtn, -10, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, logCtrlBtn, -10, SpringLayout.EAST, contentPane);
		logCtrlBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log.debug("MainWnd: Clicked on log control button.");
				if(logCtrlBtn.getText().equals("Start Logging")) {
					logCtrlBtn.setText("Stop Logging");
					logFlg=true;
					klg.startLogging(logTxt);
				}
				else{
					logCtrlBtn.setText("Start Logging");
					klg.stopLogging();
					logFlg=false;
				}
				log.debug("MainWnd: Finished proccessing log control button event.");
			}
		});
		contentPane.add(logCtrlBtn);
		
		logTxt = new JTextArea();
		sl_contentPane.putConstraint(SpringLayout.NORTH, logTxt, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, logTxt, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, logTxt, -10, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, logTxt, -10, SpringLayout.WEST, logCtrlBtn);
		contentPane.add(logTxt);
		
		scrollPane = new JScrollPane(logTxt);
		sl_contentPane.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, scrollPane, 10, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.WEST, logCtrlBtn);
		contentPane.add(scrollPane);
		
		JButton fileSaveBtn = new JButton("Save to File");
		fileSaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log.debug("MainWnd: Showing file dialog.");
				if(logFlg) {
					klg.stopLogging();
				}
				
				FileDlg fileDlg=new FileDlg();
				fileDlg.setModal(true);
				fileDlg.setVisible(true);
				
				if(fileDlg.getWriteFileFlg()) {
					writer=new FileWriter();
					
					writer.writeData(logTxt, fileDlg.getWriterFileName());
				}
				
				if(logFlg) {
					klg.startLogging(logTxt);
				}
				
				fileDlg.setWriteFileFlg(false);
				log.debug("MainWnd: Exited file dialog.");
			}
		});
		sl_contentPane.putConstraint(SpringLayout.WEST, fileSaveBtn, 0, SpringLayout.WEST, logCtrlBtn);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, fileSaveBtn, -6, SpringLayout.NORTH, logCtrlBtn);
		sl_contentPane.putConstraint(SpringLayout.EAST, fileSaveBtn, -10, SpringLayout.EAST, contentPane);
		contentPane.add(fileSaveBtn);
		
		JButton DBConnectBtn = new JButton("Save to DB");
		DBConnectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log.debug("MainWnd: Clicked on save to database button.");
				if(logFlg) {
					klg.stopLogging();
				}
				
				DBDlg dbDlg=new DBDlg();
				dbDlg.setModal(true);
				dbDlg.setVisible(true);
				
				if(dbDlg.getConFlag()) {
					dbWriter=new DBWriter();
					dbWriter.writeToDB(dbDlg.getDataBase(), dbDlg.getUser(), dbDlg.getPasswd(), dbDlg.getTableName(), logTxt);
				}
				
				if(logFlg) {
					klg.startLogging(logTxt);
				}
				log.debug("MainWnd: Exited database dialog.");
			}
		});
		sl_contentPane.putConstraint(SpringLayout.WEST, DBConnectBtn, 0, SpringLayout.WEST, logCtrlBtn);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, DBConnectBtn, -5, SpringLayout.NORTH, fileSaveBtn);
		sl_contentPane.putConstraint(SpringLayout.EAST, DBConnectBtn, 0, SpringLayout.EAST, logCtrlBtn);
		contentPane.add(DBConnectBtn);
		
		JButton clearBtn = new JButton("Clear");
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				log.debug("MainWnd: Clicked on clear button.");
				int confirmed = JOptionPane.showOptionDialog(contentPane,
						"Are you sure that you want to clear log area?", "Are you sure?",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, new String[] { "Yes", "No" }, "No");
				if(confirmed==JOptionPane.YES_OPTION) {
					logTxt.setText("");
					log.debug("MainWnd: Cleared log text area.");
				}
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, clearBtn, 0, SpringLayout.NORTH, scrollPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, clearBtn, 0, SpringLayout.WEST, logCtrlBtn);
		sl_contentPane.putConstraint(SpringLayout.EAST, clearBtn, 0, SpringLayout.EAST, logCtrlBtn);
		contentPane.add(clearBtn);
		log.debug("MainWnd: Finished building window.");
	}
}

package proj.keyLogger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileWriter {
	private File writeFile;
	private static final Logger log=LoggerFactory.getLogger(LoggerWindow.class);
	
	public void writeData(JTextArea logTxt, String fileName) {
		log.debug("FileWriter: Entered file writeing method.");
		
		
		Random rand=new Random(System.currentTimeMillis());
		if(FilenameUtils.getExtension(fileName).equals("")&&!FilenameUtils.getBaseName(fileName).equals("")) {
			writeFile=new File(fileName+new Integer(rand.nextInt()).toString()+".txt");
		}
		else if(fileName.equals("")) {
			String name=new String("klg");
			writeFile=new File(name+new Integer(rand.nextInt()).toString()+".txt");
		}else {
			String[] name=fileName.split("\\.");
			writeFile=new File(FilenameUtils.getBaseName(fileName)+new Integer(rand.nextInt()).toString()+"."+FilenameUtils.getExtension(fileName));
		}
		
		try {
			PrintWriter output=new PrintWriter(writeFile);
			
			for(String line:logTxt.getText().split("\\n")) {
				output.println(line);
			}
			output.close();
		}catch(IOException e) {
			log.error("FileWriter: Failed to write in file.", e);
			JOptionPane.showConfirmDialog(null, "Failed to save data into file!", "Error", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
		}
		
		log.debug("FileWriter: Exited file write method.");
	}
}

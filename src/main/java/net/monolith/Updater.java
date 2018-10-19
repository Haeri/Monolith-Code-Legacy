package net.monolith;

import java.io.IOException;

public class Updater extends Thread{

	private MonolithFrame motherFrame;

	public Updater(MonolithFrame parent){
		motherFrame = parent;
	}

	public void run() {
		// Delay check
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			if (GlobalVariables.debug)
				e.printStackTrace();
		}

		shouldUpdate();
	}

	public void shouldUpdate(){
		int ret = 0;
		try {
			String tmp = execCmd("java -jar Updater.jar " + GlobalVariables.VERSION + " " + GlobalVariables.BUILD + " true");
			tmp = tmp.replace("\n", "").replace("\r", "");
			ret = Integer.parseInt(tmp);
		}catch(Exception e){
			motherFrame.console.println(e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
		}

		if(ret == 1){
			updateMonolith();
		}
	}

	public void updateMonolith() {
		try {
			Runtime.getRuntime().exec("java -jar Updater.jar " + GlobalVariables.VERSION + " " + GlobalVariables.BUILD + " false");
		} catch (IOException e) {
			motherFrame.console.println(e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
		}
	}

	private static String execCmd(String cmd) throws java.io.IOException {
		java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}
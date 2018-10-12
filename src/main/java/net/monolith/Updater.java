package net.monolith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;

public class Updater {
	private static String newVersion = "";
	private static int newBuild = -1;
	
	private final static String WEB_ADDRESS = "http://monolith-code.net.tiberius.sui-inter.net/update/";
	
	public static boolean checkUpdate(MonolithFrame parent){
		if(checkConnectin(parent.console)){
			if(!compareVersions()){
				int res = JOptionPane.showOptionDialog(parent, "There is a new version available. Would you like to update?\nold:  Monolith Text v" + GlobalVariables.VERSION + " : " + GlobalVariables.BUILD + "\nnew: Monolith Text v" + newVersion + " : " + newBuild, "New Vesion Available", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
				if(res == JOptionPane.OK_OPTION){
					return true;
				}
			}
		}
		return false;
	}
	
	// Gets latest version
	private static boolean compareVersions() {
		// fetch new version number
		String version = "";
		String build = "";
     
		try{
			URL url = new URL(WEB_ADDRESS + "version.txt");
		
			// Get the input stream through URL Connection
		    URLConnection con = url.openConnection();
		    InputStream is =con.getInputStream();
		
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
		    version = br.readLine();
		    build = br.readLine();
		
		}catch (IOException e){
			return true;
		}
		
		newVersion = version;
		newBuild = Integer.parseInt(build);
		
		return (GlobalVariables.BUILD >= newBuild);
	}
	
	// Checks internet connection
	public static boolean checkConnectin(Console console) {
		try {
			try {
				URL url = new URL("http://www.google.com");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.connect();
				if (con.getResponseCode() == 200){
					return true;
				}
			} catch (Exception exception) {
				return false;
			}
		} catch (Exception e) {
			console.println(e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
		}
		return false;
	}
}
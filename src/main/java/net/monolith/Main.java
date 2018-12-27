package net.monolith;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.SwingUtilities;

public class Main {
breaking it
	public static void main(String[] args) {
		try{		
			Locale.setDefault(Locale.ENGLISH);
			SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			    	if (args.length > 0 && !(args[0].equals(""))){
			    		MonolithFrame papa = new MonolithFrame(args[0]);
			    		for(int i = 1; i < args.length; i++){
			    			papa = new MonolithFrame(args[i], papa);
						}
					}else{
						new MonolithFrame(null, null);
					}
			    }
			});
		}catch(Throwable e){
	        Map<String,Object> params = new LinkedHashMap<>();
	        params.put("Version", GlobalVariables.VERSION + ":" + GlobalVariables.BUILD);
	        params.put("OS", System.getProperty("os.name"));
	        params.put("HardwareID", GlobalVariables.uniqueID);
	        params.put("Time", System.currentTimeMillis());
	        params.put("Exception", e.getStackTrace());
	        
			DataPoster.POST("http://monolith-code.net.tiberius.sui-inter.net/exception/index.php", params);
		}
	}
}
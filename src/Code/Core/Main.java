package Code.Core;

import java.util.Locale;

import javax.swing.SwingUtilities;

public class Main {

	public static void main(String[] args) {
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
	}
}
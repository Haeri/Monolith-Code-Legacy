package Code.Core;

import java.util.Locale;

public class Main {

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		if (args.length > 0 && !(args[0].equals(""))){
			MonolithFrame papa = new MonolithFrame(args[0], "New File");
			for(int i = 1; i < args.length; i++){
				new MonolithFrame(args[i], "New File", papa);
			}
		}
		else{
			new MonolithFrame("New File");
		}
		
		
//		if (args.length > 0 && !(args[0].equals(""))){
//			SwingUtilities.invokeLater(new Runnable() {
//			    public void run() {
//
//			new MonolithFrame(args[0], "New File", 620, 700).setVisible(true);
//			    }
//			 });
//
//		}
//		else{
//			SwingUtilities.invokeLater(new Runnable() {
//			    public void run() {
//			       new MonolithFrame("New File", 620, 700).setVisible(true);
//			    }
//			 });
//		}
	}
}
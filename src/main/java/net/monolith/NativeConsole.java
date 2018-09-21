package net.monolith;

/***
 * Notes to myself:
 *
 * Windows: Is straight forward.
 * Linux: xterm calls bash so we can execute all kinds of commands. 
 * Mac: Worst platform ever! We do a osascript call to execute a script. But the script spams the terminal 
 * 		So we first call a script that disables the echo (stty -echo), then we execute the actual command. Since 
 * 		Echo is disabled, the script won't spam the terminal. But this also hides any user input. To counter this,
 * 		we enable echo again with the first command call. What a mess. 
 * @author haeri
 *
 */
public class NativeConsole {
	
	public static final String WIN_HEAD[] = { "cmd", "/c", "start", "/wait", "cmd.exe", "/K", ""};
	public static final String MAC_HEAD[] = { "osascript", "-e", 
			"tell application \"Terminal\"\n\treopen\n\tactivate\n\tdo script \"stty -echo\" in window 1\n\tdo script \""};
	public static final String NIX_HEAD[] = { "xterm", "-e", "bash", "-c", ""};
	
	public static final String WIN_TAIL[] = {"&& pause && exit"};
	public static final String MAC_TAIL[] = {"&& echo 'Press Enter to continue' && read && exit\" in window 1\nend tell"};
	public static final String NIX_TAIL[] = {"&& read -n 1 -s -p 'Press any key to continue'"};
	
	public static final String ECHO_SPACE = GlobalVariables.osType == OSType.WIN ? "echo. && ": "echo && ";
	public static final String V = GlobalVariables.osType == OSType.WIN ? "^>": "'>'";
	

	public static String[] createNativeBuildCommand(String name, String path, String buildCommand){
		String body =
				(GlobalVariables.osType == OSType.MAC ? "stty echo && echo  && cd " + path + " && " : "")
				+ "echo -------------------------------------------------------- && "
                + "echo " + V + " Compiling " + name + " in " + path + " && "						// Info
                
                + buildCommand + " && "														// Compile code
                
                + "echo " + V + " Compilation finished successfully && "							// Info
                + "echo -------------------------------------------------------- ";
		
		return constructCommand(body);
	}
	
	
	
	public static String[] createNativeRunCommand(String name, String path, String runCommand){
		String body = 
				(GlobalVariables.osType == OSType.MAC ? "stty echo && echo  && cd " + path + " && " : "")
				+ "echo -------------------------------------------------------- && "
				+ "echo " + V + " " + name + " outputs: && "								// Info
				+ ECHO_SPACE
				
				+ runCommand + " && "														// Run code
				
				+ ECHO_SPACE
				+ "echo " + V + " " + name + " was successfully executed && "						// Info
				+ "echo -------------------------------------------------------- ";
		return constructCommand(body);
	}
	
	public static String[] createNativeBuildNRunCommand(String name, String path, String buildCommand, String runCommand){
		String body = 
				(GlobalVariables.osType == OSType.MAC ? "stty echo && echo  && cd " + path + " && " : "")
                + "echo -------------------------------------------------------- && "
                + "echo " + V + " Compiling " + name + " in " + path + " && "					// Info
                
                + buildCommand + " && "														// Compile code
                
                + "echo " + V + " Compilation finished successfully && "							// Info
                + "echo -------------------------------------------------------- && "
				+ "echo " + V + " " + name + " outputs: && "								// Info
				+ ECHO_SPACE
				
				+ runCommand + " && "														// Run code
				
				+ ECHO_SPACE
				+ "echo " + V + " " + name + " was successfully executed && "						// Info
				+ "echo -------------------------------------------------------- ";
		return constructCommand(body);
	}
	
	private static String[] constructCommand(String body){
		String head[] = getOSHead();
		String tail[] = getOSTail();
		String ret[] = new String[head.length + tail.length - 1];
		
		String command = head[head.length-1] + body + tail[0];
		
		for(int i = 0; i < ret.length; i++){
			if(i < head.length-1){
				ret[i] = head[i];				
			}else if(i == head.length -1){
				ret[i] = command;
			}else{
				ret[i] = tail[i - head.length];
			}
		}
		
		return ret;
	}

	private static String[] getOSHead(){
		if(GlobalVariables.osType == OSType.WIN){
			return WIN_HEAD;
		}else if(GlobalVariables.osType == OSType.MAC){
			return MAC_HEAD;
		}else{
			return NIX_HEAD;
		} 
	}
	
	private static String[] getOSTail(){
		if(GlobalVariables.osType == OSType.WIN){
			return WIN_TAIL;
		}else if(GlobalVariables.osType == OSType.MAC){
			return MAC_TAIL;
		}else{
			return NIX_TAIL;
		} 
	}
}




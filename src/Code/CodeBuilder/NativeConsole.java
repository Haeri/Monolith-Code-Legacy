package Code.CodeBuilder;

import Code.Core.GlobalVariables;
import Code.Core.OSType;

public class NativeConsole {
	
	public static final String NATIV_HEADER[] = {"sh -c ", "cmd /c start cmd.exe /K ", null, null};
	public static final String NATIV_TAIL[] = {"read -n 1 -s -p \"Press any key to continue\"\"", "pause && exit\"", null, null};
	
	public static String createNativeBuildCommand(String name, String path, String buildCommand){
		return getOSHead()
                +"\""//+ "\"cd " + path + " && "													// Set path
                + "echo -------------------------------------------------------- && "
                + "echo ^> Compiling " + name + " in " + path + " && "					// Info
                
                + buildCommand + " && "														// Compile code
                
                + "echo ^> Compilation finished successfully && "							// Info
                + "echo -------------------------------------------------------- && "
                + getOSTail();		
	}
	
	
	public static String createNativeRunCommand(String name, String path, String runCommand){
		return getOSHead()
				+"\""//+ "\"cd " + path + " && "													// Set path
				+ "echo -------------------------------------------------------- && "
				+ "echo ^> " + name + " outputs: && "								// Info
				+ "echo. && "
				
				+ runCommand + " && "														// Run code
				
				+ "echo. && "
				+ "echo ^> " + name + " was successfully executed && "						// Info
				+ "echo -------------------------------------------------------- && "
                + getOSTail();
	}
	
	public static String createNativeBuildNRunCommand(String name, String path, String buildCommand, String runCommand){
		return getOSHead()
				+"\""//+ "\"cd " + path + " && "													// Set path
				
                + "echo -------------------------------------------------------- && "
                + "echo ^> Compiling " + name + " in " + path + " && "					// Info
                
                + buildCommand + " && "														// Compile code
                
                + "echo ^> Compilation finished successfully && "							// Info
                + "echo -------------------------------------------------------- && "
				+ "echo ^> " + name + " outputs: && "								// Info
				+ "echo. && "
				
				+ runCommand + " && "														// Run code
				
				+ "echo. && "
				+ "echo ^> " + name + " was successfully executed && "						// Info
				+ "echo -------------------------------------------------------- && "
                + getOSTail();	
	}
	
	private static String getOSHead(){
		return NATIV_HEADER[GlobalVariables.osType.ordinal()] != null ? NATIV_HEADER[GlobalVariables.osType.ordinal()] : NATIV_HEADER[OSType.ANY.ordinal()]; 
	}
	
	private static String getOSTail(){
		return NATIV_TAIL[GlobalVariables.osType.ordinal()] != null ? NATIV_TAIL[GlobalVariables.osType.ordinal()] : NATIV_TAIL[OSType.ANY.ordinal()];
	}
}





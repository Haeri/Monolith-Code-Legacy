package main.java.net.monolith;

public class GlobalVariables {

	
	private GlobalVariables(){}
	
	// Version
	public static final String MONOLITH_NAME = "Monolith Code";
	public static final String VERSION = "0.0.7";
	public static final int BUILD = 61;
	public static boolean BETA = false;
	
	// Paths
	public static final String RESOURCE_PATH = "/main/resources";
	public static final String AUTOSAVE_PATH = "autosave";
	
	// Math
	public static int mathRound = 5;
	
	// Themes
	public static String[] loadedThemes = null;	

	// Misc
	public static boolean debug = false;
	public static String osName = "";
	public static OSType osType;
}
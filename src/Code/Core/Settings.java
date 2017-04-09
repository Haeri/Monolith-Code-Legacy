package Code.Core;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import Code.Console.Console;

public class Settings {
	
	private static Settings masterInstance;
	
	private Properties config = new Properties();
	private final File configFile = new File("config.ini");
	private Console console;
	
	
	// Change on every exit
	private int lastX;
	private int lastY;
	private int width;
	private int height;
	
	// Change on master exit
	private int fontSize;
	private int tabSize;
	private Font font;
	private String theme;
	private boolean isLineWrap;
	private boolean isConsole;
	private boolean isLineNumbers;
	private boolean isCodeFolding;	
	private int backupMaxSaveDays;
	
	// Defaults
	public static final int DEF_LAST_X = -1;
	public static final int DEF_LAST_Y = -1;
	public static final int DEF_WIDTH = 500;
	public static final int DEF_HEIGHT = 600;
	public static final int DEF_FONT_SIZE = 12;
	public static final int DEF_TAB_SIZE = 4;
	public static final boolean DEF_IS_LINE_WRAP = true;
	public static final boolean DEF_IS_CONSOLE = false;
	public static final boolean DEF_IS_LINE_NUMBERS = true;
	public static final boolean DEF_IS_CODE_FOLDING = false;
	public static final Font DEF_FONT = new Font("Consolas", Font.PLAIN, DEF_FONT_SIZE);
	public static final int DEF_BACKUP_MAX_SAVE_DAYS = 30;	
	
	public static final String DEF_THEME = "Default";

	
	public Settings(Console console){
		this.console = console;	
		
		loadSettings();
	}
	
	
	public void loadSettings(){
		if (configFile.exists()) {
			try {
				config.load(new FileInputStream(configFile));

				lastX = Integer.parseInt(config.getProperty("pos_x", DEF_LAST_X + ""));
				lastY = Integer.parseInt(config.getProperty("pos_y", DEF_LAST_Y + ""));
				width = Integer.parseInt(config.getProperty("width", DEF_WIDTH + ""));
				height = Integer.parseInt(config.getProperty("height", DEF_HEIGHT + ""));
				fontSize = Integer.parseInt(config.getProperty("font_size", DEF_FONT_SIZE + ""));
				tabSize = Integer.parseInt(config.getProperty("tab_size", DEF_TAB_SIZE + ""));
				isLineWrap = Boolean.parseBoolean(config.getProperty("is_line_wrap", DEF_IS_LINE_WRAP + ""));
				isConsole = Boolean.parseBoolean(config.getProperty("is_console", DEF_IS_CONSOLE + ""));
				isLineNumbers = Boolean.parseBoolean(config.getProperty("is_line_numbers", DEF_IS_LINE_NUMBERS + ""));
				isCodeFolding = Boolean.parseBoolean(config.getProperty("is_code_folding", DEF_IS_CODE_FOLDING + ""));
				font = new Font(config.getProperty("font", DEF_FONT + ""), Font.PLAIN, DEF_FONT_SIZE);
				theme = config.getProperty("theme", DEF_THEME + "");
				backupMaxSaveDays = Integer.parseInt(config.getProperty("backup_max_save_days", DEF_BACKUP_MAX_SAVE_DAYS + ""));
						
			} catch (IOException e) {
				console.println("Could not read From cofig!\n" + e.getMessage(), Console.err);
				if(GlobalVariables.debug) e.printStackTrace();
			}
		} else {
			
			setLastX(DEF_LAST_X);
			setLastY(DEF_LAST_Y);
			setWidth(DEF_WIDTH);
			setHeight(DEF_HEIGHT);
			setFontSize(DEF_FONT_SIZE);
			setTabSize(DEF_TAB_SIZE);
			setLineWrap(DEF_IS_LINE_WRAP);
			setConsole(DEF_IS_CONSOLE);
			setLineNumbers(DEF_IS_LINE_NUMBERS);
			setCodeFolding(DEF_IS_CODE_FOLDING);
			setFont(DEF_FONT);
			setTheme(DEF_THEME);
			setBackupMaxSaveDays(DEF_BACKUP_MAX_SAVE_DAYS);
			
			saveConfig();
		}
	}
	
	public void saveWindow(int x, int y, int width, int height){
		setLastX(x);
		setLastY(y);
		setWidth(width);
		setHeight(height);
	}
	
	public void saveConfigOnExit() {		
		if(masterInstance != null && masterInstance != this){
			masterInstance.saveWindow(lastX, lastY, width, height);
			masterInstance.saveConfig();
		}else{
			saveConfig();
		}
	}

	public void saveConfig() {
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(configFile);
		} catch (FileNotFoundException e) {
			console.println("Couldn't find config File!\n" + e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
			return;
		}

		try {
			config.store(output, null);
		} catch (IOException e) {
			console.println("Couldn't write config File!\n" + e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
			return;
		}
		
		makeMeMaster();
	}
	
	private void makeMeMaster(){
		masterInstance = this;
	}
	
	
	
	public int getLastX() {
		return lastX;
	}


	public void setLastX(int lastX) {
		config.setProperty("pos_x", "" + lastX);
		this.lastX = lastX;
	}


	public int getLastY() {
		return lastY;
	}


	public void setLastY(int lastY) {
		config.setProperty("pos_y", "" + lastY);
		this.lastY = lastY;
	}


	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		config.setProperty("width", "" + width);
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		config.setProperty("height", "" + height);
		this.height = height;
	}


	public int getFontSize() {
		return fontSize;
	}


	public void setFontSize(int fontSize) {
		config.setProperty("font_size", "" + fontSize);
		this.fontSize = fontSize;
		makeMeMaster();
	}


	public int getTabSize() {
		return tabSize;
	}


	public void setTabSize(int tabSize) {
		config.setProperty("tab_size", "" + tabSize);
		this.tabSize = tabSize;
		makeMeMaster();
	}


	public Font getFont() {
		return font;
	}


	public void setFont(Font font) {
		config.setProperty("font", "" + font.getName());
		this.font = font;
		makeMeMaster();
	}


	public String getTheme() {
		return theme;
	}


	public void setTheme(String theme) {
		config.setProperty("theme", "" + theme);
		this.theme = theme;
		makeMeMaster();
	}


	public boolean isLineWrap() {
		return isLineWrap;
	}


	public void setLineWrap(boolean isLineWrap) {
		config.setProperty("is_line_wrap", "" + isLineWrap);
		this.isLineWrap = isLineWrap;
		makeMeMaster();
	}


	public boolean isConsole() {
		return isConsole;
	}


	public void setConsole(boolean isConsole) {
		config.setProperty("is_console", "" + isConsole);
		this.isConsole = isConsole;
		makeMeMaster();
	}

	public int getBackupMaxSaveDays() {
		return backupMaxSaveDays;
	}
	
	public void setBackupMaxSaveDays(int days) {
		config.setProperty("backup_max_save_days", "" + days);
		this.backupMaxSaveDays = days;
		makeMeMaster();
	}

	public boolean isLineNumbers() {
		return isLineNumbers;
	}

	public void setLineNumbers(boolean isLineNumbers) {
		config.setProperty("is_line_numbers", "" + isLineNumbers);
		this.isLineNumbers = isLineNumbers;
		masterInstance = this;
	}
	
	public boolean isCodeFolding() {
		return isCodeFolding;
	}

	public void setCodeFolding(boolean isCodeFolding) {
		config.setProperty("is_code_folding", "" + isCodeFolding);
		this.isCodeFolding = isCodeFolding;
		masterInstance = this;
	}	
}

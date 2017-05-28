package main.java.net.monolith;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import dk.heick.properties.types.utils.CommentedProperties;

class Setting{
	public Object object;
	public boolean isSessionSetting;
	public Setting(Object object, boolean isSessionSetting){
		this.object = object;
		this.isSessionSetting = isSessionSetting;
	}
}

public class Settings {
	
	private static Settings masterInstance;
	
	private Map<String, Setting> settingsMap = new HashMap<String, Setting> ();
	private CommentedProperties config = new CommentedProperties();
	private final File configFile = new File("config.ini");
	private Console console;
	
	public static final String CLEAR_COMMAND = "CLEAR";
	
	// Settings Strings
	public static final String LAST_X = "LAST_X";
	public static final String LAST_Y = "LAST_Y";
	public static final String WIDTH = "WIDTH";
	public static final String HEIGHT = "HEIGHT";
	public static final String FONT_SIZE = "FONT_SIZE";
	public static final String TAB_SIZE = "TAB_SIZE";
	public static final String IS_LINE_WRAP = "IS_LINE_WRAP";
	public static final String IS_CONSOLE = "IS_CONSOLE";
	public static final String IS_LINE_NUMBERS = "IS_LINE_NUMBERS";
	public static final String IS_CODE_FOLDING = "IS_CODE_FOLDING";
	public static final String FONT_NAME = "FONT_NAME";
	public static final String BACKUP_MAX_SAVE_DAYS = "BACKUP_MAX_SAVE_DAYS";	
	public static final String THEME = "THEME";
	
	// Defaults
	public static final int DEF_LAST_X = 100;
	public static final int DEF_LAST_Y = 100;
	public static final int DEF_WIDTH = 600;
	public static final int DEF_HEIGHT = 700;
	public static final int DEF_FONT_SIZE = 14;
	public static final int DEF_TAB_SIZE = 4;
	public static final int DEF_BACKUP_MAX_SAVE_DAYS = 30;	
	public static final boolean DEF_IS_LINE_WRAP = true;
	public static final boolean DEF_IS_CONSOLE = false;
	public static final boolean DEF_IS_LINE_NUMBERS = true;
	public static final boolean DEF_IS_CODE_FOLDING = false;
	public static final String DEF_FONT_NAME = "Consolas";
	public static final String DEF_THEME = "Default";

	
	public Settings(Console console){
		this.console = console;	
		
		loadSettings();
	}
	
	
	public void loadSettings(){
		if (configFile.exists()) {
			BufferedReader brTest;
			try {
				brTest = new BufferedReader(new FileReader(configFile));
				if(brTest.readLine().equals(CLEAR_COMMAND)){
					configFile.delete();
					createSettings();
				}else{
					loadSettingsFromFile();
				}
			} catch (Exception e) {
				console.println("Could not delete cofig!\n" + e.getMessage(), Console.err);
				if(GlobalVariables.debug) e.printStackTrace();
			}
		} else {
			createSettings();
		}
	}
	
	public void loadSettingsFromFile(){
		try {
			config.load(configFile);

			putSetting(LAST_X, new Setting(Integer.parseInt(config.getPropertyValue(LAST_X)), true));
			putSetting(LAST_Y, new Setting(Integer.parseInt(config.getPropertyValue(LAST_Y)), true));
			putSetting(WIDTH, new Setting(Integer.parseInt(config.getPropertyValue(WIDTH)), true));
			putSetting(HEIGHT, new Setting(Integer.parseInt(config.getPropertyValue(HEIGHT)), true));
			putSetting(FONT_SIZE, new Setting(Integer.parseInt(config.getPropertyValue(FONT_SIZE)), false));
			putSetting(TAB_SIZE, new Setting(Integer.parseInt(config.getPropertyValue(TAB_SIZE)), false));
			putSetting(IS_LINE_WRAP, new Setting(Boolean.parseBoolean(config.getPropertyValue(IS_LINE_WRAP)), false));
			putSetting(IS_CONSOLE, new Setting(Boolean.parseBoolean(config.getPropertyValue(IS_CONSOLE)), false));
			putSetting(IS_LINE_NUMBERS, new Setting(Boolean.parseBoolean(config.getPropertyValue(IS_LINE_NUMBERS)), false));
			putSetting(IS_CODE_FOLDING, new Setting(Boolean.parseBoolean(config.getPropertyValue(IS_CODE_FOLDING)), false));
			putSetting(FONT_NAME, new Setting(config.getPropertyValue(FONT_NAME), false));
			putSetting(THEME, new Setting(config.getPropertyValue(THEME), false));
			putSetting(BACKUP_MAX_SAVE_DAYS, new Setting(Integer.parseInt(config.getPropertyValue(BACKUP_MAX_SAVE_DAYS)), false));
			
			for(int i = 0; i < LanguageFactory.languages.size(); i++){
				Language lang = LanguageFactory.languages.get(i);
				putSetting(LanguageFactory.getCompileName(lang), new Setting(config.getPropertyValue(LanguageFactory.getCompileName(lang)), false));
				putSetting(LanguageFactory.getRunName(lang), new Setting(config.getPropertyValue(LanguageFactory.getRunName(lang)), false));
			}
		} catch (IOException e) {
			console.println("Could not read From cofig!\n" + e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
		}
	}
	
	public void createSettings(){
		config.setPropertiesComment("This is the Config file");
		putSetting(LAST_X, new Setting(DEF_LAST_X, true));
		putSetting(LAST_Y, new Setting(DEF_LAST_Y, true));
		putSetting(WIDTH, new Setting(DEF_WIDTH, true));
		putSetting(HEIGHT, new Setting(DEF_HEIGHT, true));
		putSetting(FONT_SIZE, new Setting(DEF_FONT_SIZE, false));
		putSetting(TAB_SIZE, new Setting(DEF_TAB_SIZE, false));
		putSetting(IS_LINE_WRAP, new Setting(DEF_IS_LINE_WRAP, false));
		putSetting(IS_CONSOLE, new Setting(DEF_IS_CONSOLE, false));
		putSetting(IS_LINE_NUMBERS, new Setting(DEF_IS_LINE_NUMBERS, false));
		putSetting(IS_CODE_FOLDING, new Setting(DEF_IS_CODE_FOLDING, false));
		putSetting(FONT_NAME, new Setting(DEF_FONT_NAME, false));
		putSetting(THEME, new Setting(DEF_THEME, false));
		putSetting(BACKUP_MAX_SAVE_DAYS, new Setting(DEF_BACKUP_MAX_SAVE_DAYS, false));		
		
		for(int i = 0; i < LanguageFactory.languages.size(); i++){
			Language lang = LanguageFactory.languages.get(i);
			putSetting(LanguageFactory.getCompileName(lang), new Setting(LanguageFactory.getCompileCommand(lang), false));
			putSetting(LanguageFactory.getRunName(lang), new Setting(LanguageFactory.getRunCommand(lang), false));
		}
					
		saveConfig();
	}
	
	public void saveWindow(int x, int y, int width, int height){
		setSetting(LAST_X, x);
		setSetting(LAST_Y, y);
		setSetting(WIDTH, width);
		setSetting(HEIGHT, height);
	}
	
	public void saveConfigOnExit() {		
		if(masterInstance != null && masterInstance != this){
			masterInstance.saveWindow(getSetting(LAST_X), getSetting(LAST_Y), getSetting(WIDTH), getSetting(HEIGHT));
			masterInstance.saveConfig();
		}else{
			saveConfig();
		}
	}

	public void saveConfig() {
		try {
			PrintWriter writer = new PrintWriter(configFile, "UTF-8");
			config.store(writer);
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
	
	@SuppressWarnings("unchecked")
	public <T> T getSetting(String name) {
		return (T) settingsMap.get(name).object;
	}
	
	public void putSetting(String name, Setting setting){
		config.setProperty(name, setting.object + "");
		settingsMap.put(name, setting);
	}
	
	public void setSetting(String name, Object object){
		config.setProperty(name, object + "");
		settingsMap.get(name).object = object;
		if(!settingsMap.get(name).isSessionSetting){
			makeMeMaster();
		}
	}
	
}
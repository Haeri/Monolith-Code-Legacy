package Code.Languages;

import javafx.stage.FileChooser.ExtensionFilter;

public class Language {
	
	public String name;
	public String extension;
	public String compileCommand;
	public String runCommand;
	public String quickCode;
	public String defaultSugetedName;
	private NameSuggeseter ns;
	public int index;
	
	public boolean isCompilable = false;
	public boolean isRunnable = false;
	public boolean forceClassName = false;
	
	public ExtensionFilter extensionFilter;
	public String syntaxConstant;
	
	public String toString(){
		return name;
	}
		
	public void setNameSuggester(NameSuggeseter ns){
		this.ns = ns;
	}
	
	public String suggestName(String text){
		if(ns != null)
			return ns.generateFileName(text);
		else
			return defaultSugetedName; 
	}

}

abstract class NameSuggeseter{
	public abstract String generateFileName(String text);
}

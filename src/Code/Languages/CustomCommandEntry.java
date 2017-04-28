package Code.Languages;

public class CustomCommandEntry {
	public Language language;
	public boolean isCustomBuildCommand;
	public boolean isCustomRunCommand;
	public String customBuildCommand;
	public String customRunCommand;
	
	public CustomCommandEntry(Language language){
		this.language = language;
		isCustomBuildCommand = false;
		isCustomRunCommand = false;
		customBuildCommand = "";
		customRunCommand = "";
	}
}

package plugins;

import javax.swing.JFrame;

public interface Plugin {
    public PluginInfo getInfo();
    public ResponseData process(ContextData data);

    class PluginInfo {
        public String name;
        public String author;
        public String description;
        public int version;
    }

    class ContextData {
    	public JFrame frame;
    	
        // Current document settings
        public String language;
        public String fileName;
        public String extension;

        // Text
        public String text;
        public int selectionStart;
        public int selectionEnd;
    }

    class ResponseData {
        public boolean replaceDocument;
        public String response;
    }

}
package plugins;

public interface Plugin {
    public PluginInfo getInfo();
    public String process(String text);
}
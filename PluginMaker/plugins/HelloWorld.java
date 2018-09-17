package plugins;

public class HelloWorld implements Plugin{
    private PluginInfo info;

    public HelloWorld(){
        info = new PluginInfo();
        info.name = "Hello World";
        info.author = "Haeri";
        info.description = "Says Hello World";
        info.buildNumber = 1;
    }

    @Override
    public PluginInfo getInfo() {
        return info;
    }

    @Override
    public String process(String text) {
        return text + " [Hello World]";
    }
}

package plugins;

public class BinaryConverter implements Plugin{
    private PluginInfo info;

    public BinaryConverter(){
        info = new PluginInfo();
        info.name = "Binary Converter";
        info.author = "Haeri";
        info.description = "Convert hexadecimal numbers to binary numbers";
        info.buildNumber = 1;
    }

    @Override
    public PluginInfo getInfo() {
        return info;
    }

    @Override
    public String process(String text) {
        return text + " - This is my Binary converter talking!!";
    }
}

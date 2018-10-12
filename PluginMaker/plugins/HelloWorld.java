package plugins;

import javax.swing.*;

public class HelloWorld implements Plugin{
    private PluginInfo info;

    public HelloWorld(){
        info = new PluginInfo();
        info.name = "Hello World";
        info.author = "Haeri";
        info.description = "Says Hello World";
        info.version = 1;
    }

    @Override
    public PluginInfo getInfo() {
        return info;
    }

    @Override
    public ResponseData process(ContextData data) {
        ResponseData res = new ResponseData();

        res.response = data.text;
		JOptionPane.showMessageDialog(data.frame, "Hello World");
        
        return res;
    }
}
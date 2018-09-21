package net.monolith;

import plugins.Plugin;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class PluginManager {

    public List<Plugin> pluginList = new ArrayList<>();

    private File dir;
    private MonolithFrame motherFrame;

    public PluginManager(MonolithFrame motherFrame){
        this.motherFrame = motherFrame;

        dir = new File(GlobalVariables.PLUGINS_PATH);
        dir.mkdir();
    }

    public void loadPlugins(){
        File[] listOfFiles = dir.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".class")) {
                String name = "plugins." + listOfFiles[i].getName().replace(".class", "");
                File file = new File(".");
                try {
                    URL url = file.toURI().toURL();
                    URL[] urls = new URL[]{url};
                    ClassLoader cl = new URLClassLoader(urls);
                    Class cls = cl.loadClass(name);
                    pluginList.add((Plugin)cls.newInstance());
                    Plugin.PluginInfo info = pluginList.get(i).getInfo();
                    motherFrame.console.println("\"" + info.name + "\"" + " loaded successfully", Console.stdOut);
                } catch (Exception e) {
                    System.out.println("Broken");
                    motherFrame.console.println(e.getMessage(), Console.err);
                    if(GlobalVariables.debug) e.printStackTrace();
                }
            }
        }
    }

    public void initializePlugins(JMenu menu){
        // PLUGINS
        JMenuItem tmp;
        for (int i = 0; i < pluginList.size(); i++){
            final int index = i;
            tmp = new JMenuItem(pluginList.get(i).getInfo().name);
            menu.add(tmp);
            //pluginItems.add(tmp);

            tmp.addActionListener(event -> {
                Plugin.ContextData context = new Plugin.ContextData();
                context.fileName = motherFrame.getName();
                context.language = motherFrame.getLanguage().name;
                context.extension = motherFrame.getLanguage().extension;
                context.selectionStart = motherFrame.tField.getSelectionStart();
                context.selectionEnd = motherFrame.tField.getSelectionEnd();
                context.text = motherFrame.tField.getSelectedText();

                Plugin.ResponseData res = pluginList.get(index).process(context);

                if(res.replaceDocument){
                    motherFrame.setText(res.response);
                }else{
                    motherFrame.replaceSegemnt(context.selectionStart, context. selectionEnd, res.response);
                }
            });
        }
    }
}
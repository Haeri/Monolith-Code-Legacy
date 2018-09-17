package main.java.net.monolith;

import plugins.Plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class PluginManager {

    public List<Plugin> pluginList = new ArrayList<>();

    private File dir;

    public PluginManager(){
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
                    Class  cls = cl.loadClass(name);
                    pluginList.add((Plugin)cls.newInstance());
                    System.out.println(name + " loaded successfully");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
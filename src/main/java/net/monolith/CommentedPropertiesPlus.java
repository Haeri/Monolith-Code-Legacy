package main.java.net.monolith;

import dk.heick.properties.types.utils.CommentedProperties;

public class CommentedPropertiesPlus extends CommentedProperties{
	public int getPropertyValue(String key, int defaultValue){
		int ret;
		try{			
			String s = super.getPropertyValue(key);
			if(s != null) 
				ret = Integer.parseInt(s);
			else
				ret = defaultValue;
		}catch(Exception e){
			ret = defaultValue;
		}
		return ret;
	}
	
	public String getPropertyValue(String key, String defaultValue){
		String ret = super.getPropertyValue(key);
	
		if(ret == null)
			ret = defaultValue;
	
		return ret;
	}
	
	public boolean getPropertyValue(String key, boolean defaultValue){
		boolean ret;
		try{			
			String s = super.getPropertyValue(key);
			if(s != null) 
				ret = Boolean.parseBoolean(s);
			else
				ret = defaultValue;
		}catch(Exception e){
			ret = defaultValue;
		}
		return ret;
	}
}

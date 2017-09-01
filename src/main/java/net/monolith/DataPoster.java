package main.java.net.monolith;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class DataPoster {

	public static boolean POST(String postUrl, Map<String,Object> params){
	       URL url = null;
		try {
			url = new URL(postUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	        byte[] postDataBytes = null;
	        try {
		        StringBuilder postData = new StringBuilder();
		        for (Map.Entry<String,Object> param : params.entrySet()) {
		            if (postData.length() != 0) postData.append('&');
						postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
		            postData.append('=');
		            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		        }
		        postDataBytes = postData.toString().getBytes("UTF-8");
	        } catch (UnsupportedEncodingException e) {
	        	// TODO Auto-generated catch block
	        	e.printStackTrace();
	        	return false;
	        }

	        try{
		        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		        conn.setRequestMethod("POST");
		        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		        conn.setDoOutput(true);
		        conn.getOutputStream().write(postDataBytes);
	
		        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	
		        StringBuilder sb = new StringBuilder();
		        for (int c; (c = in.read()) >= 0;)
		            sb.append((char)c);
		        String response = sb.toString();
		        if(!response.equals("SUCCESS"))
		        	return false;
		        
	        }catch(Exception e){
	        	return false;
	        }
	        
	        System.out.println("POST was successful");
	        return true;
	}
}
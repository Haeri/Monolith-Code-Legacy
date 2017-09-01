package main.java.net.monolith;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public class Util {

	 public static int getEncodedLength(String text, Charset charset) {
	        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);        
	        CharBuffer charBuffer = CharBuffer.wrap(text);
	        CharsetEncoder encoder = charset.newEncoder();

	        int length = 0;
	        while (encoder.encode(charBuffer, byteBuffer, false) == CoderResult.OVERFLOW) {
	            length += byteBuffer.position();
	            byteBuffer.clear();
	        }

	        encoder.encode(charBuffer, byteBuffer, true);
	        length += byteBuffer.position();
	        return length;
	    }
	 
	 public static <T> void print(T generic){
		 if(GlobalVariables.debug)
			 System.out.print(generic);
	 }
	 
	 public static <T> void println(T generic){
		 if(GlobalVariables.debug)
			 System.out.println(generic);
	 }
	 
	 
	 public static byte[] getMACAddress() throws SocketException, UnknownHostException {
		    InetAddress address = InetAddress.getLocalHost();
		    NetworkInterface networkInterface = NetworkInterface.getByInetAddress(address);

		    return networkInterface.getHardwareAddress();
		}
	 
	 public static String getUniqueId(){
		 try{
			 byte[] macAddress = getMACAddress();
			 String ret = "";
			 for (int byteIndex = 0; byteIndex < macAddress.length; byteIndex++) {
				 ret += String.format("%02X%s", macAddress[byteIndex], (byteIndex < macAddress.length - 1) ? "-" : "");
			 }
			 return ret;
		 }catch(Exception e){
			return "";
		}
	 }
}
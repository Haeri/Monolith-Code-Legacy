package Code.Core;

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
}

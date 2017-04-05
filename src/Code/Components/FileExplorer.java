package Code.Components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.swing.JOptionPane;

import Code.Core.GlobalVariables;

public class FileExplorer {

	private PrintWriter writer;
	private BufferedReader bufRead;
	private String text, path, name;
	private File file;
	private int lineCount = 1;
	StringBuilder fullText = new StringBuilder();

	public FileExplorer(String path) {
		this.path = path;
	}
	
	public FileExplorer(String path, String name, String text) {
		this.text = text;
		this.path = path;
		this.name = name;
	}
	
	/**
	 * Writes text to file in UTF_8 
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void writeFile() throws FileNotFoundException, UnsupportedEncodingException{
		writeFile(StandardCharsets.UTF_8.name());
	}
	
	/**
	 * Writes text to file
	 * @param charset Charset to be used when writing
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void writeFile(String charset) throws FileNotFoundException, UnsupportedEncodingException{
		file = new File(path + name);
		writer = new PrintWriter(file, charset);
		
		writer.print(lineSegment(text));
		writer.close();
	}
	
	// Reads from file
	public void readFile() {
		try {
	        //BufferedREader
			bufRead = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
			String firstLine = bufRead.readLine();
			fullText.append(firstLine != null ? firstLine : "");
			String tempRead = "";
			while ((tempRead = bufRead.readLine()) != null) {				
				fullText.append("\n" + tempRead);
	            lineCount++;
			}
			bufRead.close();
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Couln't read " + path, "Unknown Format", JOptionPane.OK_OPTION);
			if(GlobalVariables.debug) e.printStackTrace();
		}	
	}
	
	// Returns String from file
	public String getString() {
		readFile();
		return fullText.toString();
	}
	
	// Returns amount of lines
	public int getLineCount() {
		return lineCount;
	}
	
	// Corrects all line breaks 
	private String lineSegment(String content) {
		return content.replaceAll("(\n)", "\r\n");
	}
}

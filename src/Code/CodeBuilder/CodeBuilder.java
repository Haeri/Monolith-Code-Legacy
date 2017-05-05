package Code.CodeBuilder;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import Code.Components.InputStreamLineBuffer;
import Code.Console.Console;
import Code.Core.GlobalVariables;
import Code.Core.Settings;
import Code.Languages.CustomCommandEntry;
import Code.Languages.CustomCommandSerializer;
import Code.Languages.Language;
import Code.Languages.LanguageFactory;

public class CodeBuilder extends Thread {
	private String path, name, fullname;
	private Language language;
	private BuildConsole con;
	private Process pro;
	private String line;
	private String buildCommand, runCommand;

	private InputStream inStream, inErrStream;
	private OutputStream outStream;
	private InputStreamLineBuffer outBuff, errBuff;
	private Settings settings;

	private CustomCommandEntry cce;
	private int mode = -1;
	
	public static final int BUILD = 0;
	public static final int RUN = 1;
	public static final int BUILD_N_RUN = 2;
	public static final int FREE_RUN = 3;
	
	
	public CodeBuilder(String path, String fullname, Language language, BuildConsole con, Settings settings) {
		this.path = path.replaceAll("\\\\", "/");
		this.fullname = fullname;
		this.language = language;
		this.con = con;
		this.settings = settings;
		
		try{
 			cce = CustomCommandSerializer.getCCE(language.name);
 		}catch(NullPointerException e){
 			cce = new CustomCommandEntry(language);
 		}
		
		int length = 16 + path.length() + fullname.length();
		line = new String(new char[length]).replace("\0", "-");
		
		con.calculateWidth(line);
	}

	public void begin(int mode) {

		name = LanguageFactory.getName(fullname);
		
		if(language.isCompilable && language.isRunnable){
			buildCommand = parseCommand(settings.<String>getSetting(LanguageFactory.getCompileName(language)));
			runCommand = parseCommand(settings.<String>getSetting(LanguageFactory.getRunName(language)));
		}else if(language.isCompilable){
			buildCommand = parseCommand(settings.<String>getSetting(LanguageFactory.getCompileName(language)));
		}else if(language.isRunnable){
			runCommand = parseCommand(settings.<String>getSetting(LanguageFactory.getRunName(language)));
		}
		
		// Override if custom build is enabled
 		if(cce.isCustomBuildCommand && cce.isCustomRunCommand){
 			buildCommand = parseCommand(cce.customBuildCommand);
 			runCommand = parseCommand(cce.customRunCommand);
 		}else if (cce.isCustomBuildCommand && language.isRunnable){
 			buildCommand = parseCommand(cce.customBuildCommand);
 		}else if (cce.isCustomRunCommand && language.isCompilable){
 			runCommand = parseCommand(cce.customRunCommand);
 		}else if (cce.isCustomBuildCommand && !language.isRunnable){
 			buildCommand = parseCommand(cce.customBuildCommand);
 		}else if (cce.isCustomRunCommand && !language.isCompilable){
 			runCommand = parseCommand(cce.customRunCommand);
 		}
		
		// Print if mode mismatch
		if(mode == BUILD_N_RUN && buildCommand == null){
			con.println("No BUILD command available. Skipping BUILD", Console.warn);
			mode = RUN;
		}else if(mode == BUILD_N_RUN && runCommand == null){
			con.println("No RUN command available. Skipping RUN\n", Console.warn);		
			mode = BUILD;
		}
		if(mode == BUILD && buildCommand == null){
			con.println("No BUILD command available. Skipping BUILD", Console.warn);
			con.stop();
			return;
		}
		if(mode == RUN && runCommand == null){
			con.println("No RUN command available. Skipping RUN\n", Console.warn);
			con.stop();
			return;
		}
		
		this.mode = mode;
		this.start();
	}

	public void run() {
		boolean errBuild = false;

		if(pro != null)
			kill();
		
		con.println("\n" + line);

		// Build
		if (mode == 0 || mode == 2) {
			con.println(buildCommand, Console.userIn);
			con.println("> Compiling " + fullname + " in " + path);

			try {
				errBuild = exec(buildCommand);
			} catch (Exception e) {
				con.println(e.getMessage(), Console.err);
				if(GlobalVariables.debug) e.printStackTrace();
				errBuild = true;
			}

			if (!errBuild)
				con.println("> Compilation finished successfully");
			else {
				con.println("> Compilation failed", Console.errMute);
			}
		}

		if (mode == 2 && !errBuild) {
			con.println(line);
		}

		// Run
		if ((mode == 1 || mode == 2) && !errBuild) {
			con.println(runCommand, Console.userIn);
			con.println("> " + fullname + " outputs:\n");
			boolean errRun = false;

			try {
				errRun = exec(runCommand);
			} catch (Exception e) {
				con.println(e.getMessage(), Console.err);
				if(GlobalVariables.debug) e.printStackTrace();
				errRun = true;
			}

			con.println("");
			if (!errRun)
				con.println("> " + fullname + " was successfully executed.");
			else
				con.println("> Error while executing " + fullname, Console.errMute);
		}

		con.println(line + "\n");
		con.stop();
	}
	

	//TODO Still broken for gcc and g++
	private boolean exec(String command) throws Exception {			
		pro = Runtime.getRuntime().exec(command, null, new File(path));

		outStream = pro.getOutputStream();
		inStream = pro.getInputStream();
		inErrStream = pro.getErrorStream();

		outBuff = new InputStreamLineBuffer(inStream);
		errBuff = new InputStreamLineBuffer(inErrStream);

		// Process output stream reader
		outBuff.start();
		errBuff.start();

		// while an input reader buffer thread is alive
		// or there are unconsumed data left
		while (outBuff.isAlive() || outBuff.hasNext() || errBuff.isAlive() || errBuff.hasNext()) {

			// get the normal output if at least 50 millis have passed
			if (outBuff.timeElapsed() > 50)
				while (outBuff.hasNext())
					con.println(outBuff.getNext(), Console.out);
			// get the error output if at least 50 millis have passed
			if (errBuff.timeElapsed() > 50)
				while (errBuff.hasNext())
					con.println(errBuff.getNext(), Console.err);
			// sleep a bit bofore next run
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				con.println(e.getMessage(), Console.err);
				if(GlobalVariables.debug) e.printStackTrace();
			}
		}

		pro.waitFor();
		outStream.close();
		outStream = null;

		if (pro.exitValue() == 0)
			return false;
		else
			return true;
	}

	public void kill() {
		if (pro != null) {
			pro.destroy();
			errBuff.destroy();
			outBuff.destroy();
			pro = null;
		}
	}

	public OutputStream getOutStream() {
		return outStream;
	}

	private String parseCommand(String command) {
		String temp = command.replaceAll("<name>", name);
		return temp.replaceAll("<path>", path);
	}
}
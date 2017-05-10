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
	public enum BuildMode{
		BUILD, RUN, BUILD_N_RUN, FREE_BUILD;
	}
	private String path, name, fullname;
	private Language language;
	private BuildConsole con;
	private Process pro;
	private String line;
	private String buildCommand, runCommand, freeCommand;

	private InputStream inStream, inErrStream;
	private OutputStream outStream;
	private InputStreamLineBuffer outBuff, errBuff;
	private Settings settings;

	private CustomCommandEntry cce;
	private BuildMode mode;
	
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

	public void begin(BuildMode mode) {

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
		if(mode == BuildMode.BUILD_N_RUN && buildCommand == null){
			con.println("No BUILD command available. Skipping BUILD", Console.warn);
			mode = BuildMode.RUN;
		}else if(mode == BuildMode.BUILD_N_RUN && runCommand == null){
			con.println("No RUN command available. Skipping RUN\n", Console.warn);		
			mode = BuildMode.BUILD;
		}
		if(mode == BuildMode.BUILD && buildCommand == null){
			con.println("No BUILD command available. Skipping BUILD", Console.warn);
			con.stop();
			return;
		}
		if(mode == BuildMode.RUN && runCommand == null){
			con.println("No RUN command available. Skipping RUN\n", Console.warn);
			con.stop();
			return;
		}
		
		// Override if native console
		if(cce.useNativeConsole){
			if(mode == BuildMode.BUILD_N_RUN){
				freeCommand = NativeConsole.createNativeBuildNRunCommand(fullname, path, buildCommand, runCommand);
			}else if (mode == BuildMode.BUILD){
				freeCommand = NativeConsole.createNativeBuildCommand(fullname, path, buildCommand);
			}else if (mode == BuildMode.RUN){
				freeCommand = NativeConsole.createNativeBuildCommand(fullname, path, runCommand); 	 			
			}
			mode = BuildMode.FREE_BUILD;
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
		if (mode == BuildMode.BUILD || mode == BuildMode.BUILD_N_RUN) {
			con.println(buildCommand, Console.userIn);
			con.println("> Compiling " + fullname + " in " + path);

			try {
				errBuild = exec(buildCommand, path);
			} catch (Exception e) {
				con.println(e.getMessage(), Console.err);
				if(GlobalVariables.debug) e.printStackTrace();
				errBuild = true;
			}

			if (!errBuild){
				con.println("> Compilation finished successfully");
				con.stat.confirm();
			}else {
				con.println("> Compilation failed", Console.errMute);
				con.stat.error();
			}
		}

		if (mode == BuildMode.BUILD_N_RUN && !errBuild) {
			con.println(line);
		}

		// Run
		if ((mode == BuildMode.RUN || mode == BuildMode.BUILD_N_RUN) && !errBuild) {
			con.println(runCommand, Console.userIn);
			con.println("> " + name + " outputs:\n");
			boolean errRun = false;

			try {
				errRun = exec(runCommand, path);
			} catch (Exception e) {
				con.println(e.getMessage(), Console.err);
				if(GlobalVariables.debug) e.printStackTrace();
				errRun = true;
			}

			con.println("");
			if (!errRun){
				con.println("> " + name + " was successfully executed");
				con.stat.confirm();
			}
			else{
				con.println("> Error while executing " + fullname, Console.errMute);
				con.stat.error();
			}
		}
		
		// Free build
		if(mode == BuildMode.FREE_BUILD){
			boolean errFree = false;
			
			if(cce.useNativeConsole)
				con.println("> Running command in native terminal");		
			else
				con.println(freeCommand, Console.userIn);
			
			con.println(freeCommand);
				
			try {
				errFree = exec(freeCommand, path);
			} catch (Exception e) {
				con.println(e.getMessage(), Console.err);
				if(GlobalVariables.debug) e.printStackTrace();
				errFree = true;
			}
			
			if (!errFree){
				con.println("> successfully executed");
				con.stat.confirm();
			}
			else{
				con.println("> Error while executing", Console.errMute);
				con.stat.error();
			}
		}

		con.println(line + "\n");
		con.stop();
	}
	

	private boolean exec(String command, String path) throws Exception {			
		pro = Runtime.getRuntime().exec(command, null, new File(path+"/"));

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
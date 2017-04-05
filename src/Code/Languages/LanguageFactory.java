package Code.Languages;

import java.util.ArrayList;
import java.util.List;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import javafx.stage.FileChooser.ExtensionFilter;

public class LanguageFactory {
	
	public static Language LANG_TEXT;
	public static Language LANG_C;
	public static Language LANG_CPP;
	public static Language LANG_CS;
	public static Language LANG_H;
	public static Language LANG_JAVA;
	public static Language LANG_PYTHON;
	public static Language LANG_JAVASCRIPT;
	public static Language LANG_TYPESCRIPT;
	public static Language LANG_PHP;
	public static Language LANG_HTML;
	public static Language LANG_CSS;
	public static Language LANG_XML;
	
	public static List<Language> languages;
	
	public static void generateLanguages(){
		if(languages != null) return;
		
		languages = new ArrayList<Language>();
		
		// Text
		LANG_TEXT = new Language();
		LANG_TEXT.name = "Text";
		LANG_TEXT.extension = ".txt";
		LANG_TEXT.isCompilable = false;
		LANG_TEXT.isRunnable = false;
		LANG_TEXT.extensionFilter = new ExtensionFilter("Text File (*.txt)", "*" + LANG_TEXT.extension);
		LANG_TEXT.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_NONE;
		LANG_TEXT.defaultSugetedName = "MyTextFile" + LANG_TEXT.extension;
		LANG_TEXT.setNameSuggester(new NameSuggeseter(){
			@Override
			public String generateFileName(String text) {
				String[] title = text.split("\r\n|\r|\n", 2);
				int min = Math.min(title[0].length(), 20);
				return title[0].substring(0, min) + LANG_TEXT.extension;
			}
		});
		languages.add(LANG_TEXT);
				
		// C
		LANG_C = new Language();
		LANG_C.name = "C";
		LANG_C.extension = ".c";
		LANG_C.compileCommand = "gcc <name>.c -o <name>";
		LANG_C.runCommand = "<path><name>";
		LANG_C.isCompilable = true;
		LANG_C.isRunnable = true;
		LANG_C.quickCode = "#include <stdio.h>\n\nint main(){\n\tprintf(\"Hello World!\\n\");\n\treturn 0;\n}";
		LANG_C.extensionFilter = new ExtensionFilter("C File (*.c)", "*" + LANG_C.extension);
		LANG_C.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_C;
		LANG_C.defaultSugetedName = "MyCCode" + LANG_C.extension;
		languages.add(LANG_C);
		
		// C++
		LANG_CPP = new Language();
		LANG_CPP.name = "C++";
		LANG_CPP.extension = ".cpp";
		LANG_CPP.compileCommand = "g++ -std=c++11 <name>.cpp -o <name>";
		LANG_CPP.runCommand = "<path><name>";
		LANG_CPP.isCompilable = true;
		LANG_CPP.isRunnable = true;
		LANG_CPP.quickCode = "#include <iostream>\n\nusing namespace std;\n\nint main(){\n	cout << \"Hello World\" << endl;\n	return 0;\n}";
		LANG_CPP.extensionFilter = new ExtensionFilter("C++ File (*.cpp)", "*" + LANG_CPP.extension);
		LANG_CPP.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS;
		LANG_CPP.defaultSugetedName = "MyCppCode" + LANG_CPP.extension;
		languages.add(LANG_CPP);
		
		// C#
		LANG_CS = new Language();
		LANG_CS.name = "C#";
		LANG_CS.extension = ".cs";
		LANG_CS.compileCommand = "C:/Windows/Microsoft.NET/Framework/v4.0.30319/csc.exe /out:<name>.exe <name>.cs";
		LANG_CS.runCommand = "<path><name>.exe";
		LANG_CS.isCompilable = true;
		LANG_CS.isRunnable = true;
		LANG_CS.quickCode = "public class CLASS_NAME{\n\tpublic static void Main(){\n\t\tSystem.Console.WriteLine(\"Hello World!\");\n\t}\n}";
		LANG_CS.extensionFilter = new ExtensionFilter("C# File (*.cs)", "*" + LANG_CS.extension);
		LANG_CS.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_CSHARP;
		LANG_CS.defaultSugetedName = "MyC#Code" + LANG_CS.extension;
		languages.add(LANG_CS);
		
		// Header
		LANG_H = new Language();
		LANG_H.name = "Header";
		LANG_H.extension = ".h";
		LANG_H.compileCommand = "g++ -std=c++11 <name>.cpp -o <name>";
		LANG_H.runCommand = "<path><name>";
		LANG_H.isCompilable = true;
		LANG_H.isRunnable = true;
		LANG_H.quickCode = "#include <iostream>\n\nusing namespace std;\n\nint main(){\n	cout << \"Hello World\" << endl;\n	return 0;\n}";
		LANG_H.extensionFilter = new ExtensionFilter("Header File (*.h)", "*" + LANG_H.extension);
		LANG_H.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS;
		LANG_H.defaultSugetedName = "MyHeaderFile" + LANG_H.extension;
		languages.add(LANG_H);
		
		// Java
		LANG_JAVA = new Language();
		LANG_JAVA.name = "Java";
		LANG_JAVA.extension = ".java";
		LANG_JAVA.compileCommand = "javac <name>.java";
		LANG_JAVA.runCommand = "java <name>";
		LANG_JAVA.isCompilable = true;
		LANG_JAVA.isRunnable = true;
		LANG_JAVA.quickCode ="public class CLASS_NAME {\n\tpublic static void main(String[] args){ \n\t\tSystem.out.println(\"Hello World!\");\n\t}\n}";
		LANG_JAVA.extensionFilter = new ExtensionFilter("Java File (*.java)", "*" + LANG_JAVA.extension);
		LANG_JAVA.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_JAVA;
		LANG_JAVA.defaultSugetedName = "MyJavaCode" + LANG_JAVA.extension;
		LANG_JAVA.forceClassName = true;
		LANG_JAVA.setNameSuggester(new NameSuggeseter() {	
			@Override
			public String generateFileName(String text) {
				int iClass = text.indexOf(" class ");
				String tempS = text.substring(iClass + 7);
				int iSpace = tempS.indexOf(" ");
				int iNLine = tempS.indexOf("\n");
				int iBlup = tempS.indexOf("{");
				int iEnd = Math.min(iSpace, iBlup);
				iEnd = Math.min(iEnd, iNLine);

				return text.substring(iClass + 7, iClass + 7 + iEnd) + ".java";
			}
		});
		languages.add(LANG_JAVA);
		
		// Python
		LANG_PYTHON = new Language();
		LANG_PYTHON.name = "Python";
		LANG_PYTHON.extension = ".py";
		LANG_PYTHON.runCommand = "python -u <name>.py";
		LANG_PYTHON.isCompilable = false;
		LANG_PYTHON.isRunnable = true;
		LANG_PYTHON.quickCode ="print \"Hello world\"";
		LANG_PYTHON.extensionFilter = new ExtensionFilter("Python (*.py)", "*" + LANG_PYTHON.extension);
		LANG_PYTHON.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_PYTHON;
		LANG_PYTHON.defaultSugetedName = "MyPythonCode" + LANG_PYTHON.extension;
		languages.add(LANG_PYTHON);
		
		// javaScript
		LANG_JAVASCRIPT = new Language();
		LANG_JAVASCRIPT.name = "JavaScript";
		LANG_JAVASCRIPT.extension = ".js";
		LANG_JAVASCRIPT.isCompilable = true;
		LANG_JAVASCRIPT.isRunnable = true;
		LANG_JAVASCRIPT.quickCode ="console.log(\"Hello world\");";
		LANG_JAVASCRIPT.extensionFilter = new ExtensionFilter("JavaScript (*.js)", "*" + LANG_JAVASCRIPT.extension);
		LANG_JAVASCRIPT.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT;
		LANG_JAVASCRIPT.defaultSugetedName = "MyJavaScriptCode" + LANG_JAVASCRIPT.extension;
		languages.add(LANG_JAVASCRIPT);
		
		// typeScript
		LANG_TYPESCRIPT = new Language();
		LANG_TYPESCRIPT.name = "TypeScript";
		LANG_TYPESCRIPT.extension = ".ts";
		LANG_TYPESCRIPT.isCompilable = true;
		LANG_TYPESCRIPT.isRunnable = true;
		LANG_TYPESCRIPT.compileCommand = "ntsc <name>.ts";
		LANG_TYPESCRIPT.runCommand = "node <name>.js";
		LANG_TYPESCRIPT.quickCode ="class Greeter {\n\tconstructor(public greeting: string) { }\n\tgreet() {\n\t\treturn \"<h1>\" + this.greeting + \"</h1>\";\n\t}\n};\n\nvar greeter = new Greeter(\"Hello World!\");\nvar str = greeter.greet();";
		LANG_TYPESCRIPT.extensionFilter = new ExtensionFilter("TypeScript (*.ts)", "*" + LANG_TYPESCRIPT.extension);
		LANG_TYPESCRIPT.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT;
		LANG_TYPESCRIPT.defaultSugetedName = "MyTypeScriptCode" + LANG_TYPESCRIPT.extension;
		languages.add(LANG_TYPESCRIPT);

		// HTML
		LANG_HTML = new Language();
		LANG_HTML.name = "HTML";
		LANG_HTML.extension = ".html";
		LANG_HTML.isCompilable = false;
		LANG_HTML.isRunnable = false;
		LANG_HTML.quickCode = "<!DOCTYPE html>\n<html>\n\t<head>\n\t\t<meta charset=\"UTF-8\">\n\t\t<title>Hello World!</title>\n\t</head>\n\t<body>\n\t\t<h1>Hello World!</h1>\n\t</body>\n</html>";
		LANG_HTML.extensionFilter = new ExtensionFilter("HTML File (*.html)", "*" + LANG_HTML.extension);
		LANG_HTML.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_HTML;
		LANG_HTML.defaultSugetedName = "index" + LANG_HTML.extension;
		languages.add(LANG_HTML);

		// CSS
		LANG_CSS = new Language();
		LANG_CSS.name = "CSS";
		LANG_CSS.extension = ".css";
		LANG_CSS.isCompilable = false;
		LANG_CSS.isRunnable = false;
		LANG_CSS.extensionFilter = new ExtensionFilter("CSS File (*.css)", "*" + LANG_CSS.extension);
		LANG_CSS.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_CSS;
		LANG_CSS.defaultSugetedName = "style" + LANG_CSS.extension;
		languages.add(LANG_CSS);
		
		// XML
		LANG_XML = new Language();
		LANG_XML.name = "XML";
		LANG_XML.extension = ".xml";
		LANG_XML.isCompilable = false;
		LANG_XML.isRunnable = false;
		LANG_XML.extensionFilter = new ExtensionFilter("XML File (*.xml)", "*" + LANG_XML.extension);
		LANG_XML.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_XML;
		LANG_XML.defaultSugetedName = "MyXMLFile" + LANG_XML.extension;
		languages.add(LANG_XML);
		
		// PHP
		LANG_PHP = new Language();
		LANG_PHP.name = "PHP";
		LANG_PHP.extension = ".php";
		LANG_PHP.isCompilable = false;
		LANG_PHP.isRunnable = false;
		LANG_PHP.extensionFilter = new ExtensionFilter("PHP File (*.php)", "*" + LANG_PHP.extension);
		LANG_PHP.syntaxConstant = SyntaxConstants.SYNTAX_STYLE_XML;
		LANG_PHP.defaultSugetedName = "MyPHPCode" + LANG_PHP.extension;
		languages.add(LANG_PHP);
		
		
		for(int i = 0; i < languages.size(); i++){
			languages.get(i).index = i;
		}

	}
	
	public static Language getLanguageFromFile(String name) {
		String temp = getExtension(name); 
		
		for(int i = 0; i < languages.size(); i++){
			if(languages.get(i).extension.equals(temp))
				return languages.get(i);
		}
		
		return null;
	}
	
	public static Language getLanguageByString(String _name){
		for(int i = 0; i < languages.size(); i++){
			if(languages.get(i).name.equals(_name))
				return languages.get(i);
		}
		
		return LANG_TEXT;
	}
	
	public static String getName(String name){
		String out = "";
		int i = name.lastIndexOf('.');
		if (i > 0) {
		    out = name.substring(0, i);
		}
		return out;
	}
	
	public static String getExtension(String name){
		String out = "";
		int i = name.lastIndexOf('.');
		if (i > 0) {
		    out = name.substring(i);
		}
		return out;
	}
}

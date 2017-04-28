package test.java;

import Code.Core.MonolithFrame;
import Code.Languages.Language;
import Code.Languages.LanguageFactory;

public class FileNameTest {
	
	public static void main(String args[]){

		LanguageFactory.generateLanguages();
		
		for (Language l : LanguageFactory.languages) {
			MonolithFrame mf = new MonolithFrame("Test", 300, 300);
			mf.setLang(l);
			mf.setText("Hello");
			mf.fullName = mf.suggetFileName();
			mf.path = "C:\\Users\\haeri\\Desktop\\s\\";
			mf.saveFile(false);
		} 
	}

}

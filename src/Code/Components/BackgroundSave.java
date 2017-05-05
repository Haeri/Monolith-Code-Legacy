package Code.Components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.Timer;

import Code.Console.Console;
import Code.Core.GlobalVariables;
import Code.Core.MonolithFrame;
import Code.Core.Settings;
import Code.Core.Util;

public class BackgroundSave extends Thread {
	private static boolean isClean;

	private int SAVE_INTERVAL = 600000;
	private int MIN_KEEP_NUMBER = 5;
	private final String FILE_EXTENSION = ".mc";

	private List<SaveFile> saveFiles;
	private MonolithFrame motherFrame;
	private File dir;
	private String text = "";

	private class SaveFile {
		public File file;
		public int pastDays;

		public SaveFile(File file, int pastDays) {
			this.file = file;
			this.pastDays = pastDays;
		}
	}

	public BackgroundSave(MonolithFrame motherFrame) {
		this.motherFrame = motherFrame;
		dir = new File(GlobalVariables.AUTOSAVE_PATH);
		dir.mkdir();
	}

	private void cleanDir() {
		if (isClean)
			return;

		saveFiles = new ArrayList<SaveFile>();
		File[] listOfFiles = dir.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String name = listOfFiles[i].getName();

				int p1 = name.lastIndexOf("_");
				int p2 = name.lastIndexOf("_", p1 - 1);
				String oldDate = name.substring(p2 + 1, name.length() - 10).replaceAll("\\.", "");

				int pastDays = -1;

				try {
					pastDays = calculateDaysToNow(oldDate);
				} catch (ParseException e) {
					if (GlobalVariables.debug)
						e.printStackTrace();
					continue;
				}

				saveFiles.add(new SaveFile(listOfFiles[i], pastDays));
			}
		}

		deleteFiles();
	}

	private void deleteFiles() {
		int delCnt = 0;

		// Sort list
		Collections.sort(saveFiles, new Comparator<SaveFile>() {
			@Override
			public int compare(SaveFile f1, SaveFile f2) {
				return f1.pastDays > f2.pastDays ? -1 : (f1.pastDays < f2.pastDays) ? 1 : 0;
			}
		});

		// delete
		for (int i = 0; i < saveFiles.size() - MIN_KEEP_NUMBER; i++) {
			if (saveFiles.get(i).pastDays > motherFrame.settings.<Integer>getSetting(Settings.BACKUP_MAX_SAVE_DAYS)) {
				saveFiles.get(i).file.delete();
				delCnt++;
			}
		}

		Util.println("Deleted " + delCnt + " old files from backup!");
		isClean = true;
	}

	private int calculateDaysToNow(String oldDate) throws ParseException {
		Calendar cal1 = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		Date date = sdf.parse(oldDate);
		cal1.setTime(date);
		Date date2 = new Date();
		return daysBetween(cal1.getTime(), date2);
	}

	private int daysBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}

	public void run() {
		// Delay the cleanup, so the application startup is not hindered
		try {
			Thread.sleep(9000);
		} catch (InterruptedException e) {
			if (GlobalVariables.debug)
				e.printStackTrace();
		}

		cleanDir();

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				save();
			}
		};
		new Timer(SAVE_INTERVAL, taskPerformer).start();
	}
	
	/**
	 * Save the currently open file in the background. Will only save if current file is not already stored.
	 */
	public void save() {
		String store = motherFrame.getText();
		if (!text.equals(store)) {
			String name = motherFrame.getFullName();
			dir.mkdir();
			String timeStamp = new SimpleDateFormat("dd.MM.yyy_HH.mm").format(Calendar.getInstance().getTime());
			FileExplorer bgSave = new FileExplorer(GlobalVariables.AUTOSAVE_PATH + "/", name + "_" + timeStamp + FILE_EXTENSION, store);
			try {
				bgSave.writeFile();
			} catch (FileNotFoundException e) {
				motherFrame.console.println(e.getMessage(), Console.err);
				if (GlobalVariables.debug)
					e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				motherFrame.console.println(e.getMessage(), Console.err);
				if (GlobalVariables.debug)
					e.printStackTrace();
			}
			text = store;
			Util.print(dir.getName() + " - Background save completed");
		}
	}
}
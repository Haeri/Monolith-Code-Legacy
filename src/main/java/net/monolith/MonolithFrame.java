/**
 *  Monolith Code
 *  by Haeri Studios
 *  (c) 2015
 */

package net.monolith;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
//import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class MonolithFrame extends JFramePlus {
	private static final long serialVersionUID = 1L;

	// GUI Components
	public RSyntaxTextArea 	tField;
	public StatusBar 		stat;
	public Console 			console;

	private JLabel 			leftLabel, rightLabel;
	private JViewport 		tFieldVievport;
	private JPanelPlus 		bottomLabelPanel;
	private JPanel 			bottomPanel;
	private RTextScrollPane tScrollPane;
	private JButton 		pin;
	private JMenuBar 		tMenuBar;
	private JMenu 			mbFile, mbEdit, mbView, mbTools, mbDBug, mbBin, mbLanguage, mbBuild, mbHelp;
	private JMenuItem 		mNew, mNewIndep, mOpen, mSave, mSaveAs, mExit,
							mUndo, mRedo, mCut, mCopy, mPaste, 
							mSymbol,
							mSettings, mFind, mCode, mMath, mBin, mBinInv, mTable,
							mBuild, mRun, mBuildRun, mBuildRunNew, mBuildConf,
							mUpdate, mHelp,
							mToInt, mCrypt, mDeCrypt;
	
	private JCheckBoxMenuItem 				mCon, mPgrp, mLineB;
	private JRadioButtonMenuItem[] 			languageButtons;
	private JSplitPaneWithZeroSizeDivider 	splitter;
	private Gutter							gutter;
	private JViewport 						jvp;

	private List<JMenuItem> pluginItems = new ArrayList<JMenuItem>();

	// Misc Components
	public Settings 		settings;

	private MonolithFrame   parent;
	private BuildConsole    buildConsole;
	private Document        document;
	private BackgroundSave  backgroundSave;
	private Updater			 updater;
	private PluginManager   pluginManager;
	
	// Primitives
	public String 			fullName = "";
	public String 			path = "/";
	public boolean 			buildNew;

	private Language 		language;
	private int 			chars = 0;
	private int 			dividerLocation = 0;
	private double 			ans = 0;
	private boolean 		hasDir, hasChange, isReading, ignoreNameDiscrepency, isPin;
	private static boolean 	isFirst = true;

	// Defaults
	private	final ExtensionFilter EXTENSION_FILTER_ALL = new ExtensionFilter("All Files", "*.*");
	private static final String[] DEF_THEMES = { "Default", "Text" };
	private static final String DIR_THEMES = GlobalVariables.RESOURCE_PATH + "/themes/";

	// Icons
	public static ImageIcon 	iUndo, iRedo, iCut, iCopy, iPaste, iFind, iCog, iConsole, 
								iRun, iMath, iStop, iBuild, iBuildRun, iBuildConfig;
	private static ImageIcon 	iSave, iSaveas, iCode, iBinary, iPinUp, iPinDown, iOpen, 
								iNew, iNewFork, iExit, iTable, iEarth, gifLoading, iUpdate, 
								iInfo, iBuildRunNew;

	
	public MonolithFrame(MonolithFrame spawner) {
		this(null, spawner);
	}

	public MonolithFrame(String path) {
		this(path, null);
	}

	public MonolithFrame(String path, MonolithFrame spawner) {
		super(GlobalVariables.MONOLITH_NAME + " - New File");
		parent = spawner;
		
		
		// ---------- INITIALIZSATION ---------- //
				
		// Operating System
		GlobalVariables.osName = System.getProperty("os.name");		
		
		String osLow = GlobalVariables.osName.toLowerCase();
		if(osLow.indexOf("win") >= 0){
			GlobalVariables.osType = OSType.WIN;
		}else if(osLow.indexOf("mac") >= 0){
			GlobalVariables.osType = OSType.MAC;
		}else if(osLow.indexOf("nix") >= 0 || osLow.indexOf("nux") >= 0 || osLow.indexOf("aix") > 0 ){
			GlobalVariables.osType = OSType.NIX;
		}else{
			GlobalVariables.osType = OSType.ANY;
		}

		// Changes to System Look and Feel
		try {
			if (GlobalVariables.osType == OSType.NIX) {
				// Special Linux style
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
				UIManager.getLookAndFeelDefaults().put("ScrollPaneUI", "javax.swing.plaf.basic.BasicScrollPaneUI");
			} else {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			if(GlobalVariables.debug) e.printStackTrace();
		}
		
		
		
		// Console
		console = new Console(GlobalVariables.MONOLITH_NAME + " v" + GlobalVariables.VERSION + ":" +GlobalVariables.BUILD + " on " + GlobalVariables.osName, this);
		
		// Languages
		LanguageFactory.generateLanguages();

		// JavaFX FileChooser Platform
		new JFXPanel();
		Platform.setImplicitExit(false);
		
		// MenuBar
		tMenuBar = new JMenuBar();
		this.setJMenuBar(tMenuBar);
		
		
		
		// ---------- LOADING ----------//

		// Load Settings
		settings = new Settings(console);

		// Load Font
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			InputStream is = getClass().getResourceAsStream(GlobalVariables.RESOURCE_PATH + "/Consolas.ttf");
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, is));
		} catch (Exception e) {
			console.queueError("Faild to load default font\n" + e.getMessage());
			if(GlobalVariables.debug) e.printStackTrace();
		}

 		// Load custom Commands
 		try {
 			CustomCommandSerializer.init();
 		} catch (Exception e) {
 			console.queueError("Faild to load custom commands\n"+ e.getMessage());
 			if(GlobalVariables.debug) e.printStackTrace();
 		}
		
		// Load Themes
		loadThemes();

		// Load Icons
		loadIcons(tMenuBar.getBackground());

		// Load Plugins
		pluginManager = new PluginManager(this);
		pluginManager.loadPlugins();

		
		
		
		
		// ---------- UI ---------- //
		
		// Window Buildup
		this.setMinimumSize(new Dimension(300, 100));
		getContentPane().setLayout(new BorderLayout());

		// Pin Button
		pin = new JButton();
		if (parent != null)
			isPin = parent.getModal();
		if (isPin) {
			pin.setIcon(iPinDown);
			setAlwaysOnTop(true);
		} else {
			pin.setIcon(iPinUp);
			setAlwaysOnTop(false);
		}
		pin.setPreferredSize(new Dimension(18, 18));
		pin.setToolTipText("Pins the Window to foreground");
		pin.setOpaque(false);
		pin.setContentAreaFilled(false);
		pin.setBorderPainted(false);
		pin.setFocusPainted(false);

		// Menu
		mbFile 	= new JMenu(" File ");
		mbEdit 	= new JMenu(" Edit ");
		mbView 	= new JMenu(" View ");
		mbTools = new JMenu(" Tools ");
		mbBuild = new JMenu(" Build ");
		mbHelp 	= new JMenu(" Help ");
		mbDBug 	= new JMenu(" DBug ");
		mbDBug.setVisible(false);
		tMenuBar.add(mbFile);
		tMenuBar.add(mbEdit);
		tMenuBar.add(mbView);
		tMenuBar.add(mbTools);
		tMenuBar.add(mbBuild);
		tMenuBar.add(mbHelp);
		tMenuBar.add(mbDBug);
		tMenuBar.add(Box.createGlue());
		tMenuBar.add(pin);

		// FILE
		mNew 		= new JMenuItem("New", 'N');
		mNewIndep 	= new JMenuItem("New (Fork)");
		mOpen 		= new JMenuItem("Open");
		mSave 		= new JMenuItem("Save", 'S');
		mSaveAs 	= new JMenuItem("Save As");
		mExit 		= new JMenuItem("Exit");
		mNew.setIcon(iNew);
		mNewIndep.setIcon(iNewFork);
		mOpen.setIcon(iOpen);
		mSave.setIcon(iSave);
		mSaveAs.setIcon(iSaveas);
		mExit.setIcon(iExit);
		mbFile.add(mNew);
		mbFile.add(mNewIndep);
		mbFile.add(mOpen);
		mbFile.addSeparator();
		mbFile.add(mSave);
		mbFile.add(mSaveAs);
		mbFile.addSeparator();
		mbFile.add(mExit);

		// EDIT
		mUndo 		= new JMenuItem("Undo");
		mRedo 		= new JMenuItem("Redo");
		mCut 		= new JMenuItem("Cut");
		mCopy 		= new JMenuItem("Copy");
		mPaste 		= new JMenuItem("Paste");
		mbLanguage 	= new JMenu("Language");
		mUndo.setIcon(iUndo);
		mRedo.setIcon(iRedo);
		mCut.setIcon(iCut);
		mCopy.setIcon(iCopy);
		mPaste.setIcon(iPaste);
		mbLanguage.setIcon(iEarth);
		mbEdit.add(mUndo);
		mbEdit.add(mRedo);
		mbEdit.addSeparator();
		mbEdit.add(mCut);
		mbEdit.add(mCopy);
		mbEdit.add(mPaste);
		mbEdit.addSeparator();
		mbEdit.add(mbLanguage);

		// Sub Menu Language
		ButtonGroup langGroup = new ButtonGroup();
		languageButtons = new JRadioButtonMenuItem[LanguageFactory.languages.size()];
		for(int i = 0; i < LanguageFactory.languages.size(); i++){
			languageButtons[i] = new JRadioButtonMenuItem(LanguageFactory.languages.get(i).name);
			langGroup.add(languageButtons[i]);
			mbLanguage.add(languageButtons[i]);
		}

		// VIEW
		mCon 	= new JCheckBoxMenuItem("Console     ", settings.<Boolean>getSetting(Settings.IS_CONSOLE));
		mLineB 	= new JCheckBoxMenuItem("Line Numbers", settings.<Boolean>getSetting(Settings.IS_LINE_NUMBERS));
		mSymbol = new JMenuItem("Symbols");
		mbView.add(mCon);
		mbView.add(mLineB);
		mbView.add(mSymbol);

		// TOOLS
		mSettings 	= new JMenuItem("Settings");
		mFind 		= new JMenuItem("Find");
		mCode 		= new JMenuItem("Quick Code");
		mMath 		= new JMenuItem("Quick Math");
		mbBin 		= new JMenu("Quick Binary");
		mTable 		= new JMenuItem("Quick Table");
		mSettings.setIcon(iCog);
		mFind.setIcon(iFind);
		mCode.setIcon(iCode);
		mMath.setIcon(iMath);
		mbBin.setIcon(iBinary);
		mTable.setIcon(iTable);
		mbTools.add(mSettings);
		mbTools.add(mFind);
		mbTools.addSeparator();
		mbTools.add(mCode);
		mbTools.add(mMath);
		mbTools.add(mbBin);
		mbTools.add(mTable);
		mbTools.addSeparator();

		// PLUGINS
		pluginManager.initializePlugins(mbTools);



		// BUILD
		mBuild 			= new JMenuItem("Build");
		mRun 			= new JMenuItem("Run");
		mBuildRun 		= new JMenuItem("Build & Run");
		mBuildRunNew 	= new JMenuItem("Build & Run as New");
		mBuildConf 		= new JMenuItem("Custom Build/Run");
		mBuild			.setIcon(iBuild);
		mRun			.setIcon(iRun);
		mBuildRun		.setIcon(iBuildRun);
		mBuildRunNew	.setIcon(iBuildRunNew);
		mBuildConf		.setIcon(iBuildConfig);
		mbBuild			.add(mBuild);
		mbBuild			.add(mRun);
		mbBuild			.add(mBuildRun);
		mbBuild			.add(mBuildRunNew);
		mbBuild			.addSeparator();
		mbBuild			.add(mBuildConf);
		
		// HELP
		mUpdate 	= new JMenuItem("Check for Updates");
		mHelp 		= new JMenuItem("About Monolith");
		mUpdate.setIcon(iUpdate);
		mHelp.setIcon(iInfo);
		mbHelp.add(mUpdate);
		mbHelp.add(mHelp);

		// DBug
		mToInt 		= new JMenuItem("To Integer");
		mPgrp 		= new JCheckBoxMenuItem("Paragraph");
		mCrypt 		= new JMenuItem("LolCrypt");
		mDeCrypt 	= new JMenuItem("Decrypt");
		mbDBug.add(mToInt);
		mbDBug.add(mPgrp);
		mbDBug.add(mCrypt);
		mbDBug.add(mDeCrypt);

		// Sub Menu BINARY
		mBin 		= new JMenuItem("Decimal to Binary");
		mBinInv 	= new JMenuItem("Binary to Decimal");
		mbBin.add(mBin);
		mbBin.add(mBinInv);

		// Label
		leftLabel = new JLabel();
		rightLabel = new JLabel("Width: " + settings.getSetting(Settings.WIDTH) + " Height: " + settings.getSetting(Settings.HEIGHT));
		leftLabel.setForeground(AppTheme.UI_LABEL_FG);
		rightLabel.setForeground(AppTheme.UI_LABEL_FG);

		// Status Bar
		stat = new StatusBar();
		stat.setPreferredSize(new Dimension(0, 4));

		// Bottom Label Panel
		bottomLabelPanel = new JPanelPlus();
		bottomLabelPanel.setPanelColor(AppTheme.BOTTOM_PANEL_BG);
		bottomLabelPanel.setLayout(new BorderLayout());
		bottomLabelPanel.setBorder(new EmptyBorder(6, 12, 2, 12));
		bottomLabelPanel.add(leftLabel, BorderLayout.LINE_START);
		bottomLabelPanel.add(rightLabel, BorderLayout.LINE_END);
		
		// BottomPane
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(bottomLabelPanel, BorderLayout.NORTH);
		bottomPanel.add(stat, BorderLayout.SOUTH);
		getContentPane().add(bottomPanel, BorderLayout.PAGE_END);

		// TextArea
		tField = new RSyntaxTextArea();
		tField.setText("");
		tField.setTabSize(settings.getSetting(Settings.TAB_SIZE));
		//TODO: To be removed
		tField.setBorder(new EmptyBorder(2, 12, 6, 12));
		tField.setDragEnabled(true);
		tField.setPopupMenu(new RMBMenu(this));
		document = tField.getDocument();
		
		
		// Autocomplete features
		LanguageSupportFactory.get().register(tField);
		
		
		// ScrollPane
		tScrollPane = new RTextScrollPane(tField);
		tScrollPane.setBorder(BorderFactory.createEmptyBorder());
		tScrollPane.getVerticalScrollBar().setUnitIncrement(6);

		// Gutter
		jvp = tScrollPane.getRowHeader();
		gutter = tScrollPane.getGutter();
		gutter.setBorder(new Gutter.GutterBorder(0, 6, 0, 6));
		
		// Splitter
		splitter = new JSplitPaneWithZeroSizeDivider(JSplitPane.VERTICAL_SPLIT);
		splitter.setTopComponent(tScrollPane);
		splitter.setBottomComponent(null);
		splitter.setDividerSize(0);
		splitter.setResizeWeight(0.9);

		splitter.setBorder(BorderFactory.createEmptyBorder());
		getContentPane().add(splitter, BorderLayout.CENTER);

		// Set initial Values
		setTheme(settings.getSetting(Settings.THEME));
		setFont(settings.<String>getSetting(Settings.FONT_NAME));
		setText("", LanguageFactory.LANG_TEXT);
		carretUpdate();
		initConsole();
		toggleLineNumbers(settings.getSetting(Settings.IS_LINE_NUMBERS));
		setLineWrap(settings.getSetting(Settings.IS_LINE_NUMBERS));
		setCodeFolding(settings.getSetting(Settings.IS_CODE_FOLDING));
		
		// TODO Do this
//      setDefaultLookAndFeelDecorated(true);
//		setBackground(new Color(0,0,0,0));
//		tField.setBackground(new Color(0,0,0,0));
		
		// Pack
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(true);
		pack();

		// Parent Information
		if (parent != null) {
			setSize(new Dimension(parent.getWidth(), parent.getHeight()));
			setLocation(parent.getLocation().x + 30, parent.getLocation().y + 30);
		} else {
			setSize(new Dimension(settings.getSetting(Settings.WIDTH), settings.getSetting(Settings.HEIGHT)));
			setLocation(settings.getSetting(Settings.LAST_X), settings.getSetting(Settings.LAST_Y));
		}

		// After Pack
		tField.requestFocus();		
		updateMargin();
		setVisible(true);
		
		// Start BackupTimer
		backgroundSave = new BackgroundSave(this);
		backgroundSave.start();

		// Init Updaer
		updater = new Updater(this);


		// SHORTCUTS

		// Save
		mSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Save As
		mSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				((Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK))));
		// New
		mNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Open
		mOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Undo
		mUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Redo
		mRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				((Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK))));
		// Cut
		mCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Copy
		mCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Paste
		mPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Build
		mBuildRun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Build new
		mBuildRunNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
				((Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK))));
		// Quick Math
		mMath.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Quick Binary
		mBin.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Quick Binary Invert
		mBinInv.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,
				((Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK))));
		// Quick Java
		mCode.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Search
		mFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Quick Table
		mTable.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Symbols
		mSymbol.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		// Console
		mCon.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				((Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_MASK))));
		
		
		// ACTIONHANDLER

		// JMenu Listener
		mbEdit.addMenuListener(new MenuListener() {
			
			@Override
			public void menuSelected(MenuEvent e) {
				mUndo.setEnabled(tField.canUndo());
				mRedo.setEnabled(tField.canRedo());
			}
			
			@Override
			public void menuDeselected(MenuEvent e) {}
			
			@Override
			public void menuCanceled(MenuEvent e) {}
		});
		
		// New Window
		mNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new MonolithFrame(MonolithFrame.this);
			}
		});

		// New Window Independent
		mNewIndep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					Runtime.getRuntime().exec("java -jar Monolith.jar");
				} catch (IOException e) {
					console.println(e.getMessage(), Console.err);
					if(GlobalVariables.debug) e.printStackTrace();
				}
			}
		});

		// Pin
		pin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setAlwaysOnTop(!isPin);
				isPin = !isPin;
				if (isPin) {
					pin.setIcon(iPinDown);
				} else {
					pin.setIcon(iPinUp);
				}
			}
		});

		// Open
		mOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				openDialog();
			}
		});

		// Save
		mSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (hasDir) {
					MonolithFrame.this.saveFile(false);
				} else {
					MonolithFrame.this.saveFile(true);
				}
			}
		});

		// SaveAs
		mSaveAs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				MonolithFrame.this.saveFile(true);
			}
		});

		// Exit
		mExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				exitFile();
			}
		});

		// Undo
		mUndo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				undo();
			}
		});

		// Redo
		mRedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				redo();
			}
		});

		// Cut
		mCut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				tField.cut();
			}
		});

		// Copy
		mCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				tField.copy();
			}
		});

		// Paste
		mPaste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				tField.paste();
			}
		});

		// Language
		ActionListener sliceActionListener = new ActionListener() {
	      @Override
		public void actionPerformed(ActionEvent actionEvent) {
	        AbstractButton aButton = (AbstractButton) actionEvent.getSource();
	        setLanguage(LanguageFactory.getLanguageByString(aButton.getText()));
	      }
	    };
		
		for(int langIndex = 0; langIndex < LanguageFactory.languages.size(); langIndex++){
			languageButtons[langIndex].addActionListener(sliceActionListener);
		}
		
		// Settings
		mSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new SettingsDialog(MonolithFrame.this);
			}
		});

		// Search
		mFind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (!SearchDialog.modal)
					SearchDialog.showDialog(MonolithFrame.this);
			}
		});

		// Font size change on mouse wheel scroll
		tField.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if ((e.getModifiers() & Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()) == Toolkit
						.getDefaultToolkit().getMenuShortcutKeyMask()) {
					if (e.getWheelRotation() > 0)
						zoomIn(false);
					else
						zoomIn(true);
				} else {
					e.getComponent().getParent().dispatchEvent(e);
				}
			}
		});

		// Zoom In/Out
		tField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == (KeyEvent.VK_F5))
					reopen();
				
				if (e.isControlDown()) {
					if (e.getKeyChar() == '+')
						zoomIn(true);
					if (e.getKeyChar() == '-')
						zoomIn(false);
				}
			}
		});

		// Quick Code
		mCode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(language.quickCode == null){	
					console.println(language + " has no quick code", Console.warn);
					return;
				}
				
				if (getDocText().isEmpty()) {
					setText(language.quickCode, language);
				} else {
					new MonolithFrame(MonolithFrame.this).setText(language.quickCode, language);
				}
			}
		});

		// Quick Math
		mMath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				MonolithFrame.this.quickMath();
			}
		});

		// Quick Binary
		mBin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				MonolithFrame.this.quickBinary(true);
			}
		});

		// Quick Binary
		mBinInv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				MonolithFrame.this.quickBinary(false);
			}
		});

		////////
		// Build Menu
		///////

		// Build
		mBuild.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				stat.loadStart();
				buildNew = false;
				MonolithFrame.this.buildAndRun(CodeBuilder.BuildMode.BUILD);
				stat.loadEnd();
			}
		});
		
		// Run
		mRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				stat.loadStart();
				buildNew = false;
				MonolithFrame.this.buildAndRun(CodeBuilder.BuildMode.RUN);
				stat.loadEnd();
			}
		});
		
		// Build & run
		mBuildRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				stat.loadStart();
				buildNew = false;
				MonolithFrame.this.buildAndRun(CodeBuilder.BuildMode.BUILD_N_RUN);
				stat.loadEnd();
			}
		});

		// Build & run as new
		mBuildRunNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				stat.loadStart();
				buildNew = true;
				MonolithFrame.this.buildAndRun(CodeBuilder.BuildMode.BUILD_N_RUN);
				stat.loadEnd();
			}
		});

		// Build Config
		mBuildConf.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				new CustomBuildRunDialog(MonolithFrame.this, language, fullName, path);
			}
		});
		 
		
		// HELP
		
		// Check for updates
		mUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				updater.updateMonolith();
			}
		});
		
		// Help
		mHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(
						MonolithFrame.this,
						"<html><b>" + GlobalVariables.MONOLITH_NAME + " v" + GlobalVariables.VERSION + ":" + GlobalVariables.BUILD + "</b><br> by Haeri Studios &#9400;<br>For more information visit <br><a href='http://monolith-code.net.tiberius.sui-inter.net/'>monolith-code.net</a></html>",
						GlobalVariables.MONOLITH_NAME,
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// DEBUG

		// Char to Int
		mToInt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String temp = getText();
				String build = "";
				for (int i = 0; i < temp.length(); i++) {
					int temp2 = temp.charAt(i);
					if (temp2 == 10) {
						build += "[LF]\n";
					} else if (temp2 == 13) {
						build += "[CR]";
					} else if (temp2 == 9) {
						build += "[tab]\t";
					} else if (temp2 == 32) {
						build += "[spc]\t";
					} else {
						build += temp2 + "\t";
					}
				}
				new MonolithFrame(MonolithFrame.this).setText(build);
			}
		});

		// Show Paragraph
		mPgrp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String temp = getText();
				AbstractButton aButton = (AbstractButton) event.getSource();
				boolean selected = aButton.getModel().isSelected();
				if (selected) {
					temp = temp.replaceAll("(\n)", "\u00B6\u0080\n");
					temp = temp.replaceAll("(\r)", "\u00B5\u0080\r");
					temp = temp.replaceAll("(\t)", "\u0362\u0080\t");
					temp = temp.replaceAll(" ", "\u00B7\u0080");
				} else {
					temp = temp.replaceAll("\u00B6\u0080\n", "\n");
					temp = temp.replaceAll("\u00B5\u0080\r", "\r");
					temp = temp.replaceAll("\u0362\u0080\t", "\t");
					temp = temp.replaceAll("\u00B7\u0080", " ");
				}

				setText(temp);

			}
		});

		// LolCrypt
		mCrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setText((LolCrypt.crypt(getText())));
			}
		});

		// DeLolCrypt
		mDeCrypt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				setText((LolCrypt.deCrypt(getText())));
			}
		});

		// Quick Table
		mTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Table.showSettingsDialog(MonolithFrame.this);
			}
		});

		// Symbols
		mSymbol.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (!SymbolDialog.modal)
					SymbolDialog.showDialog(MonolithFrame.this);
			}
		});

		// Console
		mCon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				toggleConsole();
			}
		});

		// Line Numbers
		mLineB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				toggleLineNumbers();
			}
		});

		// Drag&Drop
		tField.setDropTarget(new DropTarget() {
			private static final long serialVersionUID = 1L;

			@Override
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					Transferable t = evt.getTransferable();

					if (evt.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
						// Files were Dnd
						evt.acceptDrop(evt.getDropAction());

						@SuppressWarnings("unchecked")
						List<File> droppedFiles = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
						for (File file : droppedFiles) {
							if (tField.getText().equals("")) {
								readFromFile(file.getAbsolutePath());
							} else {
								new MonolithFrame(file.getAbsolutePath(), MonolithFrame.this);
							}
						}
						evt.dropComplete(true);
					} else if (evt.isDataFlavorSupported(DataFlavor.stringFlavor)) {
						// Text was Dnd
						evt.acceptDrop(evt.getDropAction());
						String s = (String) t.getTransferData(DataFlavor.stringFlavor);
						int pos = tField.viewToModel(evt.getLocation());
						document.insertString(pos, s, null);

						evt.dropComplete(true);
					} else
						evt.rejectDrop();
				} catch (IOException | UnsupportedFlavorException | BadLocationException e) {
					console.println(e.getMessage(), Console.err);
					if(GlobalVariables.debug) e.printStackTrace();
				}
			}
		});

		// Close
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exitFile();
			}
		});

		// DocumentListener
		document.addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {}

			@Override
			public void insertUpdate(DocumentEvent e) {
				inputUpdate();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				inputUpdate();
			}
		});

		// Caret Position
		tField.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent carr) {
				carretUpdate();
			}
		});

		// Right click
		tField.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && !e.isConsumed()) {
					e.consume();
					searchHighlight();
				} else {
					tField.clearMarkAllHighlights();
				}
			}
		});

		// Resize tField
		tFieldVievport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, tField);
		tFieldVievport.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				updateMargin();
			}
		});

		// Resize Frame
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent event) {
				settings.setSetting(Settings.WIDTH, MonolithFrame.this.getWidth());
				settings.setSetting(Settings.HEIGHT, MonolithFrame.this.getHeight());
			}
		});

		// Finished initializing
		if (path != null) {
			readFromFile(path);
		}
		
		// Check for updates
		if (isFirst) {
			isFirst = false;
			updater.start();
		}
		
		console.printQueue();
	}
	
	// Update


	// History
	public void undo() {
		if(tField.canUndo()) tField.undoLastAction();
	}
 
	public void redo() {
		if(tField.canRedo()) tField.redoLastAction();
	}

	// Zoom in
	public void zoomIn(boolean in) {
		if (in) {
			if (getFontSize() + 2 <= 60) {
				setFontSize(getFontSize() + 2);
			}
		} else {
			if (getFontSize() - 2 >= 6) {
				setFontSize(getFontSize() - 2);
			}
		}
	}

	// Triggered when tField input/remove occurred
	public void inputUpdate() {
		inputUpdate(false);
	}

	public void inputUpdate(boolean first) {
		// Disable while reading
		if (!isReading) {
			// add * to title
			if (!hasChange && !first) {
				hasChange = true;
				MonolithFrame.this.setTitle(MonolithFrame.this.getTitle() + "*");
			}
			chars = getDocText().length();
		}
	}

	// Show/Hide Debug menu
	public void toggleDebug() {
		mbDBug.setVisible(!mbDBug.isVisible());
		GlobalVariables.debug = !GlobalVariables.debug;
	}

	// Show/Hide Console
	public void toggleConsole(boolean open) {
		if(!settings.getSetting(Settings.IS_CONSOLE).equals(open))
			toggleConsole();
	}

	public void toggleConsole() {
		settings.setSetting(Settings.IS_CONSOLE, !settings.<Boolean>getSetting(Settings.IS_CONSOLE));
		initConsole();
	}

	public void toggleLineNumbers(boolean state) {
		settings.setSetting(Settings.IS_LINE_NUMBERS, state);
		if (settings.<Boolean>getSetting(Settings.IS_LINE_NUMBERS)) {
			tScrollPane.setRowHeader(jvp);
		} else {
			tScrollPane.setRowHeader(null);
		}
		mLineB.setSelected(settings.getSetting(Settings.IS_LINE_NUMBERS));
	}
	
	// Show/Hide Line Numbers
	public void toggleLineNumbers() {
		toggleLineNumbers(!settings.<Boolean>getSetting(Settings.IS_LINE_NUMBERS));
	}

	/**
	 * Build And Run
	 * First the name of the class gets detected. Then the file
	 * gets saved. Then a process is created that executes according
	 * to the declared mode.
	 * @param mode the mode that should be used. Either BUILD, RUN, BUILD_N_RUN or FREE_BUILD;
	 */
	public void buildAndRun(CodeBuilder.BuildMode mode) {

		// Old check if a Build/Run is allowed for the current language
		/*
		if ((code == CodeBuilder.BUILD_N_RUN || code == CodeBuilder.BUILD) && !language.isCompilable
				&& !CustomCommandSerializer.getCCE(language.name).isCustomBuildCommand) {
			console.println("Can't compile " + language, Console.warn);
			return;
		}
		if ((code == CodeBuilder.BUILD_N_RUN || code == CodeBuilder.RUN) && !language.isRunnable
				&& !CustomCommandSerializer.getCCE(language.name).isCustomRunCommand) {
			console.println("Can't run " + language, Console.warn);
			return;
		}
		*/

		
		
		if (buildConsole != null){
			buildConsole.requestFocus();
		}

		// match file name
		if (language.forceClassName) {
			String detectedName = suggetFileName();

			if (!fullName.equals("") && !fullName.equals(detectedName) && !ignoreNameDiscrepency) {
				stat.warning();
				int changeName = JOptionPane.showConfirmDialog(MonolithFrame.this,
						"The Class Name does not match with the filename!\nWould you like to change it",
						"Different Class Name", JOptionPane.YES_NO_CANCEL_OPTION);
				if (changeName == JOptionPane.YES_OPTION) {
					fullName = detectedName;
					if(buildConsole != null){
						buildConsole.fullname = fullName;
						buildConsole.path = path;
					}
				} else if (changeName == JOptionPane.NO_OPTION) {
					ignoreNameDiscrepency = true;
				} else if (changeName == JOptionPane.CANCEL_OPTION) {
					return;
				}
			}
		}

		// Save file
		if (hasDir) {
			if (!MonolithFrame.this.saveFile(false)) {
				return;
			}
		} else {
			if (!MonolithFrame.this.saveFile(true)) {
				return;
			}
		}

		// Build & Run
		BuildConsole MyBuildConsole;
		if ((buildNew || buildConsole == null) || buildConsole.isKill) {
			// if you want to create new window or if it is the first build
			MyBuildConsole = new BuildConsole(path, fullName, language, this);
			buildConsole = MyBuildConsole;
		} else {
			if (buildConsole.language != language) {
				// if you don't want to create a new window but the language
				// is different
				MyBuildConsole = new BuildConsole(path, fullName, language, this);
				buildConsole = MyBuildConsole;
			}
		}

		if (mode == CodeBuilder.BuildMode.BUILD)
			buildConsole.build();
		else if (mode == CodeBuilder.BuildMode.RUN)
			buildConsole.run();
		else if (mode == CodeBuilder.BuildMode.BUILD_N_RUN)
			buildConsole.buildRun();
	}

	/**
	 * Quick Math A selected String gets of numbers and arithmetical operations
	 * gets calculated and the result gets printed out next to the selection.
	 */
	public void quickMath() {
		if (!getDocText().isEmpty()) {
			int mStart = 0;
			int mEnd = 0;
			String math = "";

			// Read equation
			if (tField.getSelectedText() == null) {
				// Without Selection
				mEnd = tField.getCaretPosition();
				try {
					int lines = tField.getLineOfOffset(mEnd);
					int lineStart = tField.getLineStartOffset(lines);
					math = tField.getText(lineStart, mEnd - lineStart);
					mStart = lineStart;
				} catch (BadLocationException e) {
					console.println("Couldn't get the equation from the line!\n" + e.getMessage(), Console.err);
					if(GlobalVariables.debug) e.printStackTrace();
				}
			} else {
				// With Selection
				mStart = tField.getSelectionStart();
				mEnd = tField.getSelectionEnd();
				math = tField.getSelectedText();
			}

			// Filter
			math = math.replaceAll("ans", "" + ans);
			String outString = math;
			math = math.replaceAll("'", "");
			Util.println(math);

			// Calculate and print
			if (!math.isEmpty()) {

				BigDecimal result = null;
				try {
					Expression ex = new Expression(math).setPrecision(128);
					result = ex.eval();
				} catch (Exception e) {
					console.println("Can't solve: " + math + "\n" + e.getMessage(), Console.err);
					if(GlobalVariables.debug) e.printStackTrace();
				}

				try {
					if (result != null) {
						// Post processing
						String answer = result.toPlainString();
						int index = answer.indexOf(".");
						if (index != -1) {
							if (answer.length() - index > GlobalVariables.mathRound + 1) {
								answer = answer.substring(0, index + GlobalVariables.mathRound + 1);
							}
						}

						document.insertString(mEnd, outString + " = " + answer, null);
						document.remove(mStart, mEnd - mStart);
						ans = Double.parseDouble(answer);
					}
				} catch (BadLocationException e) {
					console.println("Inserting result didn't work!\n" + e.getMessage(), Console.err);
					if(GlobalVariables.debug) e.printStackTrace();
				}
			} else {
				console.println("Can't calculate an empty line", Console.warn);				
			}
		} else {
			console.println("The document is empty", Console.warn);
		}
	}

	/**
	 * Binary converter
	 */
	public void quickBinary(boolean to) {
		if (!getDocText().isEmpty()) {
			int mEnd = 0;
			String math = "";

			// Read equation
			if (tField.getSelectedText() == null) {
				// Without Selection
				mEnd = tField.getCaretPosition();
				try {
					int lines = tField.getLineOfOffset(mEnd);
					int lineStart = tField.getLineStartOffset(lines);
					math = tField.getText(lineStart, mEnd - lineStart);
				} catch (BadLocationException e) {
					console.println("Couldn't get the text from the line!\n" + e.getMessage(), Console.err);
					if(GlobalVariables.debug) e.printStackTrace();
				}
			} else {
				// With Selection
				mEnd = tField.getSelectionEnd();
				math = tField.getSelectedText();
			}

			// Filter
			math = math.replaceAll("'", "");
			Util.println(math);

			// Calculate and print
			if (!math.isEmpty()) {
				String result = "";
				try {
					if (to) {
						int mat = Integer.parseInt(math);
						result = Integer.toBinaryString(mat);
					} else {
						result = Integer.parseInt(math, 2) + "";
					}
				} catch (Exception e) {
					console.println("Invalid input for conversion!\n" + e.getMessage(), Console.err);
					if(GlobalVariables.debug) e.printStackTrace();
					return;
				}
				// Print
				try {
					document.insertString(mEnd, " = " + result.toString(), null);
					stat.confirm();
				} catch (BadLocationException e) {
					console.println("Inserting result didn't work!\n" + e.getMessage(), Console.err);
					if(GlobalVariables.debug) e.printStackTrace();
				}
			} else {
				console.println("There is nothing to convert", Console.warn);
			}
		}
	}

	public void insertText(String text) {
		int car = tField.getCaretPosition();
		try {
			document.insertString(car, text, null);
		} catch (BadLocationException e) {
			console.println("Inserting text didn't work!\n" + e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
		}
	}
	
	/**
	 * Resets settings to default state
	 */
	public void resetSettings(){
		setFontSize(Settings.DEF_FONT_SIZE);
		setTabSize(Settings.DEF_TAB_SIZE);
		setFont(Settings.DEF_FONT_NAME);
		setTheme(Settings.DEF_THEME);
		setCodeFolding(Settings.DEF_IS_CODE_FOLDING);
		setLineWrap(Settings.DEF_IS_LINE_WRAP);

		tField.updateUI();
	}

	public void reopen(){
		if(hasChange){
			saveFile(true);
		}else if(!hasChange && path != "/"){
			readFromFile(path + fullName);
			stat.confirm();
		}
	}

	public String getFullName() {
		return fullName;
	}

	public int getTabSize() {
		return settings.getSetting(Settings.TAB_SIZE);
	}

	public int getFontSize() {
		return settings.getSetting(Settings.FONT_SIZE);
	}

	public String getText() {
		return getDocText();
	}

	public ColorPlus getTextBackground(){
		return ColorPlus.toStyle(tField.getBackground());
	}

	public boolean getModal() {
		return isPin;
	}

	public String getDocText() {
		try {
			return document.getText(0, document.getLength());
		} catch (BadLocationException e) {
			console.println(e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
		}
		return "";
	}

	/**
	 * Display text in the text area
	 * @param text - String text to be displayed
	 */
	public void setText(String text) {
		setText(text, language);
	}
	
	/**
	 * Display text in the text area with new language
	 * @param text - String text to be displayed
	 * @param language - Language to be set
	 */
	public void setText(String text, Language language) {
		JLabel load = new JLabel(" loading... ", gifLoading, SwingConstants.CENTER);
		splitter.setTopComponent(load);

		try {
			tField.setText("");
			document.insertString(0, text, null);
		} catch (Exception e) {
			console.println(e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
		}

		tField.setDocument(document);
		splitter.setTopComponent(tScrollPane);
		setLanguage(language);
	}
	
	/**
	 * set tab size
	 * @param size - int tab size to be set
	 */
	public void setTabSize(int size) {
		settings.setSetting(Settings.TAB_SIZE, size);
		tField.setTabSize(size);
	}

	/**
	 * Set font size
	 * @param size - int size to be set
	 */
	public void setFontSize(int size) {
		settings.setSetting(Settings.FONT_SIZE, size);
		String tempFont = tField.getFont().getFontName();
		int tempStyle = tField.getFont().getStyle();
		tField.setFont(new Font(tempFont, tempStyle, size));
		gutter.setLineNumberFont(new Font(tempFont, tempStyle, size));
		
		//setTabSize(settings.getTabSize());
		//updateMargin();
		//repaint();
	}

	/**
	 * Set font
	 * @param fontName - String font name to be set
	 */
	public void setFont(String fontName) {
		int tempSize = tField.getFont().getSize();
		int tempStyle = tField.getFont().getStyle();
		Font tempFont = new Font(fontName, tempStyle, tempSize);
		
		settings.setSetting(Settings.FONT_NAME, fontName);
		tField.setFont(tempFont);
		gutter.setLineNumberFont(tempFont);
		
		//updateMargin();
		//repaint();
	}
	
	/**
	 * Set Line Wrap
	 * @param isLineWrap - Boolean should Lines wrap or not
	 */
	public void setLineWrap(boolean isLineWrap) {
		settings.setSetting(Settings.IS_LINE_WRAP, isLineWrap);
		tField.setLineWrap(isLineWrap);
	}
	
	/**
	 * Set Code Folding
	 * @param isCodeFolding - Boolean should code folding be activated
	 */
	public void setCodeFolding(boolean isCodeFolding) {
		settings.setSetting(Settings.IS_CODE_FOLDING, isCodeFolding);
		tField.setCodeFoldingEnabled(isCodeFolding);
	}

	/**
	 * Set a theme
	 * @param themeName - String theme name to be set
	 */
	public void setTheme(String themeName) {
		boolean succes = true;

		Theme theme = null;
		try {
			String _path = DIR_THEMES + themeName + ".xml";
			theme = Theme.load(getClass().getResourceAsStream(_path));
		} catch (Exception e) {
			succes = false;
		}

		if (!succes) {
			try {
				String _path = "themes/" + themeName + ".xml";
				theme = Theme.load(new FileInputStream(_path));
				succes = true;
			} catch (Exception e) {
				console.println("Could not load Theme " + themeName + "!\n" + e.getMessage(), Console.err);
				if(GlobalVariables.debug) e.printStackTrace();
				setTheme(Settings.DEF_THEME);
			}
		}
		if (succes){
			theme.apply(tField);
			settings.setSetting(Settings.THEME, themeName);
			
			ScrollBarPlus hBar = new ScrollBarPlus(tField.getBackground());
			ScrollBarPlus vBar = new ScrollBarPlus(tField.getBackground());
			tScrollPane.getHorizontalScrollBar().setUI(hBar);
			tScrollPane.getVerticalScrollBar().setUI(vBar);
			tScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, AppTheme.SCROLL_BAR_WIDTH));
			tScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(AppTheme.SCROLL_BAR_WIDTH, 0));
			
			JPanel left = new JPanel();
			JPanel right = new JPanel();
			left.setBackground(tField.getBackground());
			right.setBackground(tField.getBackground());
			tScrollPane.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, right);
			tScrollPane.setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, left);
			
			
			bottomLabelPanel.setPanelColor(theme.gutterBackgroundColor);
			leftLabel.setForeground(theme.lineNumberColor);
			rightLabel.setForeground(theme.lineNumberColor);
		}
		overrideTheme();
	}

	/**
	 * Set editor language
	 * @param language - Language language to be set
	 */
	public void setLanguage(Language language) {
		this.language = language;
		//TODO: Fix JavaScript bug
		try{
			tField.setSyntaxEditingStyle(language.syntaxConstant);
		}catch(Exception e){
			e.printStackTrace();
		}
		languageButtons[language.index].setSelected(true);

		carretUpdate();
	}

	public Language getLanguage() {
		return language;
	}

	public void replaceSegemnt(int start, int end, String text){
		if(start > end ) return;

		try{
			document.insertString(end, text, null);
			document.remove(start, end - start);
		} catch (BadLocationException e) {
			console.println("Couldn't replace Text!\n" + e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
		}
	}
	
	public String suggetFileName(){
		return language.suggestName(getText());
	}

	// Triggered when caret was changed
	private void carretUpdate() {
		int carrPos = tField.getCaretPosition();
		int lines = 1;
		int cols = 1;
	
		// Update lines and columns
		try {
			lines = tField.getLineOfOffset(carrPos);
			cols = carrPos - tField.getLineStartOffset(lines) + 1;
			lines++;
		} catch (BadLocationException e) {
			console.println("Couldn't calculate Lines and Rows!\n" + e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
		}
		leftLabel.setText("Line: " + lines + "   Col: " + cols + "   |   Chars: " + chars);
		rightLabel.setText(language.name);
	}

	private void initConsole(){
		if (settings.<Boolean>getSetting(Settings.IS_CONSOLE)) {
			if (dividerLocation != 0)
				splitter.setDividerLocation(dividerLocation);
			splitter.setBottomComponent(console);
			splitter.setDividerSize(1);
			updateMargin();
			console.down();
		} else {
			dividerLocation = splitter.getDividerLocation();
			splitter.setBottomComponent(null);
			splitter.setDividerSize(0);
			updateMargin();
		}
		mCon.setSelected(settings.getSetting(Settings.IS_CONSOLE));
	}

	// Empty Space Update
	private void updateMargin() {
		JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, tField);
	
		if (viewport != null) {
			int len = document.getLength();
			try {
				Rectangle end = tField.modelToView(len);
				if (end != null) {
					//TODO: Increase top margin to 8 when issue #236 is resolved
					tField.setBorder(new EmptyBorder(2, 6, viewport.getHeight() - (end.height + 2), 6));
				}
			} catch (BadLocationException e) {
				if(GlobalVariables.debug) e.printStackTrace();
			}
		}
	}

	public boolean saveFile(boolean as) {
		String toStore = getDocText();
		
		if (as) { // Save As
			// Some FXChooser magic
			SynchronousJFXFileChooser chooser = new SynchronousJFXFileChooser(() -> {
				FileChooser ch = new FileChooser();
				ch.setTitle("Save file");
				return ch;
			});

			File file = chooser.showDialog(ch -> {
				// Def file path
				if (new File(path + fullName).exists()) {
					ch.setInitialDirectory(new File(path));
				} else {
					ch.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
				}
				
				String tempFullName = null;
				if(fullName.equals("")){
					tempFullName = suggetFileName();
				}
				
				// Def file name
				ch.setInitialFileName(tempFullName);
				ch.setSelectedExtensionFilter(language.extensionFilter);

				// File Filter
				ch.getExtensionFilters().add(EXTENSION_FILTER_ALL);
				for(int i = 0; i < LanguageFactory.languages.size(); i++){
					ch.getExtensionFilters().add(LanguageFactory.languages.get(i).extensionFilter);
				}
				
				// TODO make return file uniform for all platforms
				File f = ch.showSaveDialog(null);
				Util.println(ch.getSelectedExtensionFilter().getDescription());

				return f;
			});

			if (file != null) {
				path = file.getParent() + "/";
				// TODO make this platform dependent
				path = path.replace("\\", "/");

				fullName = file.getName();
				
				if(LanguageFactory.getLanguageFromFile(fullName) != null)
					setLanguage(LanguageFactory.getLanguageFromFile(fullName));					
				else
					setLanguage(language);					

			} else {
				return false;
			}
		}

		// Save routine
		FileExplorer text = new FileExplorer(path, fullName, toStore);
		try {
			text.writeFile();
			hasDir = true;
			hasChange = false;
			MonolithFrame.this.setTitle(GlobalVariables.MONOLITH_NAME + " - " + fullName);
			console.println(">> " + fullName + " was saved in " + path);
			stat.confirm();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// File could not be written
			console.println(e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error: Could not write file!", JOptionPane.OK_OPTION);
			return false;
		}

		return true;
	}

	public void exitFile() {
		boolean close = true;
		if ((hasDir && !hasChange) || (!hasDir && !hasChange)) {
			// File has directory and has not been changed.
			MonolithFrame.this.dispose();
		} else {
			int answer = JOptionPane.showConfirmDialog(MonolithFrame.this, "Would you like save " + fullName + "?",
					fullName + " not saved", JOptionPane.YES_NO_CANCEL_OPTION);
			if (answer == JOptionPane.YES_OPTION) {

				if (hasDir && hasChange) {
					// File has directory and needs to be saved again
					MonolithFrame.this.saveFile(false);
					MonolithFrame.this.dispose();
				} else {
					// File needs to be saved and save dialog appears
					MonolithFrame.this.saveFile(true);
					if (!hasChange && hasDir) {
						// File was successfully saved
						MonolithFrame.this.dispose();
					} else {
						// Save dialog was aborted
						close = false;
					}
				}
			} else if (answer == JOptionPane.NO_OPTION) {
				// Files does not require a save
				MonolithFrame.this.dispose();
			} else {
				// Save prompt was canceled
				close = false;
			}
		}

		// Background save
		backgroundSave.save();

		// Save window size
		settings.saveWindow(getX(), getY(), getWidth(), getHeight());
		settings.saveConfigOnExit();

		if (close) {
			exit();
		}

	}

	private void openDialog() {
		stat.loadStart();

		// Some FXChooser magic
		SynchronousJFXFileChooser chooser = new SynchronousJFXFileChooser(() -> {
			FileChooser ch = new FileChooser();
			ch.setTitle("Open file");
			return ch;
		});
		File file = chooser.showDialog(ch -> {
			// Default file path
			if (new File(path + fullName).exists()) {
				ch.setInitialDirectory(new File(path));
			} else {
				ch.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
			}
			return ch.showOpenDialog(null);
		});

		if (file != null) {
			if (getDocText().isEmpty()) {
				readFromFile(file.toString());
			} else {
				new MonolithFrame(file.toString(), MonolithFrame.this);
			}
		}

		stat.loadEnd();
	}
		
	/**
	 * Read from file and display
	 * @param path Path to the file to be read
	 */
	private void readFromFile(String path) {
		// Prepare for reading
		stat.loadStart();
		tField.setEditable(false);
		isReading = true;
		hasDir = true;
		hasChange = false;
		String tempFullName = new File(path).getName();;

		// Read file
		FileExplorer file = new FileExplorer(path);
		// Display file
		if(LanguageFactory.getLanguageFromFile(tempFullName) != null)
			setText(file.getString(), LanguageFactory.getLanguageFromFile(tempFullName));
		else
			setText(file.getString(), LanguageFactory.LANG_TEXT);
		
		// Set properties
		tField.setCaretPosition(0);
		fullName = tempFullName;
		this.path = path.substring(0, path.length() - fullName.length());
		setTitle(GlobalVariables.MONOLITH_NAME + " - " + fullName);

		// Post read settings
		console.println(">> Opened " + fullName + " as " + language);
		isReading = false;
		tField.setEditable(true);
		inputUpdate(true);
		stat.loadEnd();
	}

	// highlight all discovered matching strings
	private void searchHighlight() {	
		try {
			String search = tField.getSelectedText();
			SearchContext context = new SearchContext();
			context.setSearchFor(search);
			context.setMatchCase(true);
			context.setRegularExpression(false);
			context.setWholeWord(true);
			SearchEngine.markAll(tField, context);

		} catch (Exception e) {
			if(GlobalVariables.debug) e.printStackTrace();
		}
	}
	
	private void loadThemes() {
		File[] files = new File("themes/").listFiles();

		int cnt = DEF_THEMES.length;

		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && LanguageFactory.getExtension(files[i].getName()).equals(LanguageFactory.LANG_XML.extension)) {
					cnt++;
				}
			}
		}

		GlobalVariables.loadedThemes = new String[cnt];

		for (int i = 0; i < DEF_THEMES.length; i++) {
			GlobalVariables.loadedThemes[i] = DEF_THEMES[i];
		}

		if (files != null) {
			int tmp = 0;
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && LanguageFactory.getExtension(files[i].getName()).equals(".xml")) {
					GlobalVariables.loadedThemes[tmp++ + DEF_THEMES.length] = LanguageFactory.getName((files[i].getName()));
				}
			}
		}
	}

	private void overrideTheme(){
		tField.setTabSize(settings.getSetting(Settings.TAB_SIZE));
		setFontSize(settings.getSetting(Settings.FONT_SIZE));
		stat.setBG(bottomLabelPanel.getPanelColor());
	}

	private void loadIcons(Color c){

		float luminance = (c.getRed() * 0.2126f + c.getGreen() * 0.7152f + c.getBlue() * 0.0722f) / 255;
		String type;

		if (luminance >= 0.5f) {
			type = "dark";
		} else {
			type = "light";
		}

		iCut 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/cut.png"));
		iCopy 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/copy.png"));
		iPaste 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/paste.png"));
		iConsole 	= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/console.png"));
		iRun 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/run.png"));
		iMath 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/math.png"));
		iSave 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/save.png"));
		iSaveas 	= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/saveAs.png"));
		iCode 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/code.png"));
		iBinary 	= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/binary.png"));
		iPinUp 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/pinUp.png"));
		iPinDown 	= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/pinDown.png"));
		iUndo 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/undo.png"));
		iRedo 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/redo.png"));
		iOpen 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/open.png"));
		iNew 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/new.png"));
		iNewFork 	= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/newFork.png"));
		iExit 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/exit.png"));
		iTable 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/table.png"));
		iEarth 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/earth.png"));
		iInfo 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/info.png"));
		iStop 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/stop.png"));
		iBuild 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/build.png"));
		iBuildRun 	= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/buildRun.png"));
		iBuildRunNew= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/buildRunNew.png"));
		iBuildConfig= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/buildConfig.png"));
		iUpdate 	= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/update.png"));
		
		iFind 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/find.png"));
		iCog 		= new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/toolbar/18x18/" + type + "/cog.png"));
		gifLoading 	= new ImageIcon(getClass().getResource(GlobalVariables.RESOURCE_PATH + "/loader.gif"));
		
		
		// Set icon images
		List<BufferedImage> mainIcons = new ArrayList<BufferedImage>();
		try {
			BufferedImage icon_20 = ImageIO.read(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/icon_20.png"));
			BufferedImage icon_32 = ImageIO.read(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/icon_32.png"));
			BufferedImage icon_40 = ImageIO.read(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/icon_40.png"));
			BufferedImage icon_64 = ImageIO.read(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/icon_64.png"));
			BufferedImage icon_128 = ImageIO.read(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/icon_128.png"));
			BufferedImage icon_256 = ImageIO.read(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/icon_256.png"));
			
			mainIcons.add(icon_20);
			mainIcons.add(icon_32);
			mainIcons.add(icon_40);
			mainIcons.add(icon_64);
			mainIcons.add(icon_128);
			mainIcons.add(icon_256);
			
			setIconImages(mainIcons);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	} 

}
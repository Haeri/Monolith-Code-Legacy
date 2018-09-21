package net.monolith;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class CustomBuildRunDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;

	private JCheckBox checkBoxExec;
	private JTextPane execTextPane, buildTextPane;
	private JButton btnSave, btnCancel;
	private boolean tempActiveBuild, tempActiveRun, tempUseNative ;
	private Style style, defaultStyle;
	private JLabel lblUseAndTo;
	private CustomCommandEntry cce;
	private StyledDocument execDoc, buildDoc;
	String name, path;

	public static String[] commandLine;
	private JPanel buildPanel;
	private JCheckBox checkBoxBuild;
	private JPanel panel;
	private JPanel langPanel;
	private JLabel lblSelectLanguage;
	private JComboBox<Object> LanguageComboBox;
	private JCheckBox chckbxUseNativeConsole;
	private Component horizontalStrut;
	
	public CustomBuildRunDialog(JFrame frame, Language language, String name, String path){
		super(frame);
		this.name = name;
		this.path = path;
	
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		langPanel = new JPanel();
		langPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		FlowLayout flowLayout = (FlowLayout) langPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		getContentPane().add(langPanel, BorderLayout.NORTH);
		
		lblSelectLanguage = new JLabel("Select Language");
		langPanel.add(lblSelectLanguage);
		
		LanguageComboBox = new JComboBox<Object>(LanguageFactory.languages.toArray());
		LanguageComboBox.setSelectedItem(language);
		langPanel.add(LanguageComboBox);
		
		horizontalStrut = Box.createHorizontalStrut(100);
		langPanel.add(horizontalStrut);
		
		chckbxUseNativeConsole = new JCheckBox("Use Native Console");
		langPanel.add(chckbxUseNativeConsole);
		
		// Language comboBox
		LanguageComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				showLanguageSettings((Language) LanguageComboBox.getSelectedItem());
			}
		});
			
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(panel, BorderLayout.WEST);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		buildPanel = new JPanel();
		panel.add(buildPanel);
		buildPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Build Configuration", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagLayout gbl_buildPanel = new GridBagLayout();
		gbl_buildPanel.columnWidths = new int[]{0, 0};
		gbl_buildPanel.rowHeights = new int[]{0, 56, 0, 0};
		gbl_buildPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_buildPanel.rowWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		buildPanel.setLayout(gbl_buildPanel);
		
		checkBoxBuild = new JCheckBox("Use custom Build Command");
		GridBagConstraints gbc_checkBoxBuild = new GridBagConstraints();
		gbc_checkBoxBuild.anchor = GridBagConstraints.WEST;
		gbc_checkBoxBuild.insets = new Insets(0, 0, 5, 0);
		gbc_checkBoxBuild.gridx = 0;
		gbc_checkBoxBuild.gridy = 0;
		buildPanel.add(checkBoxBuild, gbc_checkBoxBuild);
		
		JScrollPane buildScrollPane = new JScrollPane();
		GridBagConstraints gbc_buildScrollPane = new GridBagConstraints();
		gbc_buildScrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_buildScrollPane.fill = GridBagConstraints.BOTH;
		gbc_buildScrollPane.gridx = 0;
		gbc_buildScrollPane.gridy = 1;
		buildPanel.add(buildScrollPane, gbc_buildScrollPane);
		
		buildTextPane = new JTextPane();
		buildScrollPane.setViewportView(buildTextPane);
		
		JPanel runPanel = new JPanel();
		panel.add(runPanel);
		runPanel.setBorder(new TitledBorder(null, "Execute Configuration", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_runPanel = new GridBagLayout();
		gbl_runPanel.columnWidths = new int[]{0, 0};
		gbl_runPanel.rowHeights = new int[]{0, 55, 0};
		gbl_runPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_runPanel.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		runPanel.setLayout(gbl_runPanel);
		
		checkBoxExec = new JCheckBox("Use custom Execute Configuration");
		GridBagConstraints gbc_chckBoxExec = new GridBagConstraints();
		gbc_chckBoxExec.anchor = GridBagConstraints.WEST;
		gbc_chckBoxExec.insets = new Insets(0, 0, 5, 0);
		gbc_chckBoxExec.gridx = 0;
		gbc_chckBoxExec.gridy = 0;
		runPanel.add(checkBoxExec, gbc_chckBoxExec);
		
		JScrollPane execScrollPane = new JScrollPane();
		GridBagConstraints gbc_execScrollPane = new GridBagConstraints();
		gbc_execScrollPane.fill = GridBagConstraints.BOTH;
		gbc_execScrollPane.gridx = 0;
		gbc_execScrollPane.gridy = 1;
		runPanel.add(execScrollPane, gbc_execScrollPane);
		
		execTextPane = new JTextPane();
		execScrollPane.setViewportView(execTextPane);
		
		lblUseAndTo = new JLabel(" Use <name> and <path> to mark the file variables");
		lblUseAndTo.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panel.add(lblUseAndTo);
		
		
		// Use Native Console
		chckbxUseNativeConsole.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				tempUseNative = chckbxUseNativeConsole.isSelected();
			}
		});
		
		// Build CheckBox
		checkBoxBuild.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				tempActiveBuild = checkBoxBuild.isSelected();
				setActiveBuild(tempActiveBuild);
			}
		});
		
		// Exec CheckBox
		checkBoxExec.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				tempActiveRun = checkBoxExec.isSelected();
				setActiveRun(tempActiveRun);
			}
		});
		
		execDoc = execTextPane.getStyledDocument();
		buildDoc = buildTextPane.getStyledDocument();
		
		// DocumentListener
		execTextPane.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {}

			@Override
			public void insertUpdate(DocumentEvent e) {recognizeText(execDoc);}

			@Override
			public void removeUpdate(DocumentEvent e) {recognizeText(execDoc);}
		});
		
		// DocumentListener
		buildTextPane.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {}

			@Override
			public void insertUpdate(DocumentEvent e) {recognizeText(buildDoc);}

			@Override
			public void removeUpdate(DocumentEvent e) {recognizeText(buildDoc);}
		});
		
		style = execTextPane.addStyle("MyHilite", null);
		
		JPanel inputPanel = new JPanel();
		FlowLayout fl_inputPanel = (FlowLayout) inputPanel.getLayout();
		fl_inputPanel.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(inputPanel, BorderLayout.SOUTH);
		
		
		btnSave = new JButton("Save");
		inputPanel.add(btnSave);
		
		btnCancel = new JButton("Cancel");
		inputPanel.add(btnCancel);
		
		// Save Button
		btnSave.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				
				cce.useNativeConsole = tempUseNative;
				cce.isCustomBuildCommand = tempActiveBuild;
				cce.isCustomRunCommand = tempActiveRun;
				cce.customBuildCommand = buildTextPane.getText();
				cce.customRunCommand = execTextPane.getText();
				
				CustomCommandSerializer.addEntry(cce);
				try {
					CustomCommandSerializer.writeCC();
				} catch (FileNotFoundException e2) {
					if(GlobalVariables.debug) e2.printStackTrace();
				}
				CustomBuildRunDialog.this.dispose();
			}
		});
		
		// Cancel Button
		btnCancel.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				CustomBuildRunDialog.this.dispose();
			}
		});
		
		StyleConstants.setForeground(style, new Color(30, 187, 21));
		defaultStyle = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
		

		showLanguageSettings(language);

		setResizable(true);
		pack();
		setLocationRelativeTo(frame);
		setModal(true);
		setVisible(true);
	}
	
	public void showLanguageSettings(Language language){
		setTitle("Custom Build/Run - " + language);
		
		try{
			cce = CustomCommandSerializer.getCCE(language.name);
		}catch(NullPointerException e){
			cce = new CustomCommandEntry(language);
		}
		
		tempActiveBuild = cce.isCustomBuildCommand;
		tempActiveRun = cce.isCustomRunCommand;
		
		//Set language specific values
		checkBoxBuild.setSelected(cce.isCustomBuildCommand);
		buildTextPane.setText(cce.customBuildCommand);
		
		chckbxUseNativeConsole.setSelected(cce.useNativeConsole);

		checkBoxExec.setSelected(cce.isCustomRunCommand);
		execTextPane.setText(cce.customRunCommand);
		
		
		setActiveBuild(cce.isCustomBuildCommand);
		setActiveRun(cce.isCustomRunCommand);
		
		
		recognizeText(execDoc);
		recognizeText(buildDoc);
	}
	
	private void setActiveBuild(boolean active){
		buildTextPane.setEnabled(active);
		checkBoxBuild.setSelected(active);
	}
	
	private void setActiveRun(boolean active){
		execTextPane.setEnabled(active);
		checkBoxExec.setSelected(active);
	}
	
	private void recognizeText(StyledDocument doc){
		Runnable doHighlight = new Runnable() {
	        @Override
	        public void run() {
				String text = "";
				try {
					text = doc.getText(0, doc.getLength());
				} catch (BadLocationException e) {
					if(GlobalVariables.debug) e.printStackTrace();
				}
				
				// Search all <name>
				List<Integer> intNames = new ArrayList<Integer>();
				int offset = 0;
				while(true){
					if(offset > text.length() - 6){
						break;
					}
					int intName = text.indexOf("<name>", offset);
					if(intName != -1){
						intNames.add(intName);
						offset = intName + 6;
					}else{
						break;
					}
				}
				
				// search all <path>
				List<Integer> intPaths = new ArrayList<Integer>();
				offset = 0;
				while(true){
					if(offset > text.length() - 6){
						break;
					}
					int intPath = text.indexOf("<path>", offset);
					if(intPath != -1){
						intPaths.add(intPath);
						offset = intPath + 6;
					}else{
						break;
					}
				}
				
				// Clear all color
				doc.setCharacterAttributes(0, doc.getLength(), defaultStyle, true);
				// color name and path
				for(int i = 0; i < intNames.size(); i++){
					doc.setCharacterAttributes(intNames.get(i), 6, style, false);
				}
				for(int i = 0; i < intPaths.size(); i++){
					doc.setCharacterAttributes(intPaths.get(i), 6, style, false);
				}
	        }
	    };       
	    SwingUtilities.invokeLater(doHighlight);
	}
	
}
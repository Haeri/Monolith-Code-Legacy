package Code.Dialog;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Label;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Code.Core.GlobalVariables;
import Code.Core.MonolithFrame;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class SettingsDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public SettingsDialog(MonolithFrame motherFrame) {
		super(motherFrame);
		setTitle("Settings");
		setIconImage(MonolithFrame.iCog.getImage());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));


		// MAIN PANEL
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		GridBagLayout gbl_mainPanel = new GridBagLayout();
		gbl_mainPanel.columnWidths = new int[] { 109, 0 };
		gbl_mainPanel.rowHeights = new int[] { 68, 25, 0 };
		gbl_mainPanel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_mainPanel.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		mainPanel.setLayout(gbl_mainPanel);

			// Font Panel
			JPanel fontPanel = new JPanel();
			GridBagConstraints gbc_fontPanel = new GridBagConstraints();
			gbc_fontPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_fontPanel.anchor = GridBagConstraints.NORTH;
			gbc_fontPanel.insets = new Insets(0, 0, 5, 0);
			gbc_fontPanel.gridx = 0;
			gbc_fontPanel.gridy = 0;
			mainPanel.add(fontPanel, gbc_fontPanel);
			fontPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Font Settings",
					TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagLayout gbl_fontPanel = new GridBagLayout();
			gbl_fontPanel.columnWidths = new int[] { 35, 160, 0 };
			gbl_fontPanel.rowHeights = new int[] { 0, 22, 6, 12, 6 };
			gbl_fontPanel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			gbl_fontPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE, 0.0 };
			fontPanel.setLayout(gbl_fontPanel);
	
				// Font Label
				Label fontLab = new Label("Font");
				GridBagConstraints gbc_fontLab = new GridBagConstraints();
				gbc_fontLab.anchor = GridBagConstraints.NORTHWEST;
				gbc_fontLab.insets = new Insets(0, 0, 5, 5);
				gbc_fontLab.gridx = 0;
				gbc_fontLab.gridy = 0;
				fontPanel.add(fontLab, gbc_fontLab);
				
				// Font Chooser
				String fontS[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
				JComboBox<Object> fontComboBox = new JComboBox<Object>(fontS);
				fontComboBox.setSelectedItem(motherFrame.settings.getFont().getName());
				GridBagConstraints gbc_fontComboBox = new GridBagConstraints();
				gbc_fontComboBox.anchor = GridBagConstraints.WEST;
				gbc_fontComboBox.fill = GridBagConstraints.VERTICAL;
				gbc_fontComboBox.insets = new Insets(0, 0, 5, 0);
				gbc_fontComboBox.gridx = 1;
				gbc_fontComboBox.gridy = 0;
				fontPanel.add(fontComboBox, gbc_fontComboBox);
		
				// Font Size Label
				Label fontSizeLab = new Label("Font Size");
				GridBagConstraints gbc_fontSizeLab = new GridBagConstraints();
				gbc_fontSizeLab.anchor = GridBagConstraints.NORTHWEST;
				gbc_fontSizeLab.insets = new Insets(0, 0, 5, 5);
				gbc_fontSizeLab.gridx = 0;
				gbc_fontSizeLab.gridy = 1;
				fontPanel.add(fontSizeLab, gbc_fontSizeLab);
				
				// Font Size
				SpinnerModel fontSpinnerModel = new SpinnerNumberModel(motherFrame.tField.getFont().getSize(), 6, 60, 1);
				JSpinner fontSizeSpinner = new JSpinner(fontSpinnerModel);
				fontSizeSpinner.setValue(motherFrame.settings.getFontSize());
				GridBagConstraints gbc_fontSizeSpinner = new GridBagConstraints();
				gbc_fontSizeSpinner.insets = new Insets(0, 0, 5, 0);
				gbc_fontSizeSpinner.fill = GridBagConstraints.BOTH;
				gbc_fontSizeSpinner.gridx = 1;
				gbc_fontSizeSpinner.gridy = 1;
				fontPanel.add(fontSizeSpinner, gbc_fontSizeSpinner);
		
				// Tab Size Label
				Label tabSizeLab = new Label("Tab Size");
				GridBagConstraints gbc_tabSizeLab = new GridBagConstraints();
				gbc_tabSizeLab.insets = new Insets(0, 0, 5, 5);
				gbc_tabSizeLab.anchor = GridBagConstraints.NORTHWEST;
				gbc_tabSizeLab.gridx = 0;
				gbc_tabSizeLab.gridy = 2;
				fontPanel.add(tabSizeLab, gbc_tabSizeLab);
				
				// Tab Size Controller
				SpinnerModel tabSpinnerModel = new SpinnerNumberModel(motherFrame.settings.getTabSize(), 1, 10, 1);
				JSpinner tabSizeSpinner = new JSpinner(tabSpinnerModel);
				tabSizeSpinner.setValue(motherFrame.settings.getTabSize());
				GridBagConstraints gbc_tabSizeSpinner = new GridBagConstraints();
				gbc_tabSizeSpinner.insets = new Insets(0, 0, 5, 0);
				gbc_tabSizeSpinner.fill = GridBagConstraints.BOTH;
				gbc_tabSizeSpinner.gridx = 1;
				gbc_tabSizeSpinner.gridy = 2;
				fontPanel.add(tabSizeSpinner, gbc_tabSizeSpinner);
				
				// Line Wrap Label
				Label lineWrapLab = new Label("Line Wrap");
				GridBagConstraints gbc_LineWrapLab = new GridBagConstraints();
				gbc_LineWrapLab.insets = new Insets(0, 0, 5, 5);
				gbc_LineWrapLab.anchor = GridBagConstraints.WEST;
				gbc_LineWrapLab.gridx = 0;
				gbc_LineWrapLab.gridy = 3;
				fontPanel.add(lineWrapLab, gbc_LineWrapLab);
				
				// Line Wrap Chaeckbox
				JCheckBox chckbxLineWrap = new JCheckBox("Wrap lines that are too long");
				chckbxLineWrap.setSelected(motherFrame.settings.isLineWrap());
				GridBagConstraints gbc_chckbxLineWrap = new GridBagConstraints();
				gbc_chckbxLineWrap.insets = new Insets(0, 0, 5, 0);
				gbc_chckbxLineWrap.fill = GridBagConstraints.BOTH;
				gbc_chckbxLineWrap.gridx = 1;
				gbc_chckbxLineWrap.gridy = 3;
				fontPanel.add(chckbxLineWrap, gbc_chckbxLineWrap);
				
				JLabel lblCodeFolding = new JLabel("Code Fold");
				GridBagConstraints gbc_lblCodeFolding = new GridBagConstraints();
				gbc_lblCodeFolding.anchor = GridBagConstraints.WEST;
				gbc_lblCodeFolding.insets = new Insets(0, 0, 0, 5);
				gbc_lblCodeFolding.gridx = 0;
				gbc_lblCodeFolding.gridy = 4;
				fontPanel.add(lblCodeFolding, gbc_lblCodeFolding);
				
				JCheckBox chckbxToggleCodeFolding = new JCheckBox("Toggle code folding");
				chckbxToggleCodeFolding.setSelected(motherFrame.settings.isCodeFolding());
				GridBagConstraints gbc_chckbxToggleCodeFolding = new GridBagConstraints();
				gbc_chckbxToggleCodeFolding.anchor = GridBagConstraints.WEST;
				gbc_chckbxToggleCodeFolding.gridx = 1;
				gbc_chckbxToggleCodeFolding.gridy = 4;
				fontPanel.add(chckbxToggleCodeFolding, gbc_chckbxToggleCodeFolding);
		
		
			
			
			// Style Panel
			JPanel stylePanel = new JPanel();
			GridBagConstraints gbc_stylePanel = new GridBagConstraints();
			gbc_stylePanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_stylePanel.anchor = GridBagConstraints.NORTH;
			gbc_stylePanel.gridx = 0;
			gbc_stylePanel.gridy = 1;
			mainPanel.add(stylePanel, gbc_stylePanel);
			stylePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Theme Settings",
					TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			GridBagLayout gbl_stylePanel = new GridBagLayout();
			gbl_stylePanel.columnWidths = new int[] { 66, 75, 0 };
			gbl_stylePanel.rowHeights = new int[] { 8, 0 };
			gbl_stylePanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
			gbl_stylePanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			stylePanel.setLayout(gbl_stylePanel);
	
				// Theme Label
				JLabel lblTheme = new JLabel("Theme");
				GridBagConstraints gbc_lblTheme = new GridBagConstraints();
				gbc_lblTheme.insets = new Insets(0, 0, 0, 5);
				gbc_lblTheme.anchor = GridBagConstraints.WEST;
				gbc_lblTheme.gridx = 0;
				gbc_lblTheme.gridy = 0;
				stylePanel.add(lblTheme, gbc_lblTheme);
		
				// Theme Chooser
				JComboBox<Object> ThemeBox = new JComboBox<Object>(GlobalVariables.loadedThemes);
				GridBagConstraints gbc_ThemeBox = new GridBagConstraints();
				ThemeBox.setSelectedItem(motherFrame.settings.getTheme());
				gbc_ThemeBox.fill = GridBagConstraints.BOTH;
				gbc_ThemeBox.gridx = 1;
				gbc_ThemeBox.gridy = 0;
				stylePanel.add(ThemeBox, gbc_ThemeBox);


		// BUTTONPANEL
		JPanel buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	
			// Save Settings
			JButton btnSave = new JButton("Save");
			buttonPanel.add(btnSave);
	
			// Restore Defaults
			JButton btnRestoreDefaults = new JButton("Restore Defaults");
			buttonPanel.add(btnRestoreDefaults);

		
		// LISTENER
		
		// Change Font
		fontComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String tempFont = (String) fontComboBox.getSelectedItem();
				motherFrame.setFont(tempFont);
			}
		});

		// Change Font Size
		fontSizeSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				motherFrame.setFontSize((int) fontSizeSpinner.getValue());
			}
		});

		// Change Tab Size
		tabSizeSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				motherFrame.setTabSize((int) tabSizeSpinner.getValue());
			}
		});

		// Change Theme
		ThemeBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				String theme = (String)ThemeBox.getSelectedItem();
				motherFrame.setTheme(theme);
			}
		});
		
		// Change Line Wrap
		chckbxLineWrap.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				motherFrame.setLineWrap(chckbxLineWrap.isSelected());
			}
		});

		// Toggle code folding
		chckbxToggleCodeFolding.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				motherFrame.setCodeFolding(chckbxToggleCodeFolding.isSelected());
			}
		});
		
		// Restore Defaults
		btnRestoreDefaults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				
				motherFrame.resetSettings();
				
				fontComboBox.setSelectedItem(motherFrame.settings.getFont().getName());
				fontSizeSpinner.setValue(motherFrame.settings.getFontSize());
				tabSizeSpinner.setValue(motherFrame.settings.getTabSize());
				ThemeBox.setSelectedItem(motherFrame.settings.getTheme());
				chckbxLineWrap.setSelected(motherFrame.settings.isLineWrap());
				chckbxToggleCodeFolding.setSelected(motherFrame.settings.isCodeFolding());
			}
		});
		
		// Save Config
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				motherFrame.settings.saveConfig();
				SettingsDialog.this.dispose();
			}
		});
		
		

		// Add components to BOX
		setResizable(false);
		pack();
		setLocationRelativeTo(motherFrame);
		setModal(true);
		setVisible(true);
	}
}

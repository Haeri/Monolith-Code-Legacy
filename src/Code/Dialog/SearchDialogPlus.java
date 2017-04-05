package Code.Dialog;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;

import Code.Core.GlobalVariables;
import Code.Core.MonolithFrame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.awt.BorderLayout;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;

public class SearchDialogPlus extends JDialog {
	private static final long serialVersionUID = -6246561910601607455L;
	
	private final ImageIcon iUp = new ImageIcon(SearchDialog.class.getResource(GlobalVariables.RESOURCE_PATH + "/up.png"));
	private final ImageIcon iDown = new ImageIcon(SearchDialog.class.getResource(GlobalVariables.RESOURCE_PATH + "/down.png"));
	private final ImageIcon iExp = new ImageIcon(SearchDialog.class.getResource(GlobalVariables.RESOURCE_PATH + "/exp.png"));
	private final ImageIcon iColl = new ImageIcon(SearchDialog.class.getResource(GlobalVariables.RESOURCE_PATH + "/coll.png"));

	private static boolean isCaseSensitive = true;
	private static boolean isLiveUpdate = true;
	private static boolean isWordSearch = false;
	private static boolean isRegex = false;
	private static boolean isAdvancedOpen = false;
	public static boolean modal = false;

	private MonolithFrame motherFrame;
	private JTextField findField;
	private JTextField replaceField;
	private JButton btnAdvanced;
	private JLabel lblFound;
	private JPanel advancePanel;
	private JPanel contentPanel;
	private BorderLayout borderlayout;
	
	private SearchResult result;
	
	public SearchDialogPlus(MonolithFrame motherFrame){
		super(motherFrame);
		setIconImage(MonolithFrame.iFind.getImage());
		this.motherFrame = motherFrame;
		getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		contentPanel = new JPanel();
		getContentPane().add(contentPanel);
		borderlayout = new BorderLayout(0, 0);
		contentPanel.setLayout(borderlayout);
	
			JPanel mainPanel = new JPanel();
			contentPanel.add(mainPanel, BorderLayout.CENTER);
			mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			GridBagLayout gbl_mainPanel = new GridBagLayout();
			gbl_mainPanel.columnWidths = new int[]{231, 0};
			gbl_mainPanel.rowHeights = new int[]{17, -13, 14, 0};
			gbl_mainPanel.columnWeights = new double[]{0.0, Double.MIN_VALUE};
			gbl_mainPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			mainPanel.setLayout(gbl_mainPanel);
			
			JPanel buttonPanel = new JPanel();
			GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
			gbc_buttonPanel.anchor = GridBagConstraints.NORTH;
			gbc_buttonPanel.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPanel.gridx = 0;
			gbc_buttonPanel.gridy = 2;
			mainPanel.add(buttonPanel, gbc_buttonPanel);
			GridBagLayout gbl_buttonPanel = new GridBagLayout();
			gbl_buttonPanel.columnWidths = new int[]{30, 37, 13, 0};
			gbl_buttonPanel.rowHeights = new int[]{23, 0};
			gbl_buttonPanel.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_buttonPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			buttonPanel.setLayout(gbl_buttonPanel);
			
					btnAdvanced = new JButton(iExp);
					GridBagConstraints gbc_btnAdvanced = new GridBagConstraints();
					gbc_btnAdvanced.insets = new Insets(0, 0, 0, 5);
					gbc_btnAdvanced.anchor = GridBagConstraints.SOUTHWEST;
					gbc_btnAdvanced.gridx = 0;
					gbc_btnAdvanced.gridy = 0;
					buttonPanel.add(btnAdvanced, gbc_btnAdvanced);
					
							// Replace Button
							JButton btnReplace = new JButton("Replace");
							GridBagConstraints gbc_btnReplace = new GridBagConstraints();
							gbc_btnReplace.insets = new Insets(0, 0, 0, 5);
							gbc_btnReplace.anchor = GridBagConstraints.NORTHWEST;
							gbc_btnReplace.gridx = 1;
							gbc_btnReplace.gridy = 0;
							buttonPanel.add(btnReplace, gbc_btnReplace);
							
									// Replace All Button
									JButton btnReplaceAll = new JButton("Replace All");
									GridBagConstraints gbc_btnReplaceAll = new GridBagConstraints();
									gbc_btnReplaceAll.anchor = GridBagConstraints.NORTHWEST;
									gbc_btnReplaceAll.gridx = 2;
									gbc_btnReplaceAll.gridy = 0;
									buttonPanel.add(btnReplaceAll, gbc_btnReplaceAll);
									
											JPanel formPanel = new JPanel();
											GridBagConstraints gbc_formPanel = new GridBagConstraints();
											gbc_formPanel.insets = new Insets(0, 0, 5, 0);
											gbc_formPanel.anchor = GridBagConstraints.NORTHWEST;
											gbc_formPanel.gridx = 0;
											gbc_formPanel.gridy = 1;
											mainPanel.add(formPanel, gbc_formPanel);
											GridBagLayout gbl_formPanel = new GridBagLayout();
											gbl_formPanel.columnWidths = new int[]{46, 181, 0};
											gbl_formPanel.rowHeights = new int[]{20, 20, 0};
											gbl_formPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
											gbl_formPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
											formPanel.setLayout(gbl_formPanel);
											
													// Find Label
													JLabel lblFind = new JLabel("Find:");
													GridBagConstraints gbc_lblFind = new GridBagConstraints();
													gbc_lblFind.fill = GridBagConstraints.HORIZONTAL;
													gbc_lblFind.insets = new Insets(0, 0, 5, 5);
													gbc_lblFind.gridx = 0;
													gbc_lblFind.gridy = 0;
													formPanel.add(lblFind, gbc_lblFind);
													
															JPanel FindFieldPane = new JPanel();
															GridBagConstraints gbc_FindFieldPane = new GridBagConstraints();
															gbc_FindFieldPane.fill = GridBagConstraints.BOTH;
															gbc_FindFieldPane.insets = new Insets(0, 0, 5, 0);
															gbc_FindFieldPane.gridx = 1;
															gbc_FindFieldPane.gridy = 0;
															formPanel.add(FindFieldPane, gbc_FindFieldPane);
															GridBagLayout gbl_FindFieldPane = new GridBagLayout();
															gbl_FindFieldPane.columnWidths = new int[]{56, 30, 30, 0};
															gbl_FindFieldPane.rowHeights = new int[]{11, 0};
															gbl_FindFieldPane.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
															gbl_FindFieldPane.rowWeights = new double[]{0.0, Double.MIN_VALUE};
															FindFieldPane.setLayout(gbl_FindFieldPane);
															
																	// Find Text Field
																	findField = new JTextField(getSelection());
																	GridBagConstraints gbc_findField = new GridBagConstraints();
																	gbc_findField.fill = GridBagConstraints.HORIZONTAL;
																	gbc_findField.gridx = 0;
																	gbc_findField.gridy = 0;
																	FindFieldPane.add(findField, gbc_findField);
																	findField.setColumns(10);
																	
																			// Next Find Button
																			JButton btnUp = new JButton(iUp);
																			btnUp.setPreferredSize(new Dimension(30, 22));
																			btnUp.addActionListener(new ActionListener() {
																				public void actionPerformed(ActionEvent e) {
																				}
																			});
																			GridBagConstraints gbc_btnUp = new GridBagConstraints();
																			gbc_btnUp.anchor = GridBagConstraints.EAST;
																			gbc_btnUp.gridx = 1;
																			gbc_btnUp.gridy = 0;
																			FindFieldPane.add(btnUp, gbc_btnUp);
																			
																					// Previous Find Button
																					JButton btnDown = new JButton(iDown);
																					btnDown.setPreferredSize(new Dimension(30, 22));
																					btnDown.setDefaultCapable(false);
																					GridBagConstraints gbc_btnDown = new GridBagConstraints();
																					gbc_btnDown.anchor = GridBagConstraints.EAST;
																					gbc_btnDown.gridx = 2;
																					gbc_btnDown.gridy = 0;
																					FindFieldPane.add(btnDown, gbc_btnDown);
																					
																							// Replace Label
																							JLabel lblReplace = new JLabel("Replace:");
																							GridBagConstraints gbc_lblReplace = new GridBagConstraints();
																							gbc_lblReplace.fill = GridBagConstraints.HORIZONTAL;
																							gbc_lblReplace.insets = new Insets(0, 0, 0, 5);
																							gbc_lblReplace.gridx = 0;
																							gbc_lblReplace.gridy = 1;
																							formPanel.add(lblReplace, gbc_lblReplace);
																							
																									// Replace Text Field
																									replaceField = new JTextField();
																									GridBagConstraints gbc_replaceField = new GridBagConstraints();
																									gbc_replaceField.anchor = GridBagConstraints.NORTH;
																									gbc_replaceField.fill = GridBagConstraints.HORIZONTAL;
																									gbc_replaceField.gridx = 1;
																									gbc_replaceField.gridy = 1;
																									formPanel.add(replaceField, gbc_replaceField);
																									replaceField.setColumns(10);
																									
																											JPanel findLblPanel = new JPanel();
																											GridBagConstraints gbc_findLblPanel = new GridBagConstraints();
																											gbc_findLblPanel.anchor = GridBagConstraints.NORTH;
																											gbc_findLblPanel.insets = new Insets(0, 0, 5, 0);
																											gbc_findLblPanel.fill = GridBagConstraints.HORIZONTAL;
																											gbc_findLblPanel.gridx = 0;
																											gbc_findLblPanel.gridy = 0;
																											mainPanel.add(findLblPanel, gbc_findLblPanel);
																													GridBagLayout gbl_findLblPanel = new GridBagLayout();
																													gbl_findLblPanel.columnWidths = new int[]{72, 137, 0};
																													gbl_findLblPanel.rowHeights = new int[]{14, 0};
																													gbl_findLblPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
																													gbl_findLblPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
																													findLblPanel.setLayout(gbl_findLblPanel);
																															
																																	// Objects Found Label
																																	final JLabel lblObjectsFound = new JLabel("Objects found:");
																																	GridBagConstraints gbc_lblObjectsFound = new GridBagConstraints();
																																	gbc_lblObjectsFound.anchor = GridBagConstraints.NORTHWEST;
																																	gbc_lblObjectsFound.insets = new Insets(0, 0, 0, 5);
																																	gbc_lblObjectsFound.gridx = 0;
																																	gbc_lblObjectsFound.gridy = 0;
																																	findLblPanel.add(lblObjectsFound, gbc_lblObjectsFound);
																													
																															// Found items Label
																															lblFound = new JLabel("");
																															GridBagConstraints gbc_lblFound = new GridBagConstraints();
																															gbc_lblFound.anchor = GridBagConstraints.WEST;
																															gbc_lblFound.gridx = 1;
																															gbc_lblFound.gridy = 0;
																															findLblPanel.add(lblFound, gbc_lblFound);
	
	advancePanel = new JPanel();
	contentPanel.add(advancePanel, BorderLayout.SOUTH);
	advancePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Advance Settings",
			TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	GridBagLayout gbl_advancePanel = new GridBagLayout();
	gbl_advancePanel.columnWidths = new int[]{87, 85, 0};
	gbl_advancePanel.rowHeights = new int[]{7, 11, 0};
	gbl_advancePanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
	gbl_advancePanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
	advancePanel.setLayout(gbl_advancePanel);
	
			// Full Word search
			JCheckBox chckbxWordSearch = new JCheckBox("Word search");
			chckbxWordSearch.setSelected(isWordSearch);
			GridBagConstraints gbc_chckbxWordSearch = new GridBagConstraints();
			gbc_chckbxWordSearch.anchor = GridBagConstraints.NORTHWEST;
			gbc_chckbxWordSearch.gridx = 0;
			gbc_chckbxWordSearch.gridy = 0;
			advancePanel.add(chckbxWordSearch, gbc_chckbxWordSearch);
			
			// Ignore Case
			JCheckBox chckbxWordCase = new JCheckBox("Ignore Case");
			chckbxWordCase.setSelected(isCaseSensitive);
			GridBagConstraints gbc_chckbxWordCase = new GridBagConstraints();
			gbc_chckbxWordCase.anchor = GridBagConstraints.NORTHWEST;
			gbc_chckbxWordCase.gridx = 1;
			gbc_chckbxWordCase.gridy = 0;
			advancePanel.add(chckbxWordCase, gbc_chckbxWordCase);
			
			// Live Updater
			JCheckBox chckbxLiveUpdate = new JCheckBox("Live Update");
			chckbxLiveUpdate.setSelected(isLiveUpdate);
			GridBagConstraints gbc_chckbxLiveUpdate = new GridBagConstraints();
			gbc_chckbxLiveUpdate.anchor = GridBagConstraints.NORTHWEST;
			gbc_chckbxLiveUpdate.gridx = 0;
			gbc_chckbxLiveUpdate.gridy = 1;
			advancePanel.add(chckbxLiveUpdate, gbc_chckbxLiveUpdate);
			
			// Regex
			JCheckBox chckbxRegex = new JCheckBox("Regex");
			chckbxRegex.setSelected(isRegex);
			GridBagConstraints gbc_chckbxRegex = new GridBagConstraints();
			gbc_chckbxRegex.anchor = GridBagConstraints.NORTHWEST;
			gbc_chckbxRegex.gridx = 1;
			gbc_chckbxRegex.gridy = 1;
			advancePanel.add(chckbxRegex, gbc_chckbxRegex);

		

		// Action Listener
		
		// toggle advanced
		btnAdvanced.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				toggleAdvanced();
			}
		});
		
		// Go to next find
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextFind();
			}
		});
		
		// Go to previous find
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prevFind();
			}
		});
		
		// Replace Button Listener
		btnReplaceAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				replaceAll();
			}
		});

		// Replace Button Listener
		btnReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				replaceAt(0);
			}
		});
		
		
		// Advance
		// Live Search Toggle
		chckbxLiveUpdate.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				isLiveUpdate = chckbxLiveUpdate.isSelected();
				searchPass();
			}
		});

		// Word Search Toggle
		chckbxWordSearch.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				isWordSearch = chckbxWordSearch.isSelected();
				searchPass();
			}
		});

		// Ignore Case Toggle
		chckbxWordCase.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				isCaseSensitive = chckbxWordCase.isSelected();
				searchPass();
			}
		});
		
		// Regex Toggle
		chckbxRegex.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				isRegex = chckbxWordCase.isSelected();
				searchPass();
			}
		});

		// Search Field Listener
		findField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
//				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//					if (!findList.isEmpty())
//						nextFind();
//				}
			}
		});

		// Search Field Listener
		findField.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				if (isLiveUpdate)
					searchPass();
			}
		});
		
		// On exit
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				
				modal = false;
			}
		});
		
		initAdvanced();
		setResizable(false);
		pack();
		setLocationRelativeTo(motherFrame);
		setTitle("Find & Replace");
		setVisible(true);
	}
	
	public void nextFind(){
		searchPass();
		
	}
	
	public void prevFind(){
		
	}
	
	private void replaceAll(){
		
		
	}
	
	private void replaceAt(int pos){
		
		
	}
	
	private void searchPass(){
		 SearchContext context = new SearchContext();
		  String text = findField.getText();
		  if (text.length() == 0) {
		     return;
		  }
		  context.setSearchFor(text);
		  context.setMatchCase(isCaseSensitive);
		  context.setRegularExpression(isRegex);
		  //context.setSearchForward(forward);
		  context.setWholeWord(false);
		
		  result = SearchEngine.find(motherFrame.tField, context);
		  updateLabl();
		 
	}
	
	private void updateLabl(){
//		if(!result.wasFound()){
//			lblFound.setText("No match");
//		}else {			
//			lblFound.setText(result.getMarkedCount() + " of " + result.getCount());
//		}
		//lblFound.setText(result.toString());
		System.out.println(result.toString());
	//	SearchEngine.
	}
	
	private void initAdvanced(){
		if (isAdvancedOpen) {
			contentPanel.add(advancePanel, BorderLayout.SOUTH);
			btnAdvanced.setIcon(iColl);
		} else {
			contentPanel.remove(advancePanel);
			btnAdvanced.setIcon(iExp);
		}
		
		getContentPane().revalidate();
		pack();
	}
	
	private void toggleAdvanced(){
		isAdvancedOpen = !isAdvancedOpen;
		initAdvanced();
	}
	
	private String getSelection() {
		return motherFrame.tField.getSelectedText();
	}
	
	public static void showDialog(MonolithFrame parent) {
		new SearchDialogPlus(parent);
		modal = true;
	}
}

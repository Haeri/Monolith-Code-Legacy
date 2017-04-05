package Code.Components;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import Code.Console.Console;
import Code.Core.GlobalVariables;
import Code.Core.MonolithFrame;


public class Table extends JDialog{
	private static final long serialVersionUID = 1L;
	
	private int row = 3;
	private int col = 4;
	private int size = 0;
	private int tabSize = 0;
	private MonolithFrame motherFrame;
	
	public Table(MonolithFrame motherFrame) {
		super(motherFrame);
		this.motherFrame = motherFrame;
		this.tabSize = motherFrame.settings.getTabSize();
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 85, 20);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblRows = new JLabel("Columns:");
		lblRows.setBounds(0, 3, 46, 14);
		panel.add(lblRows);
		
		SpinnerModel rowSpinnerModel = new SpinnerNumberModel(row, 1, 20, 1);
		JSpinner rowSpinner = new JSpinner(rowSpinnerModel);
		rowSpinner.setBounds(50, 0, 35, 20);
		panel.add(rowSpinner);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(10, 33, 85, 20);
		getContentPane().add(panel_1);
		
		JLabel lblColumns = new JLabel("Rows:");
		lblColumns.setBounds(0, 3, 46, 14);
		panel_1.add(lblColumns);
		
		SpinnerModel colSpinnerModel = new SpinnerNumberModel(col, 1, 20, 1);
		JSpinner colSpinner = new JSpinner(colSpinnerModel);
		colSpinner.setBounds(50, 0, 35, 20);
		panel_1.add(colSpinner);
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBounds(100, 11, 85, 20);
		getContentPane().add(panel_2);
		
		JLabel lblSize = new JLabel("Size:");
		lblSize.setBounds(0, 3, 46, 14);
		panel_2.add(lblSize);
		
		JSpinner sizeSpinner = new JSpinner();
		sizeSpinner.setBounds(50, 0, 35, 20);
		panel_2.add(sizeSpinner);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setBounds(10, 59, 85, 23);
		getContentPane().add(btnCreate);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(100, 59, 85, 23);
		getContentPane().add(btnCancel);
		
		
		// Setup Frame
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(204, 118);
		setResizable(false);
		setLocationRelativeTo(motherFrame);
		setTitle("Insert Table");
		setVisible(true);
		
		// Rows
		rowSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				row = (int) rowSpinner.getValue();
			}
		});
		
		// Columns
		colSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				col = (int) colSpinner.getValue();
			}
		});
		
		// Create
		btnCreate.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent event) {
				create();
				dispose();
			}
		});
		
		// Cancel
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});
		
		// Size
		sizeSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				size = (int) sizeSpinner.getValue();
				System.out.println(row + " " + col + " " +size);
			}
		});
		
		
	}
	
	public void create(){
		int carPos = motherFrame.tField.getCaretPosition();
		int RealTabSize = tabSize*2;
		String table = "";
		String hor = "";
		for (int i = 0; i < col; i++) {
			hor = "";
			String ver = "";
			for (int j = 0; j < row; j++){
				for (int k = 0; k < RealTabSize; k++)
					hor += "-";
				ver += "|\t\t";
			}
			
			table += hor + "-" + "\n" + ver + "|" + "\n";
		}
		table += hor + "-"; 
		
		Document doc = motherFrame.tField.getDocument();
		try {
			doc.insertString(carPos, table, null);
		} catch (BadLocationException e) {
			motherFrame.console.println(e.getMessage(), Console.err);
			if(GlobalVariables.debug) e.printStackTrace();
		}
//		motherFrame.setText(table);
	}
	
	public static void showSettingsDialog(MonolithFrame parent){
		new Table(parent);
	}
}
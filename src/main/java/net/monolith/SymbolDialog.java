package net.monolith;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class SymbolDialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private char[] math = {8704, 8707, 8708, 8710, 8712, 8713, 8715, 8716, 8721, 8723, 8728, 8730, 8734, 8743, 8744, 8745, 8746, 8747, 8776, 8793, 8800, 8801, 8804, 8805, 8834, 8835, 8836, 8837, 8838, 8839};
	private final String[] sTypes = {"Math","Logic", "Arrows"};
	private JPanel pan = new JPanel();
	private MonolithFrame motherFrame;
	public static boolean modal = false;
	
	public final int NORTH = 0;
	public final int EAST = 1;
	public final int SOUTH = 2;
	public final int WEST = 3;
	
	public SymbolDialog(MonolithFrame motherFrame){
		super(motherFrame);
		this.motherFrame = motherFrame;
		
		pan.setPreferredSize(new Dimension(432, 70));
		pan.setLayout(new ColumnsFlowLayout(0,0));
		// Button Build
		for (int i = 0; i < math.length; i++){
			JButton buton = new JButton(math[i]+"");
			buton.addActionListener(this);
			pan.add(buton);
		}  
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5,10,0,10));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.WEST;
		gbc_panel.fill = GridBagConstraints.VERTICAL;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		
		getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblCathegorie = new JLabel("Categories:");
		GridBagConstraints gbc_lblCathegorie = new GridBagConstraints();
		gbc_lblCathegorie.insets = new Insets(0, 0, 0, 5);
		gbc_lblCathegorie.gridx = 0;
		gbc_lblCathegorie.gridy = 0;
		panel.add(lblCathegorie, gbc_lblCathegorie);
		
		JComboBox<Object> comboBox = new JComboBox<Object>(sTypes);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 0;
		panel.add(comboBox, gbc_comboBox);
		
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 1;
        getContentPane().add(pan, gbc_scrollPane);
		
        
		
		// Pack
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(true);
		pack();
//		setLocationRelativeTo(motherFrame);
		position(0);
		setTitle("Symbols");
		setVisible(true);
	
		this.addWindowFocusListener(new WindowFocusListener() {

            public void windowGainedFocus(WindowEvent e) {
            	System.out.println("Focus gain");
//            	setOpacity(1.0f);
            }

            public void windowLostFocus(WindowEvent e) {
            	System.out.println("Focus lost");
//            	setUndecorated(true);
//        		setOpacity(0.5f);
            }

        });
	}
	
	public void actionPerformed(ActionEvent event) {
		motherFrame.insertText(((JButton) event.getSource()).getText());
		motherFrame.tField.requestFocus();
	}
	
	public void position(int position){
		int mW = motherFrame.getWidth();
		int mH = motherFrame.getHeight();
		int mX = motherFrame.getX();
		int mY = motherFrame.getY();
		int tH = this.getHeight();
		int rX, rY;
		
		rX = mX+8;
		rY = (mH - tH) + mY-26;
		this.setLocation(rX, rY);
		this.setSize(mW-16, tH);
	}
	
	public static void showDialog(MonolithFrame parent) {
		new SymbolDialog(parent);
		modal = true;
	}
}
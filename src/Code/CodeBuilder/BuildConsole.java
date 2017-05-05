package Code.CodeBuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import Code.Components.Swing.JFramePlus;
import Code.Console.Console;
import Code.Languages.Language;
import Code.Core.GlobalVariables;
import Code.Core.MonolithFrame;
import Code.Dialog.CustomBuildRunDialog;

public class BuildConsole extends Console{

	private static final long serialVersionUID = 1L;
	public String path;
	public String fullname;
	public Language language;
	public boolean isKill;
	private int width;
	
	CodeBuilder builder;
	JFramePlus frame;
	JButton btStop;
	
	private static final ImageIcon icon = new ImageIcon(MonolithFrame.class.getResource(GlobalVariables.RESOURCE_PATH + "/console.png"));
	
	public BuildConsole(String path, String fullname, Language language, MonolithFrame parent) {
		super(fullname, parent);
		this.path = path;
		this.fullname = fullname;
		this.language = language;
		
		builder = new CodeBuilder(path, fullname, language, this, parent.settings);
		createWindow();
	}
	
	public void createWindow() {
		frame = new JFramePlus(def_text);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		setPosition();
	
		// MenuBar
		JToolBar menuBar = new JToolBar("Build Tools");
		
		// Buttons
		JButton btBuild = new JButton("Build", MonolithFrame.iBuild);
		btBuild.setMargin(new Insets (2, 2, 2, 2));
		JButton btRun = new JButton("Run", MonolithFrame.iRun);
		btRun.setMargin(new Insets (2, 2, 2, 2));
		JButton btBuildRun = new JButton("Build & Run", MonolithFrame.iBuildRun);
		btBuildRun.setMargin(new Insets (2, 2, 2, 2));
		btStop = new JButton("Stop", MonolithFrame.iStop);
		btStop.setEnabled(false);
		btStop.setMargin(new Insets (2, 2, 2, 2));
		JButton btEdit = new JButton(MonolithFrame.iBuildConfig);
		btEdit.setMargin(new Insets (2, 2, 2, 2));
		
		menuBar.add(btBuild);
		menuBar.add(btRun);
		menuBar.add(btBuildRun);
		menuBar.add(btStop);
		menuBar.add(Box.createGlue());
		menuBar.add(btEdit);
		
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.add(menuBar, BorderLayout.NORTH);
		
		frame.setIconImage(icon.getImage());

		// Close listener
		frame.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
            	stop();
            	isKill = true;
            	frame.dispose();
            	frame.exit();
            }
        });
		
		// Build
		btBuild.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				build();
			}
		});
		
		// Run
		btRun.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				run();
			}
		});
		
		// Build & Run
		btBuildRun.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				buildRun();
			}
		});
		
		// Stop
		btStop.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				stop();
			}
		});
		
		// Edit
		btEdit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				new CustomBuildRunDialog(frame, language, fullname, path);
			}
        });
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public void calculateWidth(String line){
		this.width = outPane.getFontMetrics(outPane.getFont()).stringWidth(line) + 6 * hGap;
	}
	
	// Set location and scale
	private void setPosition() {
		Dimension dim = super.parent.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point point = super.parent.getLocation();
		
		// left space
		double left = point.getX();
		
		// right space
		double right = screenSize.getWidth() - (left + dim.getWidth());
		
		double side = Math.max(left, right);
		double minWidth = Math.min(side, width);
		
		// no space
		if(minWidth < 350){
			frame.setPreferredSize(new Dimension((int)dim.getWidth(), (int)dim.getHeight() / 3));
			frame.setLocation((int) point.getX(), (int) point.getY()+ 2 * (int)(dim.getHeight() / 3));		
			return;
		}
		
		// set width
		frame.setPreferredSize(new Dimension((int)minWidth, (int)dim.getHeight()));
		
		// decide best side
		if(right > left){
			frame.setLocation((int) (point.getX() + dim.getWidth())-14, (int) point.getY());
		}else{
			frame.setLocation((int) (point.getX() - minWidth)+14, (int) point.getY());	
		}
	}

	public void build(){
		renewBuilder();
		builder.begin(CodeBuilder.BUILD);
	}
	
	public void run(){
		renewBuilder();
		builder.begin(CodeBuilder.RUN);
	}
	
	public void buildRun(){
		renewBuilder();
		builder.begin(CodeBuilder.BUILD_N_RUN);
	}
	
	public void stop(){
		 if (builder != null){
			 builder.kill();
			 btStop.setEnabled(false);
		 }
	}
	
	public void read(String command){
        // Write to Process
        if (builder.getOutStream() != null) {
        	registerLog(command);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(builder.getOutStream()));
            try {
                writer.write(command + "\n");  // add newline so your input will get proceed
                writer.flush();  // flush your input to your process
            } catch (IOException e) {
            	parent.console.println(e.getMessage(), Console.err);
				if(GlobalVariables.debug) e.printStackTrace();
            }
        }
        // Execute Command
        else {        	
        	println("No running process");
        	super.read(command);
        }
    }
	
	private void renewBuilder(){
		stop();
		CodeBuilder newbuilder = new CodeBuilder(path, fullname, language, this, parent.settings);
		builder = newbuilder;
		btStop.setEnabled(true);
	}

}
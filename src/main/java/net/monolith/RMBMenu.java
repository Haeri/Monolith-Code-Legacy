package net.monolith;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class RMBMenu extends JPopupMenu{
	private static final long serialVersionUID = 1L;

	JMenuItem undo, redo;
	private MonolithFrame motherFrame;
	
	public RMBMenu(MonolithFrame motherFrame){
		super();

		this.motherFrame = motherFrame;
		
		//Undo
		undo = new JMenuItem("Undo");
		undo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				motherFrame.undo();
			}
		});
		//Redo      
		redo = new JMenuItem("Redo");
		redo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				motherFrame.redo();
			}
		});
		//Cut
		JMenuItem item1 = new JMenuItem("Cut");
		item1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				motherFrame.tField.cut();
			}
		});
		//Copy      
		JMenuItem item2 = new JMenuItem("Copy");
		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				motherFrame.tField.copy();
			}
		});
		//Paste
		JMenuItem item3 = new JMenuItem("Paste");
		item3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				motherFrame.tField.paste();
			}
		});
		//Quick Run
		JMenuItem item4 = new JMenuItem("Build & Run");
		item4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				motherFrame.stat.loadStart();
				motherFrame.buildNew = false;
				motherFrame.buildAndRun(CodeBuilder.BuildMode.BUILD_N_RUN);
				motherFrame.stat.loadEnd();;
			}
		});
		//Quick Math
		JMenuItem item5 = new JMenuItem("Quick Math");
		item5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				motherFrame.quickMath();
			}
		});
		
		undo.setIcon(MonolithFrame.iUndo);
		redo.setIcon(MonolithFrame.iRedo);
		item1.setIcon(MonolithFrame.iCut);
		item2.setIcon(MonolithFrame.iCopy);
		item3.setIcon(MonolithFrame.iPaste);
		item4.setIcon(MonolithFrame.iBuildRun);
		item5.setIcon(MonolithFrame.iMath);
		
		this.add(undo);
		this.add(redo);
		this.addSeparator();
		this.add(item1);
		this.add(item2);
		this.add(item3);
		this.addSeparator();
		this.add(item4);
		this.add(item5);
	}
	
	public void show(Component invoker, int x, int y){
		undo.setEnabled(motherFrame.tField.canUndo());
		redo.setEnabled(motherFrame.tField.canRedo());		
		super.show(invoker, x, y);
	}
}
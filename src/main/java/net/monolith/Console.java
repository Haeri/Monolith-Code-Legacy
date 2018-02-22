package main.java.net.monolith;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class Console extends JPanel{
    
	class PrintEntry{
    	String print;
    	AttributeSet attrib;
    	
    	PrintEntry(String print, AttributeSet attrib){
    		this.print = print;
    		this.attrib = attrib;
    	}
    }
    
	private List<PrintEntry> printQueue = new ArrayList<PrintEntry>();
	private static final long serialVersionUID = 1L;

	public final String def_text;
	private int last = 0;
	private int repeat = 2;
	private Font font = new Font("Consolas", Font.PLAIN, 12);
	public JTextPane outPane, inPane;
	private JScrollPane conScroll;
	private Document doc;
	public MonolithFrame parent;
	private String lastLog = "";
	private boolean first = true;
	private List<String> history;
	private int currentLog = 0;
	private JScrollBar vertical;
	protected int hGap = 8;
	private int vGap = 4;
	
	private final static StyleContext cont = StyleContext.getDefaultStyleContext();
	public static AttributeSet userIn = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(49, 117, 175));
	public static AttributeSet stdOut = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(180, 180, 180));
	public static AttributeSet out = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(255, 255, 255));
	public static AttributeSet err = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(224, 47, 41));
	public static AttributeSet errMute = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(170, 170, 170));
	public static AttributeSet warn = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(240, 173, 78));
	
	final String mathHelp = "Legal Math Functions:\nNOT(expression)\tBoolean negation, 1 (means true) if the expression is not zero\nIF(condition,value_if_true,value_if_false)\tReturns one value if the condition evaluates to true or the other if it evaluates to false\nRANDOM()\t\tProduces a random number between 0 and 1\nMIN(e1,e2)\t\tReturns the smaller of both expressions\nMAX(e1,e2)\t\tReturns the bigger of both expressions\nABS(expression)\tReturns the absolute (non-negative) value of the expression\nROUND(expression,precision)\tRounds a value to a certain number of digits, uses the current rounding mode\nFLOOR(expression)\tRounds the value down to the nearest integer\nCEILING(expression)\tRounds the value up to the nearest integer\nLOG(expression)\tReturns the natural logarithm (base e) of an expression\nLOG10(expression)\tReturns the common logarithm (base 10) of an expression\nSQRT(expression)\tReturns the square root of an expression\nSIN(expression)\tReturns the trigonometric sine of an angle (in degrees)\nCOS(expression)\tReturns the trigonometric cosine of an angle (in degrees)\nTAN(expression)\tReturns the trigonometric tangens of an angle (in degrees)\nASIN(expression)\tReturns the angle of asin (in degrees)\nACOS(expression)\tReturns the angle of acos (in degrees)\nATAN(expression)\tReturns the angle of atan (in degrees)\nSINH(expression)\tReturns the hyperbolic sine of a value\nCOSH(expression)\tReturns the hyperbolic cosine of a value\nTANH(expression)\tReturns the hyperbolic tangens of a value\nRAD(expression)\tConverts an angle measured in degrees to an approximately equivalent angle measured in radians\nDEG(expression)\tConverts an angle measured in radians to an approximately equivalent angle measured in degrees";
	
	public Console(String title, MonolithFrame parent){
		super();
		this.parent = parent;
		def_text = title + "";
		history = new ArrayList<String>();
		history.add("");
		
		setLayout(new BorderLayout(0, 0));
		
		outPane = new JTextPane();
		outPane.setBorder(new EmptyBorder(vGap, hGap, vGap, hGap));
		outPane.setBackground(AppTheme.CONSOLE_OUT_BG);
		outPane.setForeground(AppTheme.CONSOLE_OUT_FG);
		outPane.setFont(font);
		outPane.setEditable(false);
		outPane.setEditorKit(new WrapEditorKit());
		
		doc = outPane.getDocument();
		
		inPane = new JTextPane();
		inPane.setBorder(new EmptyBorder(vGap, hGap, vGap, hGap));
		//FontMetrics fontMetrics = inPane.getFontMetrics( inPane.getFont() );
		inPane.setBackground(AppTheme.CONSOLE_IN_BG);
		inPane.setForeground(AppTheme.CONSOLE_IN_FG);
		inPane.setCaretColor(AppTheme.CONSOLE_IN_FG);
		inPane.setFont(font);

		JPanel pane = new JPanel(new BorderLayout());
		pane.add(outPane, BorderLayout.NORTH);
		pane.add(inPane, BorderLayout.CENTER);
		
		conScroll = new JScrollPane(pane);
		conScroll.getVerticalScrollBar().setUnitIncrement(16);
		conScroll.setBorder(BorderFactory.createEmptyBorder());
		conScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		add(conScroll, BorderLayout.CENTER);

			
		ScrollBarPlus hBarC = new ScrollBarPlus(outPane.getBackground());
		ScrollBarPlus vBarC = new ScrollBarPlus(outPane.getBackground());
		conScroll.getHorizontalScrollBar().setUI(hBarC);
		conScroll.getVerticalScrollBar().setUI(vBarC);
		conScroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, AppTheme.SCROLL_BAR_WIDTH));
		conScroll.getVerticalScrollBar().setPreferredSize(new Dimension(AppTheme.SCROLL_BAR_WIDTH, 0));
		
		JPanel right = new JPanel();
		right.setBackground(outPane.getBackground());
		conScroll.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, right);
		
		inPane.addKeyListener(new KeyListener(){
		    @Override
		    public void keyPressed(KeyEvent e){
			    if(e.getKeyCode() == KeyEvent.VK_ENTER && e.isShiftDown()){
			    	inPane.setText(inPane.getText() + "\n");
			    }else if(e.getKeyCode() == KeyEvent.VK_ENTER){
			    	e.consume();
			    	read(inPane.getText());
			    }else if(e.getKeyCode() == KeyEvent.VK_UP){
			    	getHistory();
			    }
		    }
		    @Override
		    public void keyTyped(KeyEvent e) {}

		    @Override
		    public void keyReleased(KeyEvent e) {}
		});
		
		println(def_text, Console.stdOut);
	}

	public void clear(){
		repeat = 2;
		lastLog = "";
		first = true;
		outPane.setText("");
		println(def_text, Console.stdOut);
	}

	public void queueError(String print){
		printQueue.add(new PrintEntry(print, Console.err));
	}
	
	public void printQueue(){
		for(int i = 0; i < printQueue.size(); ++i){
			println(printQueue.get(i).print, printQueue.get(i).attrib);
		}
	}
	
	public void println(String print[]){
		println(Arrays.toString(print));
	}
	
	public void println(String print){
		println(print, stdOut);
	}
	
	public void println(String print[], AttributeSet attr){
		println(Arrays.toString(print), attr);
	}
	
	public void println(String print, AttributeSet attr){
		
		// Style print
		if(attr == Console.err){
			if(!this.getClass().equals(BuildConsole.class))
				parent.toggleConsole(true);
			print = "ERROR: " + print; 
			parent.stat.error();
		}else if(attr == Console.errMute){ 
			parent.stat.error();
		}else if(attr == Console.warn){
			print = "WARNING: " + print;
			parent.stat.warning();
		}
		
		Util.println(print);
		
		String newLog = "";
		if (print.equals(lastLog)){
			try {
				doc.remove(last, doc.getLength()-last);
			} catch (BadLocationException e) {
				if(GlobalVariables.debug) e.printStackTrace();
			}
			newLog = "[" + repeat++ + "] " + print;
		}else{
			last = doc.getLength();
			newLog = print;
			repeat = 2;
		}
		lastLog = print;
	
		try {
			if(first){
				doc.insertString(last, newLog, attr);
				first = false;	
			}else
				doc.insertString(last, "\n" + newLog, attr);
		} catch (BadLocationException e) {
			if(GlobalVariables.debug) e.printStackTrace();
		}
		
		// Scroll Down
		down();
	}
		
	public void registerLog(String in){
		inPane.setText("");
		println(in, userIn);
		history.add(in);
		currentLog = history.size()-1;
	}
	
	public void read(String in){
		registerLog(in);
		
		in = in.toLowerCase();
		if (in.equals("clear")){
			clear();
		}else if(in.equals("hello") || in.equals("hy")){
			println(">Hy there!");
		}else if(in.equals("debug")){
			println(">Toggle Debug Menu");
			parent.toggleDebug();
		}else if(in.equals("math_help")){
			println(mathHelp);
		}else if(in.matches("math_round = (.*)")){
			Pattern p = Pattern.compile("math_round = (.*)");
			Matcher m = p.matcher(in);
			m.find();
			String src = m.group(1);
			
			//Read value
			int value;
			try{				
				value = Integer.parseInt(src);
			}catch(Exception e){
				println("Invalid input!", Console.err);
				if(GlobalVariables.debug) e.printStackTrace();
				return;
			}
			
			//Check if value legal
			if(value >= 0 && value <= 128){
				GlobalVariables.mathRound = value;				
				println("math_round set to " + src);
			}else{
				println("Invalid value!", Console.err);
			}
		}else if(in.matches("max_days = (.*)")){
			Pattern p = Pattern.compile("max_days = (.*)");
			Matcher m = p.matcher(in);
			m.find();
			String src = m.group(1);
			
			//Read value
			int value;
			try{				
				value = Integer.parseInt(src);
			}catch(Exception e){
				println("Invalid input!", Console.err);
				if(GlobalVariables.debug) e.printStackTrace();
				return;
			}
			
			//Check if value legal
			if(value >= 0 && value <= 128){
				parent.settings.setSetting(Settings.BACKUP_MAX_SAVE_DAYS, value);				
				println("Maximum backup days set to " + src);
			}else{
				println("Invalid value!", Console.err);
			}
		}
	}	
	public void down(){
		vertical = conScroll.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum());
	}
	
	private void getHistory(){
		if(currentLog == -1)
			currentLog = history.size()-1;
		inPane.setText(history.get(currentLog--));
	}
	
	
	
	
	
	
	  class WrapEditorKit extends StyledEditorKit {
		private static final long serialVersionUID = 1L;
			ViewFactory defaultFactory=new WrapColumnFactory();
	        public ViewFactory getViewFactory() {
	            return defaultFactory;
	        }

	    }

	    class WrapColumnFactory implements ViewFactory {
	        public View create(Element elem) {
	            String kind = elem.getName();
	            if (kind != null) {
	                if (kind.equals(AbstractDocument.ContentElementName)) {
	                    return new WrapLabelView(elem);
	                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
	                    return new ParagraphView(elem);
	                } else if (kind.equals(AbstractDocument.SectionElementName)) {
	                    return new BoxView(elem, View.Y_AXIS);
	                } else if (kind.equals(StyleConstants.ComponentElementName)) {
	                    return new ComponentView(elem);
	                } else if (kind.equals(StyleConstants.IconElementName)) {
	                    return new IconView(elem);
	                }
	            }

	            // default to text display
	            return new LabelView(elem);
	        }
	    }

	    class WrapLabelView extends LabelView {
	        public WrapLabelView(Element elem) {
	            super(elem);
	        }

	        public float getMinimumSpan(int axis) {
	            switch (axis) {
	                case View.X_AXIS:
	                    return 0;
	                case View.Y_AXIS:
	                    return super.getMinimumSpan(axis);
	                default:
	                    throw new IllegalArgumentException("Invalid axis: " + axis);
	            }
	        }

	    }
}
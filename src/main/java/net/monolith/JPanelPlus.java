package net.monolith;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class JPanelPlus extends JPanel{
	private static final long serialVersionUID = 1L;
	
	Color bgPrime = new Color(41, 45, 48);
    Color bgSecond = new Color(15, 20, 27);

    public JPanelPlus(LayoutManager layout){
    	super(layout);
    }
    
    public JPanelPlus(){}
    
    public void setPanelColor(Color prime, Color second){
        bgPrime = prime;
        bgSecond = second;
        repaint();
    }
    
    public void setPanelColor(Color prime){
        bgPrime = prime;
        bgSecond = prime;
        repaint();
    }

    public Color getPanelColor(){
    	return bgPrime;
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gp = new GradientPaint(0, 0, bgPrime, 0, getHeight()*2, bgSecond);
        setBackground(new Color(60, 60, 60));
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());

    }
}
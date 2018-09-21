package net.monolith;

import java.awt.Color;

public class ColorPlus extends Color{

	public int LINEAR = 1;
	public int EXPONENTIAL = 2;
	private static final long serialVersionUID = 1L;

	public ColorPlus(int r, int g, int b) {
		super(r, g, b);
	}
	
	// subtracts <value> from the RGB channels
	public ColorPlus darkenRGB(int value) {
		int r = this.getRed();
		int g = this.getGreen();
		int b = this.getBlue();

		r = (Math.max(r - value, 0));
		g = (Math.max(g - value, 0));
		b = (Math.max(b - value, 0));
		return new ColorPlus(r,g,b);
	}

	// Adds <value> to the RGB channels
	public ColorPlus lightenRGB(int value) {
		int r = this.getRed();
		int g = this.getGreen();
		int b = this.getBlue();
		
		r = (Math.min(r + value, 255));
		g = (Math.min(g + value, 255));
		b = (Math.min(b + value, 255));
		return new ColorPlus(r,g,b);
	}

	// Darken Color by <value>
	public ColorPlus darken(int value) {
		//TODO Change Luminanz value of Color by <value>
		float hsbVals[] = Color.RGBtoHSB( 
				this.getRed(),
				this.getGreen(),
	            this.getBlue(), null );
		Color temp = Color.getHSBColor( hsbVals[0], hsbVals[1], value * hsbVals[2]);
		return ColorPlus.toStyle(temp);
	}

	// Brightens Color by <value>
	public ColorPlus lighten(int value) {
		//TODO Change Luminanz value of Color by <value>
		float hsbVals[] = Color.RGBtoHSB( 
				this.getRed(),
				this.getGreen(),
	            this.getBlue(), null );
		Color temp = Color.getHSBColor( hsbVals[0], hsbVals[1], value * ( 1f + hsbVals[2] ));
		return ColorPlus.toStyle(temp);
	}
	
	// Generates a Color which is either darker or brighter
	public ColorPlus complement(int value) {
		ColorPlus dark = darkenRGB(value);
		ColorPlus light = lightenRGB(value);
		
		int r = dark.getRed();
		int g = dark.getGreen();
		int b = dark.getBlue();
		
		if (r == 0 || g == 0 || b == 0) {
			r = light.getRed();
			g = light.getGreen();
			b = light.getBlue();
		}
		return new ColorPlus(r,g,b);
	}
	
	// Inverts Color
	public ColorPlus invert() {
		int r = this.getRed();
		int g = this.getGreen();
		int b = this.getBlue();
		
		r = (255 - r);
		g = (255 - g);
		b = (255 - b);
		return new ColorPlus(r,g,b); 
	}
	
	// Generates a B|W Color
	public ColorPlus binary() {
		int r = this.getRed();
		int g = this.getGreen();
		int b = this.getBlue();
		
		int cSum = r + g + b;
		if (cSum >= 382) {
			r = 0;
			g = 0;
			b = 0;
		}else{
			r = 255;
			g = 255;
			b = 255;
		}
		return new ColorPlus(r,g,b); 
	}
	
	public static Color mixColors(Color color2, Color color1, int percent){
		return mixColors(color2, color1, percent, 1);
	}
	
	public static Color mixColors(Color color2, Color color1, int percent, int methode){
		double perc;
		if (percent > 100){
			percent = 100;
			System.out.println("WARNING: Value is greater 1!");
		}
		
		if (methode == 1){
			//Linear
			perc = percent/100.0f; 
		}else if(methode == 2){
			//Exponential
			perc = Math.pow(Math.E, ((percent-35.0f)*0.070849f));	
			perc = perc/100.0f;
		}else{
			perc = percent/100.0f;	
		}
		
		double inverse_percent = 1.0 - perc;
		double redPart = (color1.getRed()*perc + color2.getRed()*inverse_percent);
		double greenPart = (color1.getGreen()*perc + color2.getGreen()*inverse_percent);
		double bluePart = (color1.getBlue()*perc + color2.getBlue()*inverse_percent);
		return new Color((int)redPart, (int)greenPart, (int)bluePart);
	}
	
	// Converts Color To ColorPlus
	public static ColorPlus toStyle(Color color){
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		
		ColorPlus out = new ColorPlus(r,g,b);
		return out;
	}
}
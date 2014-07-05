package plm.core.utils;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ColorMapper {
	static String[] choices = {
		"white","black","blue","cyan","darkGray","gray","green","lightGray","magenta","orange","pink","red","yellow"};
	static Color[] colors = {
		Color.white,Color.black,Color.blue,Color.cyan,Color.darkGray,Color.gray,Color.green,Color.lightGray,Color.magenta,Color.orange,Color.pink,Color.red,Color.yellow};
	static Pattern colorName = Pattern.compile("(\\d+)/(\\d+)/(\\d+)");

	public static Color name2color(String name) throws InvalidColorNameException {
		for (int i=0; i<choices.length; i++) 
			if (choices[i].equalsIgnoreCase(name))
				return colors[i];
		Matcher m = colorName.matcher(name);
		if (m.matches()) {
			try {
				int r=Integer.parseInt( m.group(1) );
				int g=Integer.parseInt( m.group(2) );
				int b=Integer.parseInt( m.group(3) );
				
				if (r<0 || r>255) 
					throw new InvalidColorNameException("Name "+name+" is not a valid color name: Red value is not between 0 and 255");
				if (g<0 || g>255) 
					throw new InvalidColorNameException("Name "+name+" is not a valid color name: Green value is not between 0 and 255");
				if (b<0 || b>255) 
					throw new InvalidColorNameException("Name "+name+" is not a valid color name: Blue value is not between 0 and 255");
					
				return new Color(r,g,b);
			} catch (NumberFormatException nfe) {
				throw new InvalidColorNameException("Name "+name+" is not a valid color name since one of its component is not a number",nfe);
			}
		} else {
			throw new InvalidColorNameException("Name "+name+" is not a valid color name");
		}
	}
	
	public static String color2name(Color c) {
		for (int i=0; i<choices.length; i++) 
			if (colors[i].equals(c))
				return choices[i];
		return c.getRed()+"/"+c.getGreen()+"/"+c.getBlue();
	}
	
	public static int color2int(Color c){
		for (int i=0; i<choices.length; i++) 
			if (colors[i].equals(c))
				return i;
		return -1;
	}
	
	public static Color int2color(int c) throws InvalidColorNameException{
		if(c>=0 && c<colors.length){
			return colors[c];
		}else{
			//TODO GIANNINI change message and translate this
			throw new InvalidColorNameException("Color Error");
		}
	}
}

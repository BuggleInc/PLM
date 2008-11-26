package universe.turtles;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import jlm.ui.WorldView;
import jlm.universe.EntityControlPanel;
import jlm.universe.World;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;


public class TurtleWorld extends World {

	@ElementList ArrayList<ShapeAbstract> shapes = new ArrayList<ShapeAbstract>(); 

	@Attribute
	private double width;
	@Attribute
	private double height;
	
	public TurtleWorld(String name) {
		super(name);
	}

	public TurtleWorld(String name, int width, int height) {
		super(name);
		this.width = width;
		this.height = height;
	}
	public TurtleWorld(TurtleWorld world2) {
		super(world2);
		this.height = world2.height;
		this.width = world2.width;
		for (ShapeAbstract s:world2.shapes)
			shapes.add(s.copy());
	}

	@Override
	public World copy() {
		return new TurtleWorld(this);
	}
	@Override
	public void reset(World w) {
		TurtleWorld initialWorld = (TurtleWorld)w;
		shapes.clear();
		Iterator<ShapeAbstract> it = initialWorld.shapes();
		while (it.hasNext()) 
			shapes.add(it.next().copy());
		
		super.reset(w);		
	}
	@Override
	public String getAbout(){
		return  "Voici les méthodes comprises par les tortues:<br>" +
				"<ul>"+
				" <li><b>Avancer</b> <code>public void forward(double nbPas);</code><br>"+
				"   <b>Reculer</b> <code>public void backward(double nbPas);</code></li>"+
				" <li><b>Tourner à gauche</b> <code>public void turnLeft(double angle);</code> (en degrés)<br>"+
				"   <b>Tourner à droite</b> <code>public void turnRight(double angle);</code></li>"+
				" <li><b>Lever stylo</b> <code>public void penUp();</code><br>"+
				"   <b>Baisser stylo</b> <code>public void penDown();</code><br>"+
				"   <b>Obtenir position stylo</b> <code>public boolean isPenDown();</code><br>"+
				"   (les tortues ont des stylos, pas des brosses comme les buggles)</li>"+
				" <li><b>Obtenir direction</b> <code>public double getHeading();</code><br>"+
				"   <b>Changer direction</b> <code>public void setHeading(double angle);</code></li>"+
				" <li><b>Obtenir couleur</b> <code>public Color getColor();</code><br>"+
				"   <b>Changer couleur</b> <code>public void setColor(Color color);</code></li>"+
				" <li><b>Obtenir position</b> <code>public double getX();</code>"+ 
                "                             <code>public double getY();</code><br>"+
                "     <b>Changer position</b> <code>public void setX(double x);</code>"+ 	
                "                             <code>public void setY(double y);</code>"+
                "                             <code>public void setPos(double x, double y);</code></li>"+
                "</ul>";
	}
	
	public void addLine(double x, double y, double newX, double newY, Color color) {
		synchronized (shapes) {
			ShapeLine line =new ShapeLine(width/2+x,height/2+y,width/2+newX,height/2+newY,color); 
			shapes.add(line);			
		}
	}
	public Iterator<ShapeAbstract> shapes() {
		return shapes.iterator();
	}
	

	public double getHeight() {
		return height;
	}
	public double getWidth() {
		return width;
	}
	
	@Override
	public EntityControlPanel getEntityControlPanel() {
		return new TurtleButtonPanel();
	}

	@Override
	public WorldView getView() {
		return new TurtleWorldView(this);
	}
	

	// TODO implement world IO
	@Override
	public void readFromFile(BufferedReader br) throws IOException {}
	@Override
	public void writeToFile(BufferedWriter f) throws IOException {}
	
	
	@Override
	public String toString(){
		String res = "TurtleWorld: name="+getName()+", size="+width+"x"+height+", shapes=[";
		Iterator<ShapeAbstract> it = shapes();
		while (it.hasNext()) 
			res += it.next().toString();
		res += "]";
		return res;
	}
}

package universe.bugglequest;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

import jlm.universe.EntityControlPanel;
import jlm.universe.World;

import universe.bugglequest.exception.AlreadyHaveBaggleException;
import universe.bugglequest.ui.BuggleButtonPanel;
import universe.bugglequest.ui.BuggleWorldView;


public class BuggleWorld extends jlm.universe.World {
 
	private BuggleWorldCell[][] world;

	private int sizeX;
	private int sizeY;

	
	public BuggleWorld(String name, int x, int y) {
		super(name);
		create(x, y);
	}

	public void create(int width, int height) {
		this.sizeX = width;
		this.sizeY = height;
		this.world = new BuggleWorldCell[sizeX][sizeY];
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++)
				world[i][j] = new BuggleWorldCell(this,i,j);		
	}

	/** 
		 * Create a new world being almost a copy of the first one. Beware, all the buggles of the copy are changed to BuggleRaw. 
	 * @param world2
	 */
	public BuggleWorld(BuggleWorld world2) {
		super(world2);
		setName(world2.getName());
		sizeX = world2.getWidth();
		sizeY = world2.getHeight();
		this.world = new BuggleWorldCell[sizeX][sizeY];
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++) {
				world[i][j] = new BuggleWorldCell(world2.getCell(i, j));
				world[i][j].setWorld(this);
			}
	}
	
	public World copy(){
		return new BuggleWorld(this);
	}
	/**
	 * Reset the content of a world to be the same than the one passed as argument
	 * does not affect the name of the initial world.
	 * @param initialWorld
	 */
	@Override
	public void reset(World iw) {
		BuggleWorld initialWorld = (BuggleWorld)iw;
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++) {
				BuggleWorldCell c = initialWorld.getCell(i, j);
				world[i][j] = new BuggleWorldCell(c);
			}

		
		super.reset(initialWorld);
	}	
	@Override
	public String getAbout() {
		return "<h1>Méthodes comprises par les buggles</h1>" +
				"<table border=1>" +
				"<tr><td colspan=2 align=center><b>Bouger</b></td></tr>" +
				"  <tr><td><b>Tourner à gauche<br>Tourner à droite<br>Se retourner<br>Avancer<br>Reculer</td>" +
				"      <td>void turnLeft() (note 1);<br>void turnRight() (note 1)<br>void turnBack() (note 1)<br>" +
				"          void forward() ou void forward(int) (note 1)<br>void backward() ou void backward(int) (note 1)<br></td></tr>" +
				"  <tr><td><b>Obtenir l'abcisse<br>Obtenir l'ordonnée<br>Changer l'abcisse<br>Changer l'ordonnée<br>Changer la position</td>" +
				"      <td>int getX();<br>int getY()<br>void setX(int)<br>void setY(int)<br>void setPos(int,int)</td></tr>" +
				
				"<tr><td colspan=2 align=center><b>Informations sur la buggle</b></td></tr>" +
				"  <tr><td><b>Obtenir la couleur<br>Changer la couleur</td>" +
				"      <td>Color getColor();<br>void setColor(Color)</td></tr>" +				
				"  <tr><td><b>Chercher un mur devant<br>Chercher un mur derriere</td>" +
				"      <td>boolean isFacingWall();<br>boolean isBackingWall()</td></tr>" +				
				"  <tr><td><b>Obtenir la direction<br>Changer la direction</td>" +
				"      <td>Direction getDirection();<br>void setDirection(Direction)</td></tr>" +
				
				"<tr><td colspan=2 align=center><b>À propos de la brosse</b></td></tr>" +
				"  <tr><td><b>Baisser la brosse<br>Lever la brosse<br>Obtenir la position de la brosse" +
				"      <td>void brushUp();<br>void brushDown();<br>boolean isBrushDown();</td></tr>"+
				"  <tr><td><b>Modifier la couleur de la brosse<br>Obtenir la couleur de la brosse" +
				"      <td>void setBrushColor(Color);<br>Color getBrushColor();</td></tr>"+
				
				"<tr><td colspan=2 align=center><b>Interagir avec le monde</b></td></tr>" +
				"  <tr><td><b>Obtenir la couleur du sol</b></td><td>Color getGroundColor();</td></tr>" +
				
				"  <tr><td><b>Chercher un baggle par terre<br>Chercher un baggle dans ses poches<br>Prendre un baggle<br>Poser un baggle" +
				"      <td>boolean isOverBaggle();<br>boolean isCarryingBaggle();<br>void pickupBaggle() (note 2)<br>void dropBaggle() (note 2)</td></tr>"+
				
				"  <tr><td><b>Chercher un message<br>Ajouter un message<br>Lire le message<br>Effacer le message" +
				"      <td>boolean isOverMessage();<br>void writeMessage(String);<br>String readMessage()<br>void clearMessage()</td></tr>"+
				"</table>"+
				
				"<h2>Note 1</h2>" +
				"Les buggles normales lèvent une exception BuggleWallException si on cherche à leur faire traverser un mur. " +
				"Les SimpleBuggles (ie, celles utilisées dans les premiers exercices) affiche un message d'erreur sans que vous " +
				"ayez à vous soucier de ce qu'est une exception"+
				
				"<h2>Note 2</h2>" +
				"Les buggles normales lèvent une exception NoBaggleUnderBuggleException si vous cherchez à prendre un baggle dans une case qui n'en contient pas, " +
				"ou une exception AlreadyHaveBaggleException si vous portez déjà un baggle." +
				"<p>Tenter de déposer un baggle sur une case qui en contient déjà lève une exception AlreadyHaveBaggleException.</p>" +
				"<p>Les SimpleBuggles ne lèvent jamais d'exception et ouvre des fenêtres d'erreur à la place.";
				
				
	}
	
	public BuggleWorldCell getCell(int x, int y) {
		return this.world[x][y];
	}

	public void setCell(BuggleWorldCell c, int x, int y) {
		this.world[x][y] = c;
		notifyWorldUpdatesListeners();
	}

	public int getWidth() {
		return this.sizeX;
	}

	public int getHeight() {
		return this.sizeY;
	}

	@Override
	public BuggleWorldView getView() {
		return new BuggleWorldView(this);
	}
	@Override
	public EntityControlPanel getEntityControlPanel() {
		return new BuggleButtonPanel();
	}

	/* IO related */
	private String strip(String s) {
		return s.replaceAll(";.*", "");
	}

	@Override
	public void readFromFile(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		int width = 0;
		if (line != null)
			width = Integer.parseInt(strip(line));
		line = reader.readLine();
		int height = 0;
		if (line != null)
			height = Integer.parseInt(strip(line));

		create(width, height);

		/* read each cell, one after the other */
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				BuggleWorldCell cell = new BuggleWorldCell(this, x, y);
				line = reader.readLine();
				if (line == null) 
					throw new IOException("File ending before the map was read completely");

				line = strip(line); // strip '; comment'

				int index1 = line.indexOf("),");
				int index2 = line.indexOf(',', index1+2);
				int index3 = line.indexOf(',', index2+1);
				int index4 = line.length()-2;

				boolean baggleFlag = Boolean.parseBoolean(line.substring(index1+2, index2));
				boolean topWallFlag = Boolean.parseBoolean(line.substring(index2+1, index3));
				boolean leftWallFlag = Boolean.parseBoolean(line.substring(index3+1, index4));
				if (baggleFlag)
					try {
						cell.setBaggle(new Baggle(cell));
					} catch (AlreadyHaveBaggleException e) {
						e.printStackTrace();
					}
					if (topWallFlag)
						cell.putTopWall();
					if (leftWallFlag)
						cell.putLeftWall();		

					/* parse color */
					String s =line.substring(1, index1+1); 
					index1 = s.indexOf(",");
					index2 = s.indexOf(',', index1+1);
					index3 = s.length()-1;

					int r = Integer.parseInt(s.substring(1, index1));
					int g = Integer.parseInt(s.substring(index1+1, index2));
					int b = Integer.parseInt(s.substring(index2+1, index3));

					cell.setColor(new Color(r,g,b));

					setCell(cell, x, y);
			}
		}
	}

	@Override
	public void writeToFile(BufferedWriter writer) throws IOException {

		writer.write(getName() + "; name");
		writer.write(getWidth() + "; width");
		writer.write("\n");
		writer.write(getHeight() + "; height");
		writer.write("\n");

		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				BuggleWorldCell cell = getCell(x, y);

				writer.write("[");
				Color c = cell.getColor();
				writer.write("(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")");
				writer.write(",");
				writer.write(Boolean.toString(cell.hasBaggle()));
				writer.write(",");
				writer.write(Boolean.toString(cell.hasTopWall()));
				writer.write(",");
				writer.write(Boolean.toString(cell.hasLeftWall()));
				writer.write("] ; cell");
				writer.write("\n");
			}
		}
	}
	
	@Override
	public String toString() {
		return super.toString(); 
	/*	
		// cell
		String res = "";
		for (int j = 0; j < sizeY + 2; j++)
			res += "-";
		res += "\n";
		for (int i = 0; i < sizeX; i++) {
			res += "|";
			for (int j = 0; j < sizeY; j++)
				res += world[i][j].toString();
			res += "|\n";
		}
		for (int j = 0; j < sizeY + 2; j++)
			res += "-";
		res += "\n";

		// buggles
		//res += buggles.toString();

		// buggles
	
		Iterator<AbstractBuggle> it;
		for (it = buggles(); it.hasNext();) {
			AbstractBuggle b = it.next();
			res += "Buggle: "+b.toString()+"\n";
		}
	
		
		return res;
	*/	
	}

	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		//result = PRIME * result + ((entities == null) ? 0 : entities.hashCode());
		result = PRIME * result + sizeX;
		result = PRIME * result + sizeY;
		result = PRIME * result + Arrays.hashCode(world);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BuggleWorld other = (BuggleWorld) obj;
		if (sizeX != other.sizeX)
			return false;
		if (sizeY != other.sizeY)
			return false;
		for (int x=0; x<getWidth(); x++) 
			for (int y=0; y<getHeight(); y++) 
				if (!getCell(x, y).equals(other.getCell(x, y)))
					return false;

		return super.equals(obj);
	}
}

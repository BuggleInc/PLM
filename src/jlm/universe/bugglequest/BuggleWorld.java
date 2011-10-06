package jlm.universe.bugglequest;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;
import jlm.universe.EntityControlPanel;
import jlm.universe.GridWorld;
import jlm.universe.World;
import jlm.universe.bugglequest.exception.AlreadyHaveBaggleException;
import jlm.universe.bugglequest.ui.BuggleButtonPanel;
import jlm.universe.bugglequest.ui.BuggleWorldView;


public class BuggleWorld extends GridWorld {

	public BuggleWorld(String name, int x, int y) {
		super(name,x,y);
	}
	@Override
	public void create(int x, int y) {
		super.create(x,y);
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++)
				setCell(new BuggleWorldCell(this, i, j), i, j) ;
	}

	/** 
	 * Create a new world being almost a copy of the first one. Beware, all the buggles of the copy are changed to BuggleRaw. 
	 * @param world2
	 */
	public BuggleWorld(BuggleWorld world2) {
		super(world2);
	}

	/**
	 * Reset the content of a world to be the same than the one passed as argument
	 * does not affect the name of the initial world.
	 */
	@Override
	public void reset(World iw) {
		BuggleWorld initialWorld = (BuggleWorld)iw;
		for (int i = 0; i < sizeX; i++)
			for (int j = 0; j < sizeY; j++) {
				BuggleWorldCell c = (BuggleWorldCell) initialWorld.getCell(i, j);
				cells[i][j] = new BuggleWorldCell(c, this);
			}


		super.reset(initialWorld);
	}	

	@Override
	public BuggleWorldView[] getView() {
		return new BuggleWorldView[] { new BuggleWorldView(this) };
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
		/* Kill the '; name' part */
		if (!line.contains("; name"))
			throw new RuntimeException("Parse error: expected the world name field, got '"+line+"'");
		Pattern p = Pattern.compile(";.*$");
		Matcher m = p.matcher(line);
		m.replaceAll("");

		line = reader.readLine();
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

		writer.write(getName() + "; name\n");
		writer.write(getWidth() + "; width\n");
		writer.write(getHeight() + "; height\n");

		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				BuggleWorldCell cell = (BuggleWorldCell) getCell(x, y);

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
		result = PRIME * result + Arrays.hashCode(cells);
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

	public BuggleWorldCell getCell(int x, int y) {
		return (BuggleWorldCell) super.getCell(x, y);
	}

	/* adapters to the cells */
	public void setColor(int x, int y, Color c) {
		getCell(x, y).setColor(c);
	}
	public void addContent(int x, int y, String string) {
		getCell(x, y).addContent(string);
	}

	public void putTopWall(int x, int y) {
		getCell(x, y).putTopWall();		
	}

	public void putLeftWall(int x, int y) {
		getCell(x, y).putLeftWall();		
	}
	public void newBaggle(int x, int y) throws AlreadyHaveBaggleException {
		getCell(x, y).newBaggle();		
	}
	@Override
	public String getBindings(ProgrammingLanguage lang) {
		if (lang.equals(Game.PYTHON)) {
			String res =  
				"def forward(steps=1):\n"+
				"	entity.forward(steps)\n"+
				"def backward(steps=1):\n"+
				"	entity.backward(steps)\n"+
				"def turnLeft():\n"+
				"	entity.turnLeft()\n"+
				"def turnBack():\n"+
				"	entity.turnBack()\n"+
				"def turnRight():\n"+
				"	entity.turnRight()\n"+
				"\n"+
				"def getX():\n"+
				"	return entity.getX()\n"+
				"def getY():\n"+
				"	return entity.getY()\n"+
				"def setX(x):\n"+
				"	entity.setX(x)\n"+
				"def setY(y):\n"+
				"	entity.setY(y)\n"+
				"def setPos(x,y):\n"+
				"	entity.setPos(x,y)\n"+
				"def brushDown():\n"+
				"   entity.brushDown()\n"+
				"def brushUp():\n"+
				"   entity.brushUp()\n" +
				"def isFacingWall():" +
				"	return entity.isFacingWall()\n"
				;
			return res;
		}
		if (lang.equals(Game.JAVASCRIPT)) {
			String res = 
				"function forward(steps) {\n"+
				"   if (steps==undefined) steps = 1;"+
				"	entity.forward(steps)\n"+
				"}"+
				"function backward(steps){\n"+
				"   if (steps==undefined) steps = 1;"+
				"	entity.backward(steps)\n"+
				"}"+
				"function turnLeft(){\n"+
				"	entity.turnLeft()\n"+
				"}"+
				"function turnBack(){\n"+
				"	entity.turnBack()\n"+
				"}"+
				"function turnRight(){\n"+
				"	entity.turnRight()\n"+
				"}"+
				"\n"+
				"function getX(){\n"+
				"	return entity.getX()\n"+
				"}"+
				"function getY(){\n"+
				"	return entity.getY()\n"+
				"}"+
				"function setX(x){\n"+
				"	entity.setX(x)\n"+
				"}"+
				"function setY(y){\n"+
				"	entity.setY(y)\n"+
				"}"+
				"function setPos(x,y){\n"+
				"	entity.setPos(x,y)\n"+
				"}"+
				"function brushDown(){\n"+
				"   entity.brushDown()\n"+
				"}"+
				"function brushUp(){\n"+
				"   entity.brushUp()\n"+
				"}"+
				"function isFacingWall(){\n"+
				"  return entity.isFacingWall();\n" +
				"}"
				;
			return res;
		}
		throw new RuntimeException("No binding of BuggleWorld for "+lang);
	}
	@Override
	public String diffTo(World world) {
		BuggleWorld other = (BuggleWorld) world;
		StringBuffer sb = new StringBuffer();
		for (int x=0; x<getWidth(); x++) 
			for (int y=0; y<getHeight(); y++) 
				if (!getCell(x, y).equals(other.getCell(x, y))) 
					sb.append("  In ("+x+","+y+")"+  getCell(x, y).diffTo(other.getCell(x, y))+".\n");
		for (int i=0; i<entities.size(); i++)  
			if (! entities.get(i).equals(other.entities.get(i))) 
				sb.append("  Something's wrong about buggle '"+entities.get(i).getName()+"':\n"+
						((AbstractBuggle) entities.get(i)).diffTo((AbstractBuggle) other.entities.get(i)));
		return sb.toString();
	}

}

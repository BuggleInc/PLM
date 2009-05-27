package universe.bugglequest;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

import jlm.universe.EntityControlPanel;
import jlm.universe.GridWorld;
import jlm.universe.World;
import universe.bugglequest.exception.AlreadyHaveBaggleException;
import universe.bugglequest.ui.BuggleButtonPanel;
import universe.bugglequest.ui.BuggleWorldView;


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
	 * @param initialWorld
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
		BuggleWorldView[] res = new BuggleWorldView[1];
		res[0] = new BuggleWorldView(this);
		return res;
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
	/* adapters to the cells */
	public void setColor(int x, int y, Color c) {
		((BuggleWorldCell) getCell(x, y)).setColor(c);
	}
	public void addContent(int x, int y, String string) {
		((BuggleWorldCell) getCell(x, y)).addContent(string);
	}

	public void putTopWall(int x, int y) {
		((BuggleWorldCell) getCell(x, y)).putTopWall();		
	}

	public void putLeftWall(int x, int y) {
		((BuggleWorldCell) getCell(x, y)).putLeftWall();		
	}
	public void newBaggle(int x, int y) throws AlreadyHaveBaggleException {
		((BuggleWorldCell) getCell(x, y)).newBaggle();		
	}

}

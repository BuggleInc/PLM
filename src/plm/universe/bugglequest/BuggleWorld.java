package plm.universe.bugglequest;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.xnap.commons.i18n.I18n;

import plm.core.lang.LangBlockly;
import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.utils.ColorMapper;
import plm.core.utils.FileUtils;
import plm.core.utils.InvalidColorNameException;
import plm.universe.BrokenWorldFileException;
import plm.universe.Direction;
import plm.universe.Entity;
import plm.universe.GridWorld;
import plm.universe.GridWorldCell;
import plm.universe.World;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;


public class BuggleWorld extends GridWorld {

	public BuggleWorld(Game game, String name, int x, int y) {
		super(game, name,x,y);
	}
	@Override
	public GridWorldCell newCell(int x, int y) {
		return new BuggleWorldCell(this, x, y);
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
		easter=false;

		super.reset(initialWorld);
	}	
	@Override
	public void setWidth(int w) {
		super.setWidth(w);
		if (selectedCell != null && selectedCell.getX()>=w)
			selectedCell = null;
		for (int i=0; i<entities.size();i++) {
			AbstractBuggle b = (AbstractBuggle) entities.get(i);
			if (b.getX()>w)
				entities.remove(i--); // -- to counter the effect of ++ at the  end of body loop
		}
	}
	@Override
	public void setHeight(int h) {
		super.setHeight(h);
		if (selectedCell != null && selectedCell.getY()>=h)
			selectedCell = null;
		for (int i=0; i<entities.size();i++) {
			AbstractBuggle b = (AbstractBuggle) entities.get(i);
			if (b.getY()>h)
				entities.remove(i--); // -- to counter the effect of ++ at the  end of body loop
		}
	}

	public boolean easter = false;
	/* IO related */
	@Override
	public boolean haveIO() {
		return true;
	}
	
	public static World newFromFile(Game game, String path) throws IOException, BrokenWorldFileException {
		BuggleWorld res = new BuggleWorld(game, "toto", 1, 1);
		return res.readFromFile(path);
	}
	
	@Override
	public World readFromFile(String path) throws IOException, BrokenWorldFileException {
		BuggleWorld res = new BuggleWorld(getGame(), "toto", 1, 1);

		return readFromFile(path,"BuggleWorld",res);
	}
	
	public World readFromFile(String path, String classname, BuggleWorld res) throws IOException, BrokenWorldFileException {
		String name;
		if (path.endsWith(".map"))
			System.err.println(getGame().i18n.tr("{0}: The path to the map on disk should not include the .map extension (or it won''t work in jarfiles). Please fix your exercise.",path));
		
		BufferedReader reader = FileUtils.newFileReader(path, null, "map", false);
		
		/* Get the world name from the first line */
		String line = reader.readLine();
		if (line == null)
			throw new BrokenWorldFileException(getGame().i18n.tr(
					"{0}.map: this file does not seem to be a serialized BuggleWorld (the file is empty!)",path));
		
		Pattern p = Pattern.compile("^"+classname+": ");
		Matcher m = p.matcher(line);
		if (!m.find())
			throw new RuntimeException(getGame().i18n.tr(
					"{0}.map: This file does not seem to be a serialized BuggleWorld (malformated first line: {1})", path, line));
		name = m.replaceAll("");
		
		/* Get the dimension from the second line that is eg "Size: 20x20" */
		line = reader.readLine();
		if (line == null)
			throw new RuntimeException(getGame().i18n.tr("" +
					"{0}.map: End of file reached before world size specification",path));
		p = Pattern.compile("^Size: (\\d+)x(\\d+)$");
		m = p.matcher(line);
		if (!m.find()) 
			throw new RuntimeException(getGame().i18n.tr("{0}.map:1: Expected ''Size: NNxMM'' but got ''{0}''", line));
		int width = Integer.parseInt(m.group(1)); 
		int height = Integer.parseInt(m.group(2));

		res.setName(name);
		res.setWidth(width);
		res.setHeight(height);
		
		line = reader.readLine();
		
		Pattern bugglePattern = Pattern.compile("^Buggle\\((\\d+),(\\d+)\\): (\\w+),([^,]+),([^,]+),([^,]+),([^,]+),$"); // direction, color, brush, name, haveBaggle|noBaggle
		Matcher buggleMatcher = bugglePattern.matcher(line);
		String cellFmt = "^Cell\\((\\d+),(\\d+)\\): ([^,]+?),(\\w+),(\\w+),(\\w+),(.*)$";
		Pattern cellPattern = Pattern.compile(cellFmt);
		Matcher cellMatcher = cellPattern.matcher(line);

		do {
			cellMatcher = cellPattern.matcher(line);
			buggleMatcher = bugglePattern.matcher(line);

			if (buggleMatcher.matches()) { 
				int x=Integer.parseInt( buggleMatcher.group(1) );
				int y=Integer.parseInt( buggleMatcher.group(2) );

				if (x<0 || x > width || y<0 || y>height)
					throw new BrokenWorldFileException(getGame().i18n.tr(
							"Cannot put a buggle on coordinate {0},{1}: that''s out of the world",x,y));

				String dirName = buggleMatcher.group(3);
				Direction direction;
				if (dirName.equalsIgnoreCase("north"))
					direction = Direction.NORTH;
				else if (dirName.equalsIgnoreCase("south"))
					direction = Direction.SOUTH;
				else if (dirName.equalsIgnoreCase("east"))
					direction = Direction.EAST;
				else if (dirName.equalsIgnoreCase("west"))
					direction = Direction.WEST;
				else 
					throw new BrokenWorldFileException(getGame().i18n.tr(
							"Invalid buggle''s direction: {0}", buggleMatcher.group(3)));

				Color color;
				try {
					color = ColorMapper.name2color( buggleMatcher.group(4));
				} catch (InvalidColorNameException e) {
					throw new BrokenWorldFileException(getGame().i18n.tr(
							"Invalid buggle''s color name: {0}", buggleMatcher.group(4)));
				}
				Color brushColor;
				try {
					brushColor = ColorMapper.name2color( buggleMatcher.group(5));
				} catch (InvalidColorNameException e) {
					throw new BrokenWorldFileException(getGame().i18n.tr(
							"Invalid buggle''s color name: {0}", buggleMatcher.group(5)));
				}
				String buggleName = buggleMatcher.group(6);
				SimpleBuggle b = new SimpleBuggle(res, buggleName, x, y, direction, color, brushColor);
				String haveBaggle = buggleMatcher.group(7);
				if (haveBaggle.equals("haveBaggle"))
					b.doCarryBaggle();
				else if (! haveBaggle.equals("noBaggle"))
					throw new BrokenWorldFileException("Broken file, invalid buggle carrying information '"+haveBaggle+"': A buggle can either carry a baggle (haveBaggle) or not (noBaggle)");

			} else if (cellMatcher.matches()) {
				/* Get the info */
				int x=Integer.parseInt( cellMatcher.group(1) );
				int y=Integer.parseInt( cellMatcher.group(2) );

				if (x<0 || x > width || y<0 || y>height)
					throw new BrokenWorldFileException(getGame().i18n.tr(
							"Cannot define a cell on coordinate {0},{1}: that''s out of the world",x,y));


				String colorName = cellMatcher.group(3);
				Color color;
				String baggleFlag = cellMatcher.group(4);
				String topWallFlag = cellMatcher.group(5);
				String leftWallFlag = cellMatcher.group(6);
				String content = cellMatcher.group(7);

				try {
					color = ColorMapper.name2color(colorName);
				} catch (InvalidColorNameException e) {
					throw new BrokenWorldFileException(getGame().i18n.tr("Invalid color name: {0}",colorName));
				}

				/* Make sure that this info makes sense */
				if (!baggleFlag.equalsIgnoreCase("baggle") && !baggleFlag.equalsIgnoreCase("nobaggle"))
					throw new BrokenWorldFileException(getGame().i18n.tr(
							"Expecting ''baggle'' or ''nobaggle'' but got {0} instead",baggleFlag));

				if (!topWallFlag.equalsIgnoreCase("topwall") && !topWallFlag.equalsIgnoreCase("notopwall"))
					throw new BrokenWorldFileException(getGame().i18n.tr(
							"Expecting ''topwall'' or ''notopwall'' but got {0} instead",topWallFlag));

				if (!leftWallFlag.equalsIgnoreCase("leftwall") && !leftWallFlag.equalsIgnoreCase("noleftwall"))
					throw new BrokenWorldFileException(getGame().i18n.tr(
							"Expecting ''leftwall'' or ''noleftwall'' but got {0} instead",leftWallFlag));

				/* Use the info */
				BuggleWorldCell cell = new BuggleWorldCell(res, x, y);

				if (baggleFlag.equalsIgnoreCase("baggle"))
					try {
						cell.baggleAdd();
					} catch (AlreadyHaveBaggleException e) {
						throw new BrokenWorldFileException(getGame().i18n.tr(
								"The cell {0},{1} seem to be defined more than once. At least, there is two baggles here, which is not allowed.",x,y));
					}

				if (topWallFlag.equalsIgnoreCase("topwall"))
					cell.putTopWall();
				if (leftWallFlag.equalsIgnoreCase("leftwall"))
					cell.putLeftWall();		

				cell.setColor(color);

				if (content.length()>0)
					cell.setContent(content);

				res.setCell(cell, x, y);
			} else {
				throw new BrokenWorldFileException(getGame().i18n.tr(
						"Parse error. I was expecting a cell or a buggle description but got: {0}",line));					
			}

			line = reader.readLine();
		} while (line != null);

		return res;
	}

	@Override
	public void writeToFile(BufferedWriter writer) throws IOException {

		writer.write("BuggleWorld: "+getName() + "\n");
		writer.write("Size: "+getWidth() + "x"+ getHeight() + "\n");

		for (Entity e : getEntities()) {
			AbstractBuggle b = (AbstractBuggle) e;
			writer.write("Buggle("+b.getX()+","+b.getY()+"): ");
			
			if (b.getDirection().equals(Direction.NORTH)) 
				writer.write("north,");
			if (b.getDirection().equals(Direction.SOUTH)) 
				writer.write("south,");
			if (b.getDirection().equals(Direction.EAST)) 
				writer.write("east,");
			if (b.getDirection().equals(Direction.WEST)) 
				writer.write("west,");
			
			writer.write(ColorMapper.color2name(b.getBodyColor())+",");
			writer.write(ColorMapper.color2name(b.getBrushColor())+",");
			writer.write(b.getName()+",");
			if (b.isCarryingBaggle())
				writer.write("haveBaggle,");
			else
				writer.write("noBaggle,");
			writer.write("\n");
		}
			
		
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				BuggleWorldCell cell = (BuggleWorldCell) getCell(x, y);

				if ((!cell.getColor().equals(Color.white)) || cell.hasBaggle() || 
						cell.hasLeftWall() || cell.hasTopWall() || cell.hasContent()
						) {
					
					writer.write("Cell("+x+","+y+"): ");
					writer.write(ColorMapper.color2name(cell.getColor())+",");
					
					if (cell.hasBaggle()) 
						writer.write("baggle,");
					else 
						writer.write("nobaggle,");
					
					if (cell.hasTopWall()) 
						writer.write("topwall,");
					else 
						writer.write("notopwall,");

					if (cell.hasLeftWall()) 
						writer.write("leftwall,");
					else 
						writer.write("noleftwall,");
					
					if (cell.hasContent())
						writer.write(cell.getContent());
					writer.write("\n");
				}
			}
		}
	}

	@Override
	public String toString() {
		return super.toString(); 
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
	public World ignoreDirectionDifference() {
		for (Entity e: getEntities()) 
			((AbstractBuggle)e).ignoreDirectionDifference();
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if ( !(obj instanceof BuggleWorld) )
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

	/* Cell selection is particularly important to world edition */
	BuggleWorldCell selectedCell=null;
	public BuggleWorldCell getSelectedCell() {
		return selectedCell;
	}
	public void setSelectedCell(int x, int y) {
		selectedCell = getCell(x,y);
	}
	public void unselectCell() {
		selectedCell = null;
	}
	
	/* adapters to the cells */
	public BuggleWorldCell getCell(int x, int y) {
		return (BuggleWorldCell) super.getCell(x, y);
	}
	public void setColor(int x, int y, Color c) {
		getCell(x, y).setColor(c);
	}
	public void addContent(int x, int y, String string) {
		getCell(x, y).addContent(string);
	}
	public void addBaggle(int x, int y) {
		getCell(x, y).baggleAdd();
	}
	public void putTopWall(int x, int y) {
		getCell(x, y).putTopWall();		
	}

	public void putLeftWall(int x, int y) {
		getCell(x, y).putLeftWall();		
	}
	@Override
	public void setupBindings(ProgrammingLanguage lang,ScriptEngine engine) throws ScriptException {
		if (lang instanceof LangPython || lang instanceof LangBlockly) {
			engine.put("Direction", Direction.class);
			engine.put("Color", Color.class);
			engine.eval(
				"def forward(steps=1):\n"+
				"	entity.forward(steps)\n"+
				"def backward(steps=1):\n"+
				"	entity.backward(steps)\n"+
				"def left():\n"+
				"	entity.left()\n"+
				"def back():\n"+
				"	entity.back()\n"+
				"def right():\n"+
				"	entity.right()\n"+
				
				"def getWorldHeight():\n"+
				"	return entity.getWorldHeight()\n"+
				"def getWorldWidth():\n"+
				"	return entity.getWorldWidth()\n"+
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
				"def setDirection(d):\n"+
				"	entity.setDirection(d)\n"+
				"def brushDown():\n"+
				"   entity.brushDown()\n"+
				"def brushUp():\n"+
				"   entity.brushUp()\n" +
				"def isBrushDown():\n"+
				"   entity.isBrushDown()\n" +
				"def isFacingWall():" +
				"	return entity.isFacingWall()\n"+
				"def isBackingWall():" +
				"	return entity.isBackingWall()\n"+
				"def getBodyColor():\n"+
				"   return entity.getBodyColor()\n"+
				"def setBodyColor(c):\n"+
				"   return setBodyColor(c)\n"+
				"def getGroundColor():\n"+
				"   return entity.getGroundColor()\n"+
				
				"def errorMsg(str):\n"+
				"  entity.seenError(str)\n"+
				"def haveSeenError():\n"+
				"  return entity.haveSeenError()\n"+
				"def seenError():\n"+
				"  entity.seenError()\n"+
				
				"def isOverBaggle():\n"+
				"	return entity.isOverBaggle()\n"+
				"def isCarryingBaggle():\n"+
				"	return entity.isCarryingBaggle()\n"+
				"def pickupBaggle():\n"+
				"	return entity.pickupBaggle()\n"+
				"def dropBaggle():\n"+
				"	return entity.dropBaggle()\n"+
				
				"def isOverMessage():\n"+
				"	return entity.isOverMessage()\n"+
				"def readMessage():\n"+
				"	return entity.readMessage()\n"+
				"def clearMessage():\n"+
				"   entity.clearMessage()\n"+
				"def writeMessage(msg):\n"+
				"   entity.writeMessage(msg)\n"+
				
				"def getDirection():\n"+
				"   return entity.getDirection()\n"+
				
				"def setBrushColor(c):\n"+
				"    entity.setBrushColor(c)\n"+
				"def getBrushColor():\n"+
				"    return entity.getBrushColor()\n"+
				
				/* BINDINGS TRANSLATION: French */
				"def avance(pas=1):\n"+
				"	if pas==1:\n"+
				"		forward()\n"+
				"	else:\n"+
				"		forward(pas)\n"+
				"def recule(pas=1):\n"+
				"	if pas==1:\n"+
				"		backward()\n"+
				"	else:\n"+
				"		backward(pas)\n"+
				"def gauche():\n"+
				"	left()\n"+
				"def retourne():\n"+
				"	back()\n"+
				"def droite():\n"+
				"	right()\n"+
				"\n"+
				"def getMondeHauteur():\n"+
				"	return getWorldHeight()\n"+
				"def getMondeLargeur():\n"+
				"	return getWorldWidth()\n"+
				"def baisseBrosse():\n"+
				"   brushDown()\n"+
				"def leveBrosse():\n"+
				"   brushUp()\n" +
				"def estBrosseBaissee():\n"+
				"   isBrushDown()\n" +
				"def estFaceMur():" +
				"	return isFacingWall()\n"+
				"def estDosMur():" +
				"	return isBackingWall()\n"+
				"def getCouleurSol():\n"+
				"   return getGroundColor()\n"+
				
				"def estSurBiscuit():\n"+
				"	return isOverBaggle()\n"+
				"def porteBiscuit():\n"+
				"	return isCarryingBaggle()\n"+
				"def prendBiscuit():\n"+
				"	return pickupBaggle()\n"+
				"def poseBiscuit():\n"+
				"	return dropBaggle()\n"+
				
				"def estSurMessage():\n"+
				"	return isOverMessage()\n"+
				"def litMessage():\n"+
				"	return readMessage()\n"+
				"def effaceMessage():\n"+
				"   clearMessage()\n"+
				"def ecritMessage(msg):\n"+
				"   writeMessage(msg)\n"+
				
				"def getCouleurCorps():\n"+
				"   return getBodyColor()\n"+
				"def setCouleurCorps(c):\n"+
				"   setBodyColor(c)\n"+
				
				"def setCouleurBrosse(c):\n"+
				"    setBrushColor(c)\n"+
				"def getCouleurBrosse():\n"+
				"    return getBrushColor()\n"+
				
				"def errorMsg(str):\n"+
				"  entity.seenError(str)\n"
				
						);		
		} else {
			throw new RuntimeException("No binding of BuggleWorld for "+lang);
		}
	}
	@Override
	public String diffTo(World world, I18n i18n) {
		BuggleWorld other = (BuggleWorld) world;
		StringBuffer sb = new StringBuffer();
		if (! other.getName().equals(getName()))
			sb.append(i18n.tr("  The world''s name is {0}",other.getName()));
		for (int x=0; x<getWidth(); x++) 
			for (int y=0; y<getHeight(); y++) 
				if (!getCell(x, y).equals(other.getCell(x, y))) 
					sb.append(i18n.tr("  In ({0},{1})",x,y)+  getCell(x, y).diffTo(other.getCell(x, y), i18n)+".\n");
		if (entities.size() != other.entities.size()) {
			sb.append(i18n.tr("  There is {0} entities where {1} were expected.",other.entities.size(),entities.size()));
		} else {
			for (int i=0; i<entities.size(); i++)  
				if (! entities.get(i).equals(other.entities.get(i))) 
					sb.append(i18n.tr("  Something is wrong about buggle ''{0}'':\n",entities.get(i).getName())+
							((AbstractBuggle) entities.get(i)).diffTo((AbstractBuggle) other.entities.get(i), i18n));
		}
		return sb.toString();
	}

}

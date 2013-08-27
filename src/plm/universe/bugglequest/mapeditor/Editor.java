package plm.universe.bugglequest.mapeditor;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.universe.BrokenWorldFileException;
import plm.universe.Entity;
import plm.universe.World;
import plm.universe.bugglequest.AbstractBuggle;
import plm.universe.bugglequest.BuggleWorld;

public class Editor {

	private BuggleWorld world;
	private ArrayList<EditionListener> editionListeners = new ArrayList<EditionListener>();
	private String command = "topwall";
	private Color selectedColor = Color.blue;
	private int selectedColorNumber = 1;
	public I18n	i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages", new Locale("fr"), I18nFactory.FALLBACK); // FIXME: ugly: french is hardcoded!!!!!

	
	public Editor() {
		createNewMap(10, 10);
		world.setSelectedCell(0, 0);
	}

	public void createNewMap(int width, int height) {
		this.world = new BuggleWorld("Brave new world",width, height);
		
		for (EditionListener v : this.editionListeners) {
			v.setWorld(this.world);
		}
				
		notifySetWorld(world);
	}

	public void saveMap(File file) {
		try {
			this.world.writeToFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadMap(String file) throws IOException {
		try {
			world = (BuggleWorld) BuggleWorld.newFromFile(file); 
		} catch (BrokenWorldFileException e) {
			e.printStackTrace();
		}
		notifySetWorld(world);
	}

	public BuggleWorld getWorld() {
		return this.world;
	}
	
	public void addEditionListener(EditionListener v) {
		this.editionListeners.add(v);
	}

	public void removeEditionListener(EditionListener v) {
		this.editionListeners.remove(v);
	}

	public void notifySetWorld(World w) {
		for (EditionListener v : this.editionListeners) 
			v.setWorld(w);
	}
	public void notifyWorldEdited(){
		for (EditionListener el : editionListeners)
			el.worldEdited();		
	}

	public void setCommand(String cmd) {
		command = cmd;
	}
	public String getCommand() {
		return command;
	}

	public void setSelectedColor(Color c) {
		selectedColor = c;
	}
	public Color getSelectedColor() {
		return selectedColor;
	}

	public int getSelectedColorNumber() {
		return selectedColorNumber ;
	}

	public void setSelectedColorNumber(int i) {
		selectedColorNumber = i;		
	}

	public void setSelectedCell(int x, int y) {
		AbstractBuggle buggle = null;
		for (Entity e : getWorld().getEntities()) {
			AbstractBuggle b = (AbstractBuggle) e;
			if (b.getX() == x && b.getY() == y)
				buggle = b;
		}
		getWorld().setSelectedCell(x, y);
		
		for (EditionListener el : editionListeners)
			el.selectedChanged(x, y, buggle);
	}
	public void setSelectedEntity(AbstractBuggle buggle) {
		int x = getWorld().getSelectedCell().getX();
		int y = getWorld().getSelectedCell().getY();
		
		getWorld().setSelectedEntity(buggle);
		for (EditionListener el : editionListeners)
			el.selectedChanged(x, y, buggle);
		
	}

}

package jlm.universe.bugglequest.mapeditor;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import jlm.universe.BrokenWorldFileException;
import jlm.universe.Entity;
import jlm.universe.IWorldView;
import jlm.universe.World;
import jlm.universe.bugglequest.BuggleWorld;

public class Editor {

	private BuggleWorld world;
	private ArrayList<EditionListener> editionListeners = new ArrayList<EditionListener>();
	private String command = "topwall";
	private Color selectedColor = Color.blue;
	private int selectedColorNumber = 1;
	
	public Editor() {
		createNewMap(10, 10);
	}

	public void createNewMap(int width, int height) {
		this.world = new BuggleWorld("edited world",width, height);
		
		for (EditionListener v : this.editionListeners) {
			v.setWorld(this.world);
		}
				
		this.world.addWorldUpdatesListener(new IWorldView() {
			@Override
			public void worldHasChanged() {
				notifyWorldEdited();
			}

			@Override
			public void worldHasMoved() {
				notifyWorldEdited();
			}
		});
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
			this.world.readFromFile(file);
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
	public void notifySelectedEntity(Entity e) {
		for (EditionListener el : editionListeners)
			el.setSelectedEntity(e);
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

}

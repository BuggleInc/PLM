package jlm.universe.bugglequest.mapeditor;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import jlm.core.ui.MarkdownDocument;
import jlm.universe.IWorldView;
import jlm.universe.bugglequest.BuggleWorld;

public class Editor {

	private BuggleWorld world;
	private ArrayList<MapView> mapViews = new ArrayList<MapView>();
	private String command = "topwall";
	private Color selectedColor = Color.blue;
	private int selectedColorNumber = 1;
	private String path;
	
	public Editor() {
		createNewMap(10, 10);
	}

	public void createNewMap(int width, int height) {
		this.world = new BuggleWorld("edited world",width, height);
		
		for (MapView v : this.mapViews) {
			v.setWorld(this.world);
		}
				
		this.world.addWorldUpdatesListener(new IWorldView() {
			@Override
			public void worldHasChanged() {
				notifyMapViews();
			}

			@Override
			public void worldHasMoved() {
				notifyMapViews();
			}
		});
		notifyMapViews();
	}

	public void saveMap(File file) {
		try {
			this.world.writeToFile(file);
			System.out.println("Map file "+file.getPath()+" saved.");
		} catch (IOException e) {
			System.err.println("Map file '"+file.getPath()+"' cannot be saved.");
		}
	}

	public void loadMap(File file) {
		try {
			//checking if the map file already exists in '.jlm-export/' directory.
			File f = new File(MarkdownDocument.getSAVE_PATH()+file.getPath().replaceAll("^src", ""));
			if (!f.exists()) {
				f=file;
			}
			//Loading
			this.world.readFromFile(f);
		} catch (IOException e) {
			System.err.println("Map file '"+file.getPath()+"' not found.");
		}
		//Recording location of the map in the jar
		setPath(file.getPath());
		notifyMapViews();
	}

	public BuggleWorld getWorld() {
		return this.world;
	}
	
	public void addMapView(MapView v) {
		this.mapViews.add(v);
	}

	public void removeMapView(MapView v) {
		this.mapViews.remove(v);
	}

	public void notifyMapViews() {
		for (MapView v : this.mapViews) {
			v.worldHasMoved();
		}
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}

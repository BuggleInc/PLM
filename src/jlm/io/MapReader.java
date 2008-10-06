package jlm.io;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import jlm.bugglequest.Baggle;
import jlm.bugglequest.World;
import jlm.bugglequest.WorldCell;
import jlm.exception.AlreadyHaveBaggleException;


public class MapReader extends Reader {

	private BufferedReader reader;

	public MapReader(BufferedReader br) {
		this.reader = br;
	}

	@Override
	public void close() throws IOException {
		if (reader != null) 
			reader.close();
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		throw new IOException("read(char[], int, int) method is not provided in "+this.getClass().getName());
	}

	private String strip(String s) {
		return s.replaceAll(";.*", "");
	}

	public void read(World world) throws IOException {
		String line = reader.readLine();
		int width = 0;
		if (line != null)
			width = Integer.parseInt(strip(line));
		line = reader.readLine();
		int height = 0;
		if (line != null)
			height = Integer.parseInt(strip(line));

		world.create(width, height);
		
		for (int x = 0; x < world.getWidth(); x++) {
			for (int y = 0; y < world.getHeight(); y++) {
				WorldCell cell = new WorldCell(world, x, y);
				read(cell);
				world.setCell(cell, x, y);
			}
		}
	}

	public void read(WorldCell cell) throws IOException {
		String line = reader.readLine();
		if (line == null) 
			throw new IOException("File ending before the map was read completely");
		
		line = strip(line); // strip '; comment'

		int index1 = line.indexOf("),");
		int index2 = line.indexOf(',', index1+2);
		int index3 = line.indexOf(',', index2+1);
		int index4 = line.length()-2;
		
		Color c = parseColor(line.substring(1, index1+1));
		
		boolean baggleFlag = Boolean.parseBoolean(line.substring(index1+2, index2));
		boolean topWallFlag = Boolean.parseBoolean(line.substring(index2+1, index3));
		boolean leftWallFlag = Boolean.parseBoolean(line.substring(index3+1, index4));

		cell.setColor(c);
		
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
	}
	
	public Color parseColor(String s) {
		int index1 = s.indexOf(",");
		int index2 = s.indexOf(',', index1+1);
		int index3 = s.length()-1;
		
		int r = Integer.parseInt(s.substring(1, index1));
		int g = Integer.parseInt(s.substring(index1+1, index2));
		int b = Integer.parseInt(s.substring(index2+1, index3));
		
		return new Color(r,g,b);
	}
}

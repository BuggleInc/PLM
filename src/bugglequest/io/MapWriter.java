package bugglequest.io;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import bugglequest.core.World;
import bugglequest.core.WorldCell;

public class MapWriter extends Writer {

	private BufferedWriter writer;

	public MapWriter(BufferedWriter bw) {
		this.writer = bw;
	}

	@Override
	public void close() throws IOException {
		writer.close();
	}

	@Override
	public void flush() throws IOException {
		writer.flush();
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		writer.write(cbuf, off, len);
	}

	public void write(World world) throws IOException {
		writer.write(world.getWidth() + "; width");
		writer.write("\n");
		writer.write(world.getHeight() + "; height");
		writer.write("\n");

		for (int x = 0; x < world.getWidth(); x++) {
			for (int y = 0; y < world.getHeight(); y++) {
				write(world.getCell(x, y));
				writer.write("\n");
			}
		}
	}

	public void write(WorldCell cell) throws IOException {
		writer.write("[");
		write(cell.getColor());
		// writer.write(writecell.getColor().toString());
		writer.write(",");
		writer.write(Boolean.toString(cell.hasBaggle()));
		writer.write(",");
		writer.write(Boolean.toString(cell.hasTopWall()));
		writer.write(",");
		writer.write(Boolean.toString(cell.hasLeftWall()));
		writer.write("] ; cell");
	}

	public void write(Color c) throws IOException {
		writer.write("(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ")");
	}

}

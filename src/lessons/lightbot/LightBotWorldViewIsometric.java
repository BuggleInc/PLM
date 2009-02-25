package lessons.lightbot;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import jlm.ui.WorldView;
import jlm.universe.Entity;
import jlm.universe.World;
import lessons.lightbot.LightBotWorld.CellIterator;

public class LightBotWorldViewIsometric extends WorldView {

	private static final long serialVersionUID = -697923562790202622L;

	private static Color GRID_COLOR = new Color(0.5f, 0.5f, 0.5f);
	private static Color DARK_CELL_COLOR = new Color(0.9f, 0.9f, 0.9f);
	private static Color LIGHT_CELL_COLOR = new Color(0.93f, 0.93f, 0.93f);

	private static Color DARK_WALL_COLOR = new Color(0.7f, 0.7f, 0.7f);
	private static Color LIGHT_WALL_COLOR = new Color(0.8f, 0.8f, 0.8f);

	private static Color LIGHT_OFF_COLOR = Color.BLACK;
	private static Color LIGHT_ON_COLOR = Color.YELLOW;
	private static Color BOT_COLOR = Color.BLUE;

	private static final double CELL_WIDTH = 40.;
	private static final double CELL_HEIGHT = 15.;

	
	private List<LightBotWorldCell> cellsZOrdered = new ArrayList<LightBotWorldCell>();
	public LightBotWorldViewIsometric(World w) {
		super(w);
		computeZOrders();
		if (world.getEntityCount() > 1)
			throw new RuntimeException("Multiple entities in lightbot world not supported");
	}

	private void computeZOrders() {	
		this.cellsZOrdered.clear();
		for (LightBotWorldCell c : ((LightBotWorld) this.world))
			cellsZOrdered.add(c);

		System.out.println("cells count = "+cellsZOrdered.size());
		
		Collections.sort(cellsZOrdered, new Comparator<LightBotWorldCell>() {
			@Override
			public int compare(LightBotWorldCell c1, LightBotWorldCell c2) {				
				return LightBotWorldViewIsometric.computeZOrder(c1) - LightBotWorldViewIsometric.computeZOrder(c2);
			}
		});

		System.out.println("cells count (after sort) = "+cellsZOrdered.size());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		LightBotWorld tw = (LightBotWorld) this.world;

		double componentWidth = (double) getWidth();
		double componentHeight = (double) getHeight();
		double boardWidth = tw.getWidth() * CELL_WIDTH;
		double boardHeight = tw.getHeight() * CELL_WIDTH;
		double diagWidth = Math.sqrt(boardWidth * boardWidth + boardHeight * boardHeight);
		double diagHeight = diagWidth;

		double verticalScaleFactor = 2.;
		double angle = Math.PI / 4.;

		// DEBUG
		if (false) {
			verticalScaleFactor = 1.;
			angle = 0.;
		}

		double ratio = Math.min(componentWidth / (diagWidth), (componentHeight * verticalScaleFactor)
				/ (1.5 * diagHeight));

		double marginW = (componentWidth - ratio * diagWidth) / 2.;
		double marginH = (componentHeight - ((1.5 * ratio * diagHeight) / verticalScaleFactor)) / 2.;
		double paddingW = (diagWidth - boardWidth) / 2.;
		double paddingH = (2 * diagHeight - boardHeight) / 2.;

		// g2.setColor(Color.WHITE);
		// g2.fill(new Rectangle2D.Double(0., 0., componentWidth,
		// componentHeight));

		// center drawing in the middle of the component
		g2.translate(marginW, marginH);

		// scale to component size + half-size for height
		g2.scale(ratio, ratio / verticalScaleFactor);

		// g2.setColor(Color.RED);
		// g2.draw(new Rectangle2D.Double(0., 0., diagWidth, 1.5*diagHeight));

		GradientPaint skyGradient = new GradientPaint((float) (diagWidth / 2.), 0.f, new Color(0.0f, 0.0f, 0.6f),
				(float) ((2. * diagWidth) / 3.), (float) (diagHeight), Color.BLACK, false);
		g2.setPaint(skyGradient);
		// g2.setColor(Color.BLACK);
		g2.fill(new Rectangle2D.Double(0., 0., diagWidth, 1.5 * diagHeight));

		// shift drawing in order to be centered after it has been rotated
		g2.translate(paddingW, paddingH);
		g2.rotate(angle, boardWidth / 2., boardHeight / 2.);

		// draw world and lights
		drawWorld3D(g2);

		// draw lightBots
		//Iterator<Entity> it = world.entities();
		//while (it.hasNext())
		//	drawBot2D(g2, (LightBot) it.next());
	}

	public static int computeZOrder(LightBotWorldCell cell) {
		int tx = cell.getWorld().getWidth();
		int ty = cell.getWorld().getHeight();

		int x = cell.getY();
		int y = cell.getX();

		int val = (x + y) * (x + y + 1) / 2 + x + 1;
		if (x + y > ty - 1)
			val -= ((x + y - ty + 1) * (x + y - ty + 2)) / 2;
		if (x + y > tx)
			val -= ((x + y - tx) * (x + y - tx + 1)) / 2;

		return val;
	}

	private void drawWorld3D(Graphics2D g) {
		LightBotWorld w = (LightBotWorld) world;

		LightBot bot = (LightBot) world.getEntity(0);		

		for (LightBotWorldCell cell : this.cellsZOrdered) {
			
		
		//for (int y = 0; y < w.getHeight(); y++) {
		//	for (int x = 0; x < w.getWidth(); x++) {
				// int numCell = y * w.getWidth() + x;

		//		LightBotWorldCell cell = w.getCell(x, y);

				int x = cell.getX();
				int y = cell.getY();
				Color cellColor = Color.white;
				if ((x + y) % 2 == 0)
					cellColor = DARK_CELL_COLOR;
				else
					cellColor = LIGHT_CELL_COLOR;
				g.setColor(cellColor);

				double cw = CELL_WIDTH;
				double cx = x * cw;
				double cy = y * cw;

				double ch = cell.getHeight() * CELL_HEIGHT;

				GeneralPath face1 = new GeneralPath();
				face1.moveTo(cx, cy + cw);
				face1.lineTo(cx - ch, cy + cw - ch);
				face1.lineTo(cx + cw - ch, cy + cw - ch);
				face1.lineTo(cx + cw, cy + cw);
				face1.lineTo(cx, cy + cw);

				GeneralPath face2 = new GeneralPath();
				face2.moveTo(cx + cw, cy + cw);
				face2.lineTo(cx + cw - ch, cy + cw - ch);
				face2.lineTo(cx + cw - ch, cy - ch);
				face2.lineTo(cx + cw, cy);
				face2.lineTo(cx + cw, cy + cw);

				// floor
				g.setColor(GRID_COLOR);
				g.draw(new Rectangle2D.Double(cx, cy, cw, cw));

				// face 1
				g.setColor(DARK_WALL_COLOR);
				g.fill(face1);
				g.setColor(GRID_COLOR);
				g.draw(face1);

				// face 2
				g.setColor(LIGHT_WALL_COLOR);
				g.fill(face2);
				g.setColor(GRID_COLOR);
				g.draw(face2);

				// roof
				g.setColor(cellColor);
				g.fill(new Rectangle2D.Double(cx - ch, cy - ch, cw, cw));
				g.setColor(GRID_COLOR);
				g.draw(new Rectangle2D.Double(cx - ch, cy - ch, cw, cw));

				if (cell.isLight()) {
					drawLight2D(g, cell);
				}

				// print elevation
				g.setColor(Color.RED);
				g.drawString(Integer.toString(cell.getHeight()), (int) (cx - ch), (int) (cy + cw - ch));

				// print z-order
				g.setColor(Color.BLUE);
				g.drawString(Integer.toString(computeZOrder(cell)), (int) (cx + cw - cw / 3 - ch), (int) (cy
								+ cw - ch));
		//	}
				
				if (cell == bot.getCell()) {
					drawBot2D(g, bot);
				}
				
		}

	}

	private void drawLight2D(Graphics2D g, LightBotWorldCell cell) {
		if (cell.isLightOn()) {
			g.setColor(LIGHT_ON_COLOR);
		} else {
			g.setColor(LIGHT_OFF_COLOR);
		}
		g.fill(new Arc2D.Double(cell.getX() * CELL_WIDTH + 0.1 * CELL_WIDTH - cell.getHeight() * CELL_HEIGHT, cell
				.getY()
				* CELL_WIDTH + 0.1 * CELL_WIDTH - cell.getHeight() * CELL_HEIGHT, 0.8 * CELL_WIDTH, 0.8 * CELL_WIDTH,
				0, 360, Arc2D.OPEN));
	}

	private void drawBot2D(Graphics2D g, LightBot bot) {
		LightBotWorldCell cell = bot.getCell();

		double width = CELL_WIDTH;
		double height = CELL_WIDTH;
		double cx = cell.getX();
		double cy = cell.getY();

		double angle = 0.;
		switch (bot.getDirection().intValue()) {
		case Direction.NORTH_VALUE:
			angle = Math.PI;
			break;
		case Direction.SOUTH_VALUE:
			angle = 0.;
			break;
		case Direction.EAST_VALUE:
			angle = -Math.PI / 2.;
			break;
		case Direction.WEST_VALUE:
			angle = Math.PI / 2;
			break;
		}
		g.rotate(angle, cx * CELL_WIDTH + width / 2., cy * CELL_WIDTH + height / 2.);

		g.setColor(BOT_COLOR);
		g.fill(new Arc2D.Double((cx - 0.25) * CELL_WIDTH - cell.getHeight() * CELL_HEIGHT, (cy + 0.1) * CELL_WIDTH
				- cell.getHeight() * CELL_HEIGHT, 1.5 * width, 1.5 * height, 60, 60, Arc2D.PIE));
	}

	@Override
	public boolean isWorldCompatible(World world) {
		return world instanceof LightBotWorld;
	}
	
	public void worldHasMoved() {
		computeZOrders();
		super.worldHasMoved();
	}
	
}

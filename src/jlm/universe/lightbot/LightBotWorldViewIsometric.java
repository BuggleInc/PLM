package jlm.universe.lightbot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import jlm.core.model.Game;
import jlm.core.ui.ResourcesCache;
import jlm.core.ui.WorldView;
import jlm.universe.Direction;
import jlm.universe.GridWorld;
import jlm.universe.GridWorldCell;
import jlm.universe.World;

public class LightBotWorldViewIsometric extends WorldView {

	private static final long serialVersionUID = -697923562790202622L;

	private static final Color GRID_COLOR = new Color(0.2f, 0.2f, 0.2f);
	private static final Color DARK_CELL_COLOR = new Color(0.9f, 0.9f, 0.9f);
	private static final Color LIGHT_CELL_COLOR = new Color(0.93f, 0.93f, 0.93f);

	private static final Color DARK_WALL_COLOR = new Color(0.7f, 0.7f, 0.7f);
	private static final Color LIGHT_WALL_COLOR = new Color(0.8f, 0.8f, 0.8f);

	private static final Color LIGHT_OFF_COLOR = Color.BLACK;
	private static final Color LIGHT_ON_COLOR = Color.YELLOW;
	private static final Color BOT_COLOR = Color.BLUE;

	private static final double CELL_WIDTH = 40.;
	private static final double CELL_HEIGHT = 15.;

	private static final double verticalIsometricScaleFactor = 2.;
	private static final double isometricAngle = Math.PI / 4.;

	private List<LightBotWorldCell> cellsZOrdered = new ArrayList<LightBotWorldCell>();

	public LightBotWorldViewIsometric(World w) {
		super(w);
		initComponents();
		computeZOrders();
		if (world.getEntityCount() > 1)
			throw new RuntimeException("Multiple entities in lightbot world not supported");
	}

	private synchronized void computeZOrders() {
		this.cellsZOrdered.clear();
		for (LightBotWorldCell c : ((LightBotWorld) this.world))
			cellsZOrdered.add(c);

		Collections.sort(cellsZOrdered, new Comparator<LightBotWorldCell>() {
			@Override
			public int compare(LightBotWorldCell c1, LightBotWorldCell c2) {
				return LightBotWorldViewIsometric.computeZOrder(c1) - LightBotWorldViewIsometric.computeZOrder(c2);
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		AffineTransform originalTransform = g2.getTransform();
		
		
		GridWorld tw = (GridWorld) this.world;

		double componentWidth = (double) getWidth();
		double componentHeight = (double) getHeight();
		double boardWidth = tw.getWidth() * CELL_WIDTH;
		double boardHeight = tw.getHeight() * CELL_WIDTH;
		double diagWidth = Math.sqrt(boardWidth * boardWidth + boardHeight * boardHeight);
		double diagHeight = diagWidth;

		double ratio = Math.min(componentWidth / (diagWidth), (componentHeight * verticalIsometricScaleFactor)
				/ (1.5 * diagHeight));

		double marginW = (componentWidth - ratio * diagWidth) / 2.;
		double marginH = (componentHeight - ((1.5 * ratio * diagHeight) / verticalIsometricScaleFactor)) / 2.;
		double paddingW = (diagWidth - boardWidth) / 2.;
		double paddingH = (2 * diagHeight - boardHeight) / 2.;

		// center drawing in the middle of the component
		g2.translate(marginW, marginH);

		// scale to component size + half-size for height
		g2.scale(ratio, ratio / verticalIsometricScaleFactor);

		// draw bounding-box
		// g2.setColor(Color.RED);
		// g2.draw(new Rectangle2D.Double(0., 0., diagWidth, 1.5*diagHeight));

		GradientPaint skyGradient = new GradientPaint((float) (diagWidth / 2.), 0.f, new Color(0.0f, 0.0f, 0.6f),
				(float) ((2. * diagWidth) / 3.), (float) (diagHeight), Color.BLACK, false);
		g2.setPaint(skyGradient);
		g2.fill(new Rectangle2D.Double(0., 0., diagWidth, 1.5 * diagHeight));

		// shift drawing in order to be centered after it has been rotated
		g2.translate(paddingW, paddingH);
		g2.rotate(isometricAngle, boardWidth / 2., boardHeight / 2.);

		// draw world and lights
		drawWorld3D(g2);
		
		g2.setTransform(originalTransform);		
	}

	public static int computeZOrder(GridWorldCell cell) {
		final int tx = cell.getWorld().getWidth();
		final int ty = cell.getWorld().getHeight();

		final int x = cell.getY();
		final int y = cell.getX();

		int val = (x + y) * (x + y + 1) / 2 + x + 1;
		if (x + y > ty - 1)
			val -= ((x + y - ty + 1) * (x + y - ty + 2)) / 2;
		if (x + y > tx)
			val -= ((x + y - tx) * (x + y - tx + 1)) / 2;

		return val;
	}

	private synchronized void drawWorld3D(Graphics2D g) {
		LightBotEntity bot = null;
		if (world.getEntityCount() > 0)
			bot = (LightBotEntity) world.getEntity(0);

		for (LightBotWorldCell cell : this.cellsZOrdered) {
			int x = cell.getX();
			int y = cell.getY();

			Color cellColor = Color.white;
			if ((x + y) % 2 == 0)
				cellColor = DARK_CELL_COLOR;
			else
				cellColor = LIGHT_CELL_COLOR;
			g.setColor(cellColor);

			double cw = CELL_WIDTH;
			double dx = x * cw;
			double dy = y * cw;
			double dz = cell.getHeight() * CELL_HEIGHT;

			GeneralPath face1 = new GeneralPath();
			face1.moveTo(dx, dy + cw);
			face1.lineTo(dx - dz, dy + cw - dz);
			face1.lineTo(dx + cw - dz, dy + cw - dz);
			face1.lineTo(dx + cw, dy + cw);
			face1.lineTo(dx, dy + cw);

			GeneralPath face2 = new GeneralPath();
			face2.moveTo(dx + cw, dy + cw);
			face2.lineTo(dx + cw - dz, dy + cw - dz);
			face2.lineTo(dx + cw - dz, dy - dz);
			face2.lineTo(dx + cw, dy);
			face2.lineTo(dx + cw, dy + cw);

			// floor
			g.setColor(GRID_COLOR);
			g.draw(new Rectangle2D.Double(dx, dy, cw, cw));

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

			// steps on faces
			for (int h = 0; h<cell.getHeight(); h++) {
				g.setColor(GRID_COLOR);
				double z = h * CELL_HEIGHT;
				g.draw(new Line2D.Double(dx - z, dy + cw - z, dx + cw - z, dy + cw - z));
				g.draw(new Line2D.Double(dx + cw -z , dy + cw - z, dx + cw -z , dy - z));
			}
	
			// roof
			g.setColor(cellColor);
			g.fill(new Rectangle2D.Double(dx - dz, dy - dz, cw, cw));
			g.setColor(GRID_COLOR);
			g.draw(new Rectangle2D.Double(dx - dz, dy - dz, cw, cw));

			if (cell.isLight()) {
				drawLight2D(g, cell);
			}

			// print elevation
			// g.setColor(Color.RED);
			// g.drawString(Integer.toString(cell.getHeight()), (int) (dx - dz),
			// (int) (dy + cw - ch));

			// print z-order
			// g.setColor(Color.BLUE);
			// g.drawString(Integer.toString(computeZOrder(cell)), (int) (dx +
			// cw - cw / 3 - dz), (int) (dy + cw - dz));

			// print cell coord
			//g.setColor(Color.BLACK);
			//g.drawString(("" + x + ":" + y), (int) (dx + cw - cw / 2. - dz),
			//(int) (dy + cw / 2. - dz));

			if (bot != null && cell == bot.getCell()) {
				drawBot2D(g, bot);
			}
		}

	}

	private void drawLight2D(Graphics2D g, LightBotWorldCell cell) {
		final double dx = cell.getX() * CELL_WIDTH;
		final double dy = cell.getY() * CELL_WIDTH;
		final double dz = cell.getHeight() * CELL_HEIGHT;

		g.setColor(cell.isLightOn()?LIGHT_ON_COLOR:LIGHT_OFF_COLOR);
		g.fill(new Arc2D.Double(dx + 0.1 * CELL_WIDTH - dz, dy + 0.1 * CELL_WIDTH - dz, 0.8 * CELL_WIDTH,
				0.8 * CELL_WIDTH, 0, 360, Arc2D.OPEN));
	}

	private void drawBot2D(Graphics2D g, LightBotEntity bot) {
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

		double rx = cx * CELL_WIDTH - cell.getHeight() * CELL_HEIGHT + width / 2.;
		double ry = cy * CELL_WIDTH - cell.getHeight() * CELL_HEIGHT + height / 2.;
		g.rotate(angle, rx, ry);

		g.setColor(BOT_COLOR);
		g.fill(new Arc2D.Double((cx - 0.25) * CELL_WIDTH - cell.getHeight() * CELL_HEIGHT, (cy + 0.1) * CELL_WIDTH
				- cell.getHeight() * CELL_HEIGHT, 1.5 * width, 1.5 * height, 60, 60, Arc2D.PIE));

		g.rotate(-angle, rx, ry);

	}

	@Override
	public void worldHasMoved() {
		computeZOrders();
		super.worldHasMoved();
	}	
	
	public void initComponents() {
		JButton rotateLeft = new JButton("left");
		rotateLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				for (World w : Game.getInstance().getSelectedWorlds())
					((LightBotWorld) w).rotateLeft();
			}
		});
	
		JButton rotateRight = new JButton("right");
		rotateRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				for (World w : Game.getInstance().getSelectedWorlds())
					((LightBotWorld) w).rotateRight();
			}
		});
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(1,2));
		buttonsPanel.add(rotateLeft);
		buttonsPanel.add(rotateRight);
		
		setLayout(new BorderLayout());
		add(BorderLayout.NORTH, buttonsPanel);
	}
	
	@Override
	public ImageIcon getIcon() {
		return ResourcesCache.getIcon("resources/IconWorld/lightbot.png");
	}
	
}

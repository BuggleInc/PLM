package plm.universe.bugglequest.mapeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import plm.universe.Direction;
import plm.universe.Entity;
import plm.universe.bugglequest.Buggle;
import plm.universe.bugglequest.BuggleWorldCell;
import plm.universe.bugglequest.exception.AlreadyHaveBaggleException;
import plm.universe.bugglequest.ui.BuggleWorldView;


public class MapView extends BuggleWorldView implements EditionListener {

	private static final long serialVersionUID = -8303474674104829723L;
	private Editor editor;
	private int buggleCount = 0;
	
	public MapView(Editor e) {
		super(e.getWorld());
		this.editor = e;

		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouseEvt(e);
			}
			void handleMouseEvt(MouseEvent e) {
				int x = (int) ((e.getX() - getPadX()) / getCellWidth());
				int y = (int) ((e.getY() - getPadY()) / getCellWidth());
				
				editor.setSelectedCell(x, y);
				BuggleWorldCell cell = (BuggleWorldCell) editor.getWorld().getSelectedCell();
				String cmd = editor.getCommand();

				if (cmd.equals("topwall")) {
					if (cell.hasTopWall()) 
						cell.removeTopWall();
					else
						cell.putTopWall();
				} else if (cmd.equals("leftwall")) {
					if (cell.hasLeftWall()) 
						cell.removeLeftWall();
					else
						cell.putLeftWall();
				} else if (cmd.equals("baggle")) {
					if (cell.hasBaggle()) 
						cell.pickupBaggle();
					else
						try {
							cell.dropBaggle();
						} catch (AlreadyHaveBaggleException e1) {
							System.out.println("The impossible did happen (yet again)");
							e1.printStackTrace();
						}
				} else if (cmd.equals("colors")) {
					if (cell.getColor().equals(editor.getSelectedColor())) 
						cell.setColor(Color.white);
					else
						cell.setColor(editor.getSelectedColor());
				} else if (cmd.equals("text")) {
					String inputValue = (String)JOptionPane.showInputDialog(null,
							"Choose a new message", "Change message",
							JOptionPane.QUESTION_MESSAGE, null,
							null,cell.getContent());
					
					if (inputValue == null) // cancel pressed
						return;
					cell.emptyContent();
					cell.addContent(inputValue);
					
				} else if (cmd.equals("buggle")) {
					Buggle thebuggle = null;
					for (Entity ent:editor.getWorld().getEntities()) {
						Buggle b = (Buggle) ent;
						if (b.getX() == x && b.getY() == y) {
							thebuggle = b;
							break;
						}
					}
					if (thebuggle == null) {
						thebuggle = new Buggle(editor.getWorld(),"buggle"+(++buggleCount), x,y,Direction.NORTH,Color.black, Color.black);
					} 
					editor.setSelectedEntity(thebuggle);
				} else if (cmd.equals("nobuggle")) {
					Buggle thebuggle = null;
					for (Entity ent:editor.getWorld().getEntities()) {
						Buggle b = (Buggle) ent;
						if (b.getX() == x && b.getY() == y) {
							thebuggle = b;
							break;
						}
					}
					if (thebuggle != null) {
						editor.getWorld().removeEntity(thebuggle);
						if (editor.getWorld().getEntities().contains(thebuggle))
							System.err.println("The entity is still in "+editor.getWorld().getEntities().indexOf(thebuggle)+"!!");

					}
					editor.setSelectedEntity(null);
				}
			}
		};
		addMouseListener(mouseListener);
	}

	@Override
	public void worldEdited() {
		worldHasChanged();// use the callbacks of the regular view -- not really clean but efficient
	}

	@Override
	public void selectedChanged(int x, int y, Entity ent) {
		/* I'm too lazy to react to this */
		this.repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		BuggleWorldCell selection = editor.getWorld().getSelectedCell();
		if (selection != null) {
			int padx = (int) getPadX();
			int pady = (int) getPadY();
			int ox = (int) (selection.getX()*getCellWidth()); // x-offset of the cell
			int oy = (int) ((selection.getY())*getCellWidth()); // y-offset of the cell
			int cellW = (int) getCellWidth();

			g.setColor(Color.RED);
			g.drawRoundRect(padx+ox+2, pady+oy+2, cellW-4,cellW-4, cellW/10, cellW/10);
			g.setColor(Color.WHITE);
			g.drawRoundRect(padx+ox+3, pady+oy+3, cellW-6,cellW-6, cellW/10, cellW/10);
			g.setColor(Color.RED);
			g.drawRoundRect(padx+ox+4, pady+oy+4, cellW-8,cellW-8, cellW/10, cellW/10);
		}
	}
}

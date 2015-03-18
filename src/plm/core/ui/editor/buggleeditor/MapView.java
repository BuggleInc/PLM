package plm.core.ui.editor.buggleeditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JOptionPane;

import plm.universe.Direction;
import plm.universe.Entity;
import plm.universe.bugglequest.Buggle;
import plm.universe.bugglequest.BuggleWorld;
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
						cell.baggleRemove();
					else
						try {
							cell.baggleAdd();
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
					
					
				} else if (cmd.equals("linedel")) {
					int option = JOptionPane.showConfirmDialog(null,
									"Do you really want to delete line "+y+"? (no undo possible)", "Are you sure?",
									JOptionPane.ERROR_MESSAGE);
					if (option == JOptionPane.OK_OPTION) {
						BuggleWorld w = editor.getWorld();
						for (int yiter=y; yiter<w.getHeight() - 1;yiter++) {
							for (int xiter=0;xiter<w.getWidth() ;xiter++) {
								w.setCell(w.getCell(xiter, yiter+1), xiter, yiter);
								w.getCell(xiter, yiter).setY(yiter);
							}
						}
						w.setHeight(w.getHeight()-1);
						
						Vector<Buggle> toRemove = new Vector<Buggle>();
						for (Entity ent:editor.getWorld().getEntities()) {
							Buggle b = (Buggle) ent;
							if (b.getY()==y)
								toRemove.add(b); // Don't remove right away to avoid concurrent modification of traversed collection
							if (b.getY()>y) {
								b.setY(b.getY()-1);
							}
						}
						for (Buggle b:toRemove)
							editor.getWorld().removeEntity(b);
					}
				} else if (cmd.equals("coldel")) {
					int option = JOptionPane.showConfirmDialog(null,
							"Do you really want to delete column "+x+"? (no undo possible)", "Are you sure?",
							JOptionPane.ERROR_MESSAGE);
					if (option == JOptionPane.OK_OPTION) {
						BuggleWorld w = editor.getWorld();
						for (int xiter=x;xiter<w.getWidth() - 1;xiter++) {
							for (int yiter=0; yiter<w.getHeight();yiter++) {
								w.setCell(w.getCell(xiter+1, yiter), xiter, yiter);
								w.getCell(xiter, yiter).setX(xiter);
							}
						}
						w.setWidth(w.getWidth()-1);

						Vector<Buggle> toRemove = new Vector<Buggle>();
						for (Entity ent:editor.getWorld().getEntities()) {
							Buggle b = (Buggle) ent;
							if (b.getX()==x)
								toRemove.add(b); // Don't remove right away to avoid concurrent modification of traversed collection
							if (b.getX()>x) {
								b.setX(b.getX()-1);
							}
						}
						for (Buggle b:toRemove)
							editor.getWorld().removeEntity(b);
					}
				} else if (cmd.equals("lineadd")) {
					String[] choices = {"Above", "Below","Cancel"};

					int option = JOptionPane.showOptionDialog(null, 
							"Add a line above or below the line "+y+"?", "Above or below?", 0,
							JOptionPane.INFORMATION_MESSAGE, null, choices, choices[0]);
					switch (option) {
					case 0: break;
					case 1: y++;break;
					case 2: return;
					}
					
					BuggleWorld w = editor.getWorld();
					w.setHeight(w.getHeight()+1);
					for (int yiter=w.getHeight() - 1; yiter>y;yiter--) {
						for (int xiter=0;xiter<w.getWidth() ;xiter++) {
							w.setCell(w.getCell(xiter, yiter-1), xiter, yiter);
							w.getCell(xiter, yiter).setY(yiter);
						}
					}
					for (int xiter=0;xiter<w.getWidth() ;xiter++) 
						w.setCell(w.newCell(xiter,y),xiter,y);
					for (Entity ent:editor.getWorld().getEntities()) {
						Buggle b = (Buggle) ent;
						if (b.getY()>=y) 
							b.setY(b.getY()+1);
					}	
				} else if (cmd.equals("coladd")) {
					String[] choices = {"Left", "Right","Cancel"};

					int option = JOptionPane.showOptionDialog(null, 
							"Add a column left or right of column "+x+"?", "Left or Right?", 0,
							JOptionPane.INFORMATION_MESSAGE, null, choices, choices[0]);
					switch (option) {
					case 0: break;
					case 1: x++;break;
					case 2: return;
					}
					
					BuggleWorld w = editor.getWorld();
					w.setWidth(w.getWidth()+1);
					for (int xiter=w.getWidth()-1;xiter>x ;xiter--) {
						for (int yiter=0; yiter<w.getHeight();yiter++) {
							w.setCell(w.getCell(xiter-1, yiter), xiter, yiter);
							w.getCell(xiter, yiter).setX(xiter);
						}
					}
					for (int yiter=0;yiter<w.getHeight() ;yiter++) 
						w.setCell(w.newCell(x,yiter),x,yiter);
					for (Entity ent:editor.getWorld().getEntities()) {
						Buggle b = (Buggle) ent;
						if (b.getX()>=x) 
							b.setX(b.getX()+1);
					}	
				} else {
					System.out.println("Unhandled command: "+cmd+". Please fix that bug.");
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

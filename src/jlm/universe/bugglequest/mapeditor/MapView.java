package jlm.universe.bugglequest.mapeditor;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import jlm.universe.Direction;
import jlm.universe.bugglequest.BuggleWorldCell;
import jlm.universe.bugglequest.exception.AlreadyHaveBaggleException;
import jlm.universe.bugglequest.ui.BuggleWorldView;




public class MapView extends BuggleWorldView {

	private static final long serialVersionUID = -8303474674104829723L;
	private Editor editor;

	public MapView(Editor e) {
		super(e.getWorld());
		this.editor = e;

		MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//				handleMouseEvt(e);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouseEvt(e);
			}
			void handleMouseEvt(MouseEvent e) {
				int x = (int) ((e.getX() - getPadX()) / getCellWidth());
				int y = (int) ((e.getY() - getPadY()) / getCellWidth());

				BuggleWorldCell cell = (BuggleWorldCell) editor.getWorld().getCell(x, y);
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
						cell.pickUpBaggle();
					else
						try {
							cell.newBaggle();
						} catch (AlreadyHaveBaggleException e1) {
							e1.printStackTrace();
						}
				} else if (cmd.equals("buggle")) {
					if (cell.hasBuggle())
						cell.removeBuggle();
					else
					{
						String name = null;
						Direction orientation = null;
						Color color = null;
						Color brush = null;
						
						editor.setCommand("buggle");
						
						while (name == null || name.length() == 0)
						{
							name = JOptionPane.showInputDialog("Enter a name for the buggle");
							if (name == null)
								return;
						}	
							
						Object[] choicesOrientation = {"north", "east", "south", "west"};
						Direction[] orientations = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
						
						Object selectedValue = JOptionPane.showInputDialog(null,
								"Choose an orientation for buggle", "Change orientation",
								JOptionPane.INFORMATION_MESSAGE, null,
								choicesOrientation, choicesOrientation[0]);
						
						if (selectedValue == null) // cancel pressed
							return;
						
						orientation = Direction.getDirectionFromString((String) selectedValue);
						
						Object[] choices = {
								"black","blue","cyan","darkGray","gray","green","lightGray","magenta","orange","pink","red","yellow"};
						Color[] colors = {
								Color.black,Color.blue,Color.cyan,Color.darkGray,Color.gray,Color.green,Color.lightGray,Color.magenta,Color.orange,Color.pink,Color.red,Color.yellow};

						selectedValue = JOptionPane.showInputDialog(null,
								"Choose a color for buggle", "Change color",
								JOptionPane.INFORMATION_MESSAGE, null,
								choices, choices[0]);
						
						if (selectedValue == null) // cancel pressed
							return;
							
						for (int i=0; i<choices.length;i++) 
							if (selectedValue.equals(choices[i])) {
								color = colors[i];
							}
						
						selectedValue = JOptionPane.showInputDialog(null,
								"Choose a color for brush", "Change color",
								JOptionPane.INFORMATION_MESSAGE, null,
								choices, choices[0]);
						
						if (selectedValue == null) // cancel pressed
							return;
							
						for (int i=0; i<choices.length;i++) 
							if (selectedValue.equals(choices[i])) {
								brush = colors[i];
							}
						cell.putBuggle(name, orientation, color, brush);
					}
					editor.notifyMapViews();
					
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
				}
			}
		};
		addMouseListener(mouseListener);
	}
}

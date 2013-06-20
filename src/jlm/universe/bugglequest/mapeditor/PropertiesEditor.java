package jlm.universe.bugglequest.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import jlm.universe.Entity;
import jlm.universe.World;
import jlm.universe.bugglequest.AbstractBuggle;
import jlm.universe.bugglequest.BuggleWorld;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class PropertiesEditor extends JComponent implements EditionListener {
	private static final long serialVersionUID = 3904327915735497696L;
	private I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);
	
	private AbstractBuggle selectedBuggle;

	private DefaultTableModel model = new DefaultTableModel() {
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int rowIndex, int colIndex) {
			return colIndex>0;
		}
	};
	private JTable table = new JTable(model);

	private Editor editor; 
	private int selectedXRank,selectedYRank;

	Vector<JLMProperty> properties = new Vector<JLMProperty>();
	
	public PropertiesEditor(Editor _editor) {
		editor = _editor;
		editor.addEditionListener(this);

		setLayout(new BorderLayout());
		add(new JScrollPane(table), BorderLayout.CENTER);

		model.setColumnCount(2);
		model.setColumnIdentifiers(new Object[] {i18n.tr("Property"), i18n.tr("Value")});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		repopulateTable();
		setVisible(true);
		setPreferredSize(new Dimension(100, 500));

		model.addTableModelListener(new MyTableModelListener(editor,table,properties));
	}
	private void repopulateTable() {
		while (model.getRowCount()>0)
			model.removeRow(0);
		properties.removeAllElements();

		/* The editor for the name */
		model.insertRow(0, new Object[] {i18n.tr("World name"), new JLMProperty(properties) { 
			@Override
			public boolean setValue(String value) {
				editor.getWorld().setName(value);
				return true;
			}
			@Override
			public String toString() {
				return editor.getWorld().getName();
			}
		}});

		/*---------- world width ---------------*/
		model.addRow(new Object[] {i18n.tr("World width"), new JLMProperty(properties) {
			@Override
			public String toString() {
				return ""+editor.getWorld().getWidth();
			}
			@Override
			public boolean setValue(String value) {
				Integer i;
				try {
					i = Integer.parseInt(value);
				} catch (NumberFormatException nfe) {
					table.setValueAt(""+editor.getWorld().getWidth(),rank,1);
					return false; // silently ignore invalid values
				}
				editor.getWorld().setWidth(i);
				return true;
			}
		}});
		
		/*---------- world height ---------------*/
		model.addRow(new Object[] {i18n.tr("World height"), new JLMProperty(properties) {
			@Override
			public String toString() {
				return ""+editor.getWorld().getHeight();
			}
			@Override
			public boolean setValue(String value) {
				Integer i;
				try {
					i = Integer.parseInt(value);
				} catch (NumberFormatException nfe) {
					table.setValueAt(""+editor.getWorld().getHeight(),rank,1);
					return false; // silently ignore invalid values
				}
				editor.getWorld().setHeight(i);
				return true;
			}
		}});

		/*---------- selected cell ---------------*/
		model.addRow(new Object[] {i18n.tr("Selected cell X"), new JLMProperty(properties) {
			@Override
			public String toString() {
				selectedXRank = rank;
				return ""+editor.getWorld().getSelectedCell().getX();
			}
			@Override
			public boolean setValue(String value) {
				Integer x;
				try {
					x = Integer.parseInt(value);
					if (x>=editor.getWorld().getWidth() || x<0)
						throw new NumberFormatException("out of world");
				} catch (NumberFormatException nfe) {
					table.setValueAt(""+editor.getWorld().getSelectedCell().getX(),rank,1);
					return false; // silently ignore invalid values
				}
				editor.getWorld().setSelectedCell(x, editor.getWorld().getSelectedCell().getY());
				return true;
			}
		}});

		/*---------- selected cell ---------------*/
		model.addRow(new Object[] {i18n.tr("Selected cell Y"), new JLMProperty(properties) {
			@Override
			public String toString() {
				selectedYRank = rank;
				return ""+editor.getWorld().getSelectedCell().getY();
			}
			@Override
			public boolean setValue(String value) {
				Integer y;
				try {
					y = Integer.parseInt(value);
					if (y>=editor.getWorld().getHeight() || y<0)
						throw new NumberFormatException("out of world");
				} catch (NumberFormatException nfe) {
					table.setValueAt(""+editor.getWorld().getSelectedCell().getY(),rank,1);
					return false; // silently ignore invalid values
				}
				editor.getWorld().setSelectedCell(editor.getWorld().getSelectedCell().getX(),y);
				return true;
			}
		}});
		
	}
	@Override
	public void setWorld(World w) {
		if (((BuggleWorld) w).getSelectedCell() == null)
			((BuggleWorld) w).setSelectedCell(0,0);
		repopulateTable();		
		
	}
	@Override
	public void worldEdited() {
		// TODO Auto-generated method stub

	}
	@Override
	public void selectedChanged(int x, int y, Entity ent) {
		table.setValueAt(""+x,selectedXRank,1);
		table.setValueAt(""+y,selectedYRank,1);
		
		if (selectedBuggle != ent) {
			selectedBuggle = (AbstractBuggle) ent;
			repopulateTable();
		} else {
			
		}
	}
}

class MyTableModelListener implements TableModelListener {
	private JTable table;
	private Vector<JLMProperty> properties;
	private Editor editor;
	
	MyTableModelListener(Editor e, JTable t, Vector<JLMProperty> props) {
		editor = e;
		table = t;
		properties = props;
	}
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow(); // selections are SINGLE_SELECTION anyway, so ignore getLastRow

		if (e.getType() == TableModelEvent.UPDATE) {
			for (JLMProperty p : properties) {
				if (p.rank == row) {
					if (p.setValue((String) table.getModel().getValueAt(row, 1)))
						editor.getWorld().notifyWorldUpdatesListeners();
					return;
				}
			}
			System.out.println("No property seem to be in charge of row "+row+". Ignoring the edit.");
		}
	}
}

abstract class JLMProperty {
	public int rank;
	public JLMProperty(Vector<JLMProperty> props) {
		rank = props.size();
		props.add(this);
	}
	public abstract boolean setValue(String value);
	public abstract String toString();
}

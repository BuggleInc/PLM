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

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class PropertiesEditor extends JComponent implements EditionListener {
	private static final long serialVersionUID = 3904327915735497696L;
	private I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	private DefaultTableModel model = new DefaultTableModel() {
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int rowIndex, int colIndex) {
			return colIndex>0;
		}
	};
	private JTable table = new JTable(model);

	private Editor editor; 

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
	}
	@Override
	public void setWorld(World w) {
		repopulateTable();		
	}
	@Override
	public void worldEdited() {
		// TODO Auto-generated method stub

	}
	@Override
	public void setSelectedEntity(Entity ent) {
		// TODO Auto-generated method stub

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
		int row = e.getFirstRow(); // selections are SINGLE_SELECTION, so ignore getLastRow

		if (e.getType() == TableModelEvent.UPDATE) {
			for (JLMProperty p : properties) {
				if (p.rank == row) {
					System.out.println("Property "+row+" edited. Delegation ongoing.");
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

package jlm.universe.bugglequest.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;

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

		model.addTableModelListener(new MyTableModelListener(table,editor));
	}
	private void repopulateTable() {
		while (model.getRowCount()>0)
			model.removeRow(0);

		model.addRow(new Object[] {i18n.tr("World name:"), editor.getWorld().getName()});
		model.addRow(new Object[] {i18n.tr("World width:"), editor.getWorld().getWidth()});
		model.addRow(new Object[] {i18n.tr("World height:"), editor.getWorld().getHeight()});
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
	private Editor editor;

	MyTableModelListener(JTable t, Editor e) {
		table = t;
		editor = e;
	}
	public void tableChanged(TableModelEvent e) {
		int row = e.getFirstRow(); // selections are SINGLE_SELECTION, so ignore getLastRow

		if (e.getType() == TableModelEvent.UPDATE)
			switch (row) {
			case 0:
				editor.getWorld().setName((String) table.getModel().getValueAt(0, 1));
				break;
			}
	}
}
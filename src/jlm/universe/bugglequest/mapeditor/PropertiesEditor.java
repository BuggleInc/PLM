package jlm.universe.bugglequest.mapeditor;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import jlm.universe.Entity;
import jlm.universe.World;
import net.miginfocom.swing.MigLayout;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class PropertiesEditor extends JComponent implements EditionListener {
	private static final long serialVersionUID = 3904327915735497696L;
	private I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	private JLabel jlWorldName = new JLabel(i18n.tr("World name:"));
	private JTextField jtWorldName = new JTextField();
	
	private Editor editor; 
	public PropertiesEditor(Editor _editor) {
		editor = _editor;
		editor.addEditionListener(this);
		setLayout(new MigLayout());
		
	}
	@Override
	public void setWorld(World w) {
		// TODO Auto-generated method stub
		
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

package plm.core.ui;

import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;
import plm.core.GameListener;
import plm.core.model.Game;
import plm.universe.World;

public abstract class AbstractAboutDialog extends JFrame implements GameListener {

	private static final long serialVersionUID = -6550658679688214378L;
	protected JEditorPane area = new JEditorPane("text/html","");
	
	protected AbstractAboutDialog(JFrame parent) {
		super();
		Game.getInstance().addGameListener(this);
		
		setResizable(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(getParent());
		
		setMinimumSize(new Dimension(600,400));
		
		area.setEditorKit(new PlmHtmlEditorKit());
		area.setEditable(false);
		
		setLayout(new MigLayout("fill"));
		add(new JScrollPane(area),"grow");
	}
		
	@Override
	public void selectedEntityHasChanged()             { /* I dont care I'm a punk */ }
	@Override
	public void selectedWorldHasChanged(World w)       { /* I dont care I'm a punk */ }
	@Override
	public void selectedWorldWasUpdated()              { /* I dont care I'm a punk */ }
}

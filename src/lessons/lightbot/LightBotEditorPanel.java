package lessons.lightbot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import jlm.core.Game;
import jlm.lesson.ISourceFileListener;
import jlm.ui.IEditorPanel;
import jlm.universe.Entity;
import jlm.universe.IEntityTracable;
import jlm.universe.IEntityTraceListener;
import net.miginfocom.swing.MigLayout;

public class LightBotEditorPanel extends JScrollPane implements IEditorPanel,ISourceFileListener,IEntityTraceListener {
	private static final long serialVersionUID = 1L;
	LightBotSourceFile srcFile;
	Map<String,InstructionChooser> choosers=new HashMap<String, InstructionChooser>();
	InstructionChooser selectedChooser = null;
	IEntityTracable tracedEntity;

	public LightBotEditorPanel(LightBotSourceFile srcFile) {
		super();
		this.srcFile = srcFile;
		srcFile.setListener(this);
		sourceFileContentHasChanged();
		Entity e = Game.getInstance().getSelectedEntity();
		if (e instanceof IEntityTracable) {
			tracedEntity = (IEntityTracable) e;
			tracedEntity.addTraceListener(this);
		}
	}
	@Override
	public void clear() {
		srcFile.removeListener();
	}
	@Override
	public void sourceFileContentHasChanged() {

		/* Main function */
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createTitledBorder("Main"));
		mainPanel.setLayout(new MigLayout("wrap 4, fill"));
		for (int i=0;i<srcFile.main.length;i++) {
			InstructionChooser chooser = new InstructionChooser(srcFile.main,i);
			choosers.put("main:"+i, chooser);
			mainPanel.add(chooser, "grow");
		}

		/* Func 1 */
		JPanel func1Panel = new JPanel();
		func1Panel.setBorder(BorderFactory.createTitledBorder("Function 1"));
		func1Panel.setLayout(new MigLayout("wrap 4, fill"));
		for (int i=0;i<srcFile.func1.length;i++) { 
			InstructionChooser chooser = new InstructionChooser(srcFile.func1,i);
			choosers.put("func1:"+i, chooser);
			func1Panel.add(chooser, "grow");
		}

		/* Func 2 */
		JPanel func2Panel = new JPanel();
		func2Panel.setBorder(BorderFactory.createTitledBorder("Function 2"));
		func2Panel.setLayout(new MigLayout("wrap 4, fill"));
		for (int i=0;i<srcFile.func2.length;i++) {
			InstructionChooser chooser = new InstructionChooser(srcFile.func2,i); 
			choosers.put("func2:"+i, chooser);
			func2Panel.add(chooser, "grow");
		}
		for (InstructionChooser ic:choosers.values()) 
			ic.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		/* Put everything together */
		JPanel global = new JPanel();
		global.setLayout(new MigLayout("wrap 1, fill"));
		global.add(mainPanel,"grow");
		global.add(func1Panel,"grow");
		global.add(func2Panel,"grow");
		setViewportView(global);
		doLayout();
	}

	private class InstructionChooser extends JComboBox {
		LightBotInstruction[] func;
		int pos;

		public InstructionChooser(final LightBotInstruction[] func, final int pos) {
			super(LightBotInstruction.instructionNames);
			this.func = func;
			this.pos = pos;
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					func[pos] = new LightBotInstruction((String) InstructionChooser.this.getSelectedItem());
				}				
			});
			setSelectedItem(func[pos].toString());
		}

		private static final long serialVersionUID = 7308818147531552628L;

	}

	@Override
	public void entityTraceChanged(Entity e, String location) {
		if (selectedChooser != null)
			selectedChooser.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		if (location != null) {
			selectedChooser = choosers.get(location);
			if (selectedChooser!=null)
				selectedChooser.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		}
	}

	@Override
	public void tracedEntityChanged(Entity e) {
		if (tracedEntity != null)
			tracedEntity.removeTraceListener(this);
		if (e instanceof IEntityTracable) {
			tracedEntity = (IEntityTracable) e;
			tracedEntity.addTraceListener(this);
			entityTraceChanged(e, tracedEntity.getCurrentTrace());
		}
		else {
			System.out.println("Not a tracable entity");
			if (selectedChooser != null)
				selectedChooser.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		}
	}
}

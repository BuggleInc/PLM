package lessons.lightbot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jlm.lesson.ISourceFileListener;
import jlm.ui.IEditorPanel;
import net.miginfocom.swing.MigLayout;

public class LightBotEditorPanel extends JScrollPane implements IEditorPanel,ISourceFileListener {
	private static final long serialVersionUID = 1L;
	LightBotSourceFile srcFile;
	
	
	public LightBotEditorPanel(LightBotSourceFile srcFile) {
		super();
		this.srcFile = srcFile;
		srcFile.setListener(this);
		sourceFileContentHasChanged();
	}
	@Override
	public void clear() {
	}
	@Override
	public void sourceFileContentHasChanged() {
		
		/* Main function */
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(BorderFactory.createTitledBorder("Main"));
		mainPanel.setLayout(new MigLayout("wrap 4, fill"));
		for (int i=0;i<srcFile.main.length;i++) 
			mainPanel.add(new InstructionChooser(srcFile.main,i), "grow");
		
		/* Func 1 */
		JPanel func1Panel = new JPanel();
		func1Panel.setBorder(BorderFactory.createTitledBorder("Function 1"));
		func1Panel.setLayout(new MigLayout("wrap 4, fill"));
		for (int i=0;i<srcFile.func1.length;i++) 
			func1Panel.add(new InstructionChooser(srcFile.func1,i), "grow");
		
		/* Func 2 */
		JPanel func2Panel = new JPanel();
		func2Panel.setBorder(BorderFactory.createTitledBorder("Function 2"));
		func2Panel.setLayout(new MigLayout("wrap 4, fill"));
		for (int i=0;i<srcFile.func2.length;i++) 
			func2Panel.add(new InstructionChooser(srcFile.func2,i), "grow");

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

}

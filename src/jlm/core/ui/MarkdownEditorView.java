package jlm.core.ui;

/**
 * Markdown Editor (view).
 * Use MarkdownDocument class as a model.
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.petebevin.markdown.MarkdownProcessor;

public class MarkdownEditorView extends JPanel implements Observer
{
	private static final long serialVersionUID = 1L;
	MarkdownDocument document;
	JTextArea editor;
	JTextPane view;
	JButton shortcuts[];
	MarkdownProcessor markdownProcessor;
	
	public MarkdownEditorView(final MarkdownDocument document)
	{
		this.document = document;
		document.addObserver(this);
		markdownProcessor = new MarkdownProcessor();
		
		setLayout(new BorderLayout());
		
		JPanel panel_shortcuts = new JPanel();
		panel_shortcuts.setLayout(new BoxLayout(panel_shortcuts,BoxLayout.LINE_AXIS));
		int n = 1;
		shortcuts = new JButton[n];
		for(int i=0;i<n;i++){
			shortcuts[i] = new JButton();
			//shortcuts[i].setSize(20, 20);
			panel_shortcuts.add(shortcuts[i]);
		}
		shortcuts[0].setText("Save");
		shortcuts[0].addActionListener(new SaveListener(document));
		/*shortcuts[1].setText("h2");
		shortcuts[2].setText("h3");
		shortcuts[3].setText("b");
		shortcuts[4].setText("i");
		shortcuts[5].setText("code");
		shortcuts[6].setText("pre");
		shortcuts[7].setText("• liste");*/
		add(panel_shortcuts, BorderLayout.NORTH);
		
		editor = new JTextArea(10, 20);
		JScrollPane wiki_area_pane = new JScrollPane(editor);
		editor.setEditable(true);
		editor.setText(document.getText());
		wiki_area_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		view = new JTextPane();
		view.setEditorKit(new JlmHtmlEditorKit());
		view.setText(markdownProcessor.markdown(this.document.getText()));
		view.setEditable(false);
        JScrollPane preview_scroll_pane = new JScrollPane(view);
        preview_scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,wiki_area_pane, preview_scroll_pane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.5);
		add(splitPane, BorderLayout.CENTER);
		
		editor.addCaretListener(new CaretListener()
		{
			@Override
			public void caretUpdate(CaretEvent arg0)
			{
				document.update(editor.getText());
			}
		});
	}
	
	@Override
	public void update(Observable arg0, Object arg1)
	{
		MarkdownDocument md = (MarkdownDocument) arg0;
		if(md.isLoad_editor()){editor.setText(md.getText());}
		view.setText(markdownProcessor.markdown(md.getText()));
	}
	
	class SaveListener implements ActionListener
	{
		private MarkdownDocument document;
		
		public SaveListener(MarkdownDocument document)
		{
			this.document = document;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			document.saveMarkDownDocument();
		}
	}
	
	public JTextArea getEditor()
	{
		return editor;
	}

}
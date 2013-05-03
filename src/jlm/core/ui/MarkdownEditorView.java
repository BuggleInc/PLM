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

import jlm.core.model.Game;
import jlm.core.model.ProgrammingLanguage;

import com.petebevin.markdown.MarkdownProcessor;

public class MarkdownEditorView extends JPanel implements Observer, ActionListener
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
		int n = 10;
		shortcuts = new JButton[n];
		for(int i=0;i<n;i++){
			shortcuts[i] = new JButton();
			shortcuts[i].setSize(20, 20);
			panel_shortcuts.add(shortcuts[i]);
		}
		shortcuts[0].setText("Save");
		shortcuts[0].addActionListener(new SaveListener(document));
		shortcuts[1].setText("h1");
		shortcuts[1].addActionListener(this);
		shortcuts[2].setText("h2");
		shortcuts[2].addActionListener(this);
		shortcuts[3].setText("h3");
		shortcuts[3].addActionListener(this);
		shortcuts[4].setText("b");
		shortcuts[4].addActionListener(this);
		shortcuts[5].setText("i");
		shortcuts[5].addActionListener(this);
		shortcuts[6].setText("link");
		shortcuts[6].addActionListener(this);
		shortcuts[7].setText("image");
		shortcuts[7].addActionListener(this);
		shortcuts[8].setText("•");
		shortcuts[8].addActionListener(this);
		shortcuts[9].setText("1.");
		shortcuts[9].addActionListener(this);
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

	@SuppressWarnings("static-access")
	@Override
	public void update(Observable arg0, Object arg1)
	{
		Game game = Game.getInstance();
		MarkdownDocument md = (MarkdownDocument) arg0;
		if(md.isLoad_editor()){editor.setText(md.getText());}
		String res = new String(md.getText());

		for(ProgrammingLanguage l:game.getProgrammingLanguages()){
			if(Global.admin){
				res=res.replaceAll("<"+l.getLang().toLowerCase()+">","["+l.getLang().toLowerCase()+"]");
				res=res.replaceAll("</"+l.getLang().toLowerCase()+">","[/"+l.getLang().toLowerCase()+"]");
			}
			else{
				if(!l.equals(game.getProgrammingLanguage())){
					res=res.replaceAll("<"+l.getLang().toLowerCase()+">.*</"+l.getLang().toLowerCase()+">","");
				}
			}
		}
		view.setText(markdownProcessor.markdown(res));
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton){
			JButton btn = (JButton) e.getSource();
			String s = "";
			int p=0;
			if(btn.getText().equals("h1")){
				p = editor.getCaretPosition();
				s="\n# h1";
			}
			else if(btn.getText().equals("h2")){
				p = editor.getCaretPosition();
				s="\n## h2";
			}
			else if(btn.getText().equals("h3")){
				p = editor.getCaretPosition();
				s="\n### h3";
			}
			else if(btn.getText().equals("b")){
				p = editor.getCaretPosition();
				s="**bold**";
			}
			else if(btn.getText().equals("i")){
				p = editor.getCaretPosition();
				s="*italic*";
			}
			else if(btn.getText().equals("link")){
				p = editor.getCaretPosition();
				s="[link](jlm://lesson/ \"title\")";
			}
			else if(btn.getText().equals("image")){
				p = editor.getCaretPosition();
				s="![alt_text](/path/image.ext \"title\")";
			}
			else if(btn.getText().equals("•")){
				p = editor.getCaretPosition();
				s="\n*  ";
			}
			else if(btn.getText().equals("1.")){
				p = editor.getCaretPosition();
				s="\n1.  ";
			}
			editor.setText(editor.getText().substring(0, p)+s+editor.getText().substring(p, editor.getText().length()));
			document.update(editor.getText());
			editor.setCaretPosition(p);
		}
	}

}
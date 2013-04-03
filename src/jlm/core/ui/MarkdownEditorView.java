package jlm.core.ui;

/**
 * Éditeur de document au format Markdown.
 * Utilise la classe MarkdownDocument.
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.html.HTMLEditorKit;

import com.petebevin.markdown.MarkdownProcessor;

public class MarkdownEditorView extends JPanel implements Observer
{
	private static final long serialVersionUID = 1L;
	MarkdownDocument document;
	JTextArea editeur;
	JTextPane apercu;
	JButton raccourcis[];
	MarkdownProcessor markdownProcessor;
	
	public MarkdownEditorView(final MarkdownDocument document)
	{
		this.document = document;
		document.addObserver(this);
		markdownProcessor = new MarkdownProcessor();
		
		setLayout(new BorderLayout());
		
		Dimension dimension_prefered = new Dimension(100, 100);
		Dimension dimension_minimum = new Dimension(10, 10);
		
		JPanel panelRaccourcis = new JPanel();
		panelRaccourcis.setLayout(new BoxLayout(panelRaccourcis,BoxLayout.LINE_AXIS));
		int n = 1;
		raccourcis = new JButton[n];
		for(int i=0;i<n;i++){
			raccourcis[i] = new JButton();
			//raccourcis[i].setSize(20, 20);
			panelRaccourcis.add(raccourcis[i]);
		}
		raccourcis[0].setText("Sauvegarder");
		raccourcis[0].addActionListener(new SaveListener(document));
		/*raccourcis[1].setText("h2");
		raccourcis[2].setText("h3");
		raccourcis[3].setText("b");
		raccourcis[4].setText("i");
		raccourcis[5].setText("code");
		raccourcis[6].setText("pre");
		raccourcis[7].setText("• liste");*/
		add(panelRaccourcis, BorderLayout.NORTH);
		
		editeur = new JTextArea(10, 20);
		JScrollPane wiki_area_pane = new JScrollPane(editeur);
		editeur.setEditable(true);
		editeur.setText(document.getTexte());
		wiki_area_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		//wiki_area_pane.setPreferredSize(dimension_prefered);
        //wiki_area_pane.setMinimumSize(dimension_minimum);
		
		apercu = new JTextPane();
		apercu.setEditorKit(new JlmHtmlEditorKit());
		apercu.setText(markdownProcessor.markdown(this.document.getTexte()));
		apercu.setEditable(false);
        JScrollPane preview_scroll_pane = new JScrollPane(apercu);
        preview_scroll_pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //preview_scroll_pane.setPreferredSize(dimension_prefered);
        //preview_scroll_pane.setMinimumSize(dimension_minimum);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,wiki_area_pane, preview_scroll_pane);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        splitPane.setResizeWeight(0.5);
		add(splitPane, BorderLayout.CENTER);
		
		editeur.addCaretListener(new CaretListener()
		{
			@Override
			public void caretUpdate(CaretEvent arg0)
			{
				document.maj(editeur.getText());
			}
		});
	}
	
	@Override
	public void update(Observable arg0, Object arg1)
	{
		MarkdownDocument m = (MarkdownDocument) arg0;
		if(m.isLoad_editor()){editeur.setText(m.getTexte());}
		apercu.setText(markdownProcessor.markdown(m.getTexte()));
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
			document.enregistrerDocument();
		}
	}
	
	public JTextArea getEditeur()
	{
		return editeur;
	}

}
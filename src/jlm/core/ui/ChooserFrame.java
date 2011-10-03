package jlm.core.ui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import jlm.core.model.Game;
import jlm.core.model.Reader;
import jlm.core.model.lesson.Lesson;
import net.miginfocom.swing.MigLayout;

public class ChooserFrame extends JFrame{
	private static final long serialVersionUID = 1L;

	public ChooserFrame() {
		super("Java Learning Machine");
		Reader.setLocale(this.getLocale().getLanguage());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		
		initComponents();
	}
	private void initComponents() {
		setLayout(new MigLayout("","[fill]"));
		setBackground(Color.WHITE);		
		JEditorPane aboutJLMPane = new JEditorPane("text/html", "");
		aboutJLMPane.setEditable(false);
		aboutJLMPane.setEditorKit(new JlmHtmlEditorKit());
		aboutJLMPane.setText(Reader.fileToStringBuffer("src/jlm/core/AboutJLM", "html",true).toString());
		aboutJLMPane.setCaretPosition(0);
		
		aboutJLMPane.addHyperlinkListener(new HyperlinkListener() {
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent event) {
				if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					String desc = event.getDescription();
					
					if (desc.startsWith("lesson://")) {
						String lessonName = desc.substring(new String("lesson://").length());
						Lesson lesson = Game.getInstance().loadLesson(lessonName);
						Game.getInstance().setCurrentLesson(lesson);
						
						MainFrame.getInstance().setVisible(true);
						
						 WindowEvent wev = new WindowEvent(ChooserFrame.this, WindowEvent.WINDOW_CLOSING);
						 Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
					}
				}
			}
		});


		add(aboutJLMPane,"span 3");

		setSize(1024, 768);
	}
}

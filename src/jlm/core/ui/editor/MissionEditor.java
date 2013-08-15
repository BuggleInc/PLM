package jlm.core.ui.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import jlm.core.model.Game;
import jlm.core.ui.JlmHtmlEditorKit;
import jlm.core.utils.FileUtils;
import jsyntaxpane.DefaultSyntaxKit;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

public class MissionEditor extends JFrame {
	private static final long serialVersionUID = 1L;

	private I18n i18n = I18nFactory.getI18n(getClass(),"org.jlm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);

	private JEditorPane display = new JEditorPane("text/html", "");
	private JTextPane editor =  new JTextPane();
	
	private String lastPathSelected;
	private boolean modified = false;
	
	private String TITLE_MODIFIED = i18n.tr("JLM - Mission Editor (modified)");
	private String TITLE_NOT_MODIFIED = i18n.tr("JLM - Mission Editor");
	
	public MissionEditor() {
		super();
		setTitle(TITLE_NOT_MODIFIED);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    addWindowListener(new WindowAdapter() {
	    	@Override
	        public void windowOpened(WindowEvent e) {}
	    	@Override
	        public void windowClosing(WindowEvent e) {
	        	confirmQuit();
	        }
	    });
		setSize(1200, 800);
		initComponents();
		DefaultSyntaxKit.initKit();
		setVisible(true);
	}

	private void initComponents() {

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem mi;
		
		/*
		mi = new JMenuItem(new NewMissionAction());
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		fileMenu.add(mi);
		*/
		
		mi = new JMenuItem(new OpenMissionAction());
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(mi);
		
		mi = new JMenuItem(new SaveMissionAction());
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(mi);
		
		mi = new JMenuItem(new SaveAsMissionAction());
		fileMenu.add(mi);

		mi = new JMenuItem(new QuitAction());
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		fileMenu.add(mi);

		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);

		getContentPane().setLayout(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		// TODO: add buttons
		getContentPane().add(toolBar, BorderLayout.NORTH);

		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		sp.setOneTouchExpandable(false);
		sp.setLeftComponent(new JScrollPane(editor));
		sp.setRightComponent(new JScrollPane(display));
		double weight = 0.5;
		sp.setResizeWeight(weight);
		sp.setDividerLocation((int) (((double)getWidth()) * weight));

		getContentPane().add(sp, BorderLayout.CENTER);

		editor.setContentType("text/plain");
		editor.setEditable(true);
		
		display.setEditorKit(new JlmHtmlEditorKit());
		display.setEditable(false);
		editor.getDocument().addDocumentListener(new DocumentListener() {
			private void updateHTML() {
				if (!modified) {
					setTitle(TITLE_MODIFIED);
					modified = true;
				}
				String ctn = "<html><head>"+JlmHtmlEditorKit.getCSS()+"</head><body>"+
						JlmHtmlEditorKit.filterHTML(editor.getText(),true)+
						"</body></html>";
				display.setText(ctn);
				javax.swing.text.Document doc = display.getDocument();
				int pos = (int) ( (double)doc.getLength()*editor.getCaretPosition()/editor.getDocument().getLength());
				if (pos>doc.getLength())
					pos = doc.getLength()-1;
				if (pos<0)
					pos = 0;
				display.setCaretPosition( pos );
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateHTML();
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateHTML();
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateHTML();
			}
		});		
	}
	
	public void loadMission(String path) {
		lastPathSelected = path;
		try {
			BufferedReader reader = FileUtils.newFileReader(path, "html", false);
			StringBuffer content = new StringBuffer();
			String line;
			while (null != (line = reader.readLine())) {
				content.append(line);
				content.append("\n");
			}

			editor.setText(content.toString());
			modified = false;
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, e1.getLocalizedMessage(),i18n.tr("Error while reading {0}",lastPathSelected), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void confirmQuit() {
		if (modified) {
			int choice = JOptionPane.showConfirmDialog(MissionEditor.this, 
					Game.i18n.tr("You did not save you changes. Do you want to save it before quiting?"), 
					Game.i18n.tr("Save or loose your change?"),
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			switch (choice) {
			case JOptionPane.CANCEL_OPTION: 
				return;
			case JOptionPane.YES_OPTION:
				SaveMissionAction act = new SaveMissionAction();
				act.actionPerformed(null);
				break;
			case JOptionPane.NO_OPTION:
				System.out.println("Your changes were lost as you requested.");
			}
		}
		System.exit(0);
	}

	class SaveMissionAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public SaveMissionAction() {
			super("Save");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!modified)
				return;
			
			if (lastPathSelected == null) {
				SaveAsMissionAction act = new SaveAsMissionAction();
				act.actionPerformed(e);
				return;
			}
			File file = new File(lastPathSelected);

			BufferedWriter bw = null;
			FileWriter fw = null;
			try {
				fw = new FileWriter(file);
				bw = new BufferedWriter(fw);
				bw.write(editor.getText());
				bw.close();
				fw.close();
			} catch (IOException ex) {
				// TODO: error dialog
				System.err.println(ex);
			} finally {
				try {
					if (bw != null)
						bw.close();
				} catch (IOException e1) {
					// TODO: error dialog
					e1.printStackTrace();
				}
			}
			setTitle(TITLE_NOT_MODIFIED);
			modified = false;
		}
	}
	class SaveAsMissionAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public SaveAsMissionAction() {
			super("Save As...");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!modified)
				return;
			
			JFileChooser fc;
			if (lastPathSelected != null)
				fc = new JFileChooser(lastPathSelected);
			else
				fc = new JFileChooser();
			int status = fc.showSaveDialog(MissionEditor.this);

			if (status == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();

				BufferedWriter bw = null;
				FileWriter fw = null;
				try {
					fw = new FileWriter(file);
					bw = new BufferedWriter(fw);
					bw.write(editor.getText());
					bw.close();
					fw.close();
				} catch (IOException ex) {
					// TODO: error dialog
					System.err.println(ex);
				} finally {
					try {
						if (bw != null)
							bw.close();
					} catch (IOException e1) {
						// TODO: error dialog
						e1.printStackTrace();
					}
				}
			}
			
			setTitle(TITLE_NOT_MODIFIED);
			modified = false;
		}
	}

	class OpenMissionAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public OpenMissionAction() {
			super(i18n.tr("Open Mission..."));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (modified) {
				int choice = JOptionPane.showConfirmDialog(MissionEditor.this, 
						Game.i18n.tr("You did not save you changes. Do you want to save it before opening another file?"), 
						Game.i18n.tr("Save or loose your change?"),
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);

				switch (choice) {
				case JOptionPane.CANCEL_OPTION: 
					return;
				case JOptionPane.YES_OPTION:
					SaveMissionAction act = new SaveMissionAction();
					act.actionPerformed(e);
					break;
				case JOptionPane.NO_OPTION:
					System.out.println("Your changes were lost as you requested.");
				}
				modified = false;	
			}
			JFileChooser fc = new JFileChooser(lastPathSelected);
			fc.setFileFilter(new FileNameExtensionFilter(i18n.tr("HTML files"), "html"));
			int status = fc.showOpenDialog(MissionEditor.this);

			if (status == JFileChooser.APPROVE_OPTION) {
				lastPathSelected = fc.getSelectedFile().getAbsolutePath();
				loadMission(lastPathSelected);
			}
		}

	}

	class QuitAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public QuitAction() {
			super(i18n.tr("Quit Map Editor"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			confirmQuit();
		}

	}
}

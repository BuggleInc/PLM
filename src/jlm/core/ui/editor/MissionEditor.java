package jlm.core.ui.editor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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
	
	public MissionEditor() {
		super();
		setTitle(i18n.tr("JLM - Mission Editor"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, e1.getLocalizedMessage(),i18n.tr("Error while reading {0}",lastPathSelected), JOptionPane.ERROR_MESSAGE);
		}
	}

	class SaveMissionAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public SaveMissionAction() {
			super("Save");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
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
		}
	}
	class SaveAsMissionAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public SaveAsMissionAction() {
			super("Save As...");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
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
		}
	}

	class OpenMissionAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public OpenMissionAction() {
			super(i18n.tr("Open Mission..."));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
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
			dispose();
		}

	}
}

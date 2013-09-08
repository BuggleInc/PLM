package plm.core.ui.editor.buggleeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import plm.core.ui.ResourcesCache;
import plm.core.utils.ColorMapper;
import plm.core.utils.InvalidColorNameException;


public class MainFrame extends JFrame {

	private static final long serialVersionUID = -7243431953648987489L;

	private Editor editor;
	private ButtonGroup tools;
	private String path;	
	
	private I18n i18n = I18nFactory.getI18n(getClass(),"org.plm.i18n.Messages",getLocale(), I18nFactory.FALLBACK);
	
	public MainFrame(Editor editor) {
		super("BuggleQuest - MapEditor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.editor = editor;
		initComponents();
		setSize(800, 600);
		setVisible(true);
	}

	public void initComponents() {

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		JMenuItem mi;
		
		mi = new JMenuItem(new NewMapAction());
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		fileMenu.add(mi);
		
		mi = new JMenuItem(new OpenMapAction());
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(mi);
		
		mi = new JMenuItem(new SaveMapAction());
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		fileMenu.add(mi);

		mi = new JMenuItem(new QuitAction());
		mi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		fileMenu.add(mi);

		menuBar.add(fileMenu);
		this.setJMenuBar(menuBar);

		getContentPane().setLayout(new BorderLayout());

		JToolBar toolBar = new JToolBar();

		JToggleButton topButton = createButton("topwall"); 
		JToggleButton leftButton = createButton("leftwall"); 
		JToggleButton baggleButton = createButton("baggle");
		JToggleButton buggleButton = createButton("buggle");
		JToggleButton nobuggleButton = createButton("nobuggle");
		JToggleButton textButton = createButton("text");
		JToggleButton colorButton = createButton("colors",new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.setCommand("colors");
				Object[] choices = {
						"custom", "black","blue","cyan","darkGray","gray","green","lightGray","magenta","orange","pink","red","yellow"};
				Color[] colors = {
						new Color(42,42,42), Color.black,Color.blue,Color.cyan,Color.darkGray,Color.gray,Color.green,Color.lightGray,Color.magenta,Color.orange,Color.pink,Color.red,Color.yellow};
				
				Object selectedValue = JOptionPane.showInputDialog(null,
						"Choose a color", "Change color",
						JOptionPane.INFORMATION_MESSAGE, null,
						choices, choices[editor.getSelectedColorNumber()]);
				
				if (selectedValue == null) // cancel pressed
					return;
					
				if (selectedValue.equals("custom")) {
					String name = JOptionPane.showInputDialog("What color you want (r/g/b)", 
							colors[0].getRed()+"/"+colors[0].getGreen()+"/"+colors[0].getBlue());
					try {
						Color c = ColorMapper.name2color(name);
						colors[0] = c;
					} catch (InvalidColorNameException icne) {
						/* Ignore */
					}
				}
				for (int i=0; i<choices.length;i++) 
					if (selectedValue.equals(choices[i])) {
						editor.setSelectedColorNumber(i);
						editor.setSelectedColor(colors[i]);
					}					
			}	
		});

		topButton.setSelected(true);


		toolBar.add(topButton);
		toolBar.add(leftButton);
		toolBar.add(baggleButton);
		toolBar.add(buggleButton);
		toolBar.add(nobuggleButton);
		toolBar.add(colorButton);
		toolBar.add(textButton);

		tools = new ButtonGroup();
		tools.add(topButton);
		tools.add(leftButton);
		tools.add(baggleButton);
		tools.add(buggleButton);
		tools.add(nobuggleButton);
		tools.add(colorButton);
		tools.add(textButton);

		getContentPane().add(toolBar, BorderLayout.NORTH);

		JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		sp.setOneTouchExpandable(false);

		MapView mapView = new MapView(editor);
		editor.addEditionListener(mapView);
		sp.setLeftComponent(mapView);
		sp.setRightComponent(new PropertiesEditor(editor));
		sp.setResizeWeight(.6);
		
		getContentPane().add(sp, BorderLayout.CENTER);
	}

	private JToggleButton createButton(final String name) {
		ActionListener l = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.setCommand(name);
			}	
		};
		return createButton(name,l); 
	}
	private JToggleButton createButton(final String name, ActionListener l) {
		JToggleButton bt = new JToggleButton(ResourcesCache.getIcon("img/edit_"+name+".png"));
		bt.setActionCommand(name);
		bt.setBorderPainted(false);
		bt.addActionListener(l);
		return bt;
	}

	class NewMapAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public NewMapAction() {
			super(i18n.tr("Create New Map"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int w = 10;
			int h = 10;

			String wString = null;
			String hString = null;

			wString = JOptionPane.showInputDialog("Enter desired map width", Integer.toString(w));
			if (wString != null)
				hString = JOptionPane.showInputDialog("Enter desired map height", Integer.toString(h));

			if (hString != null) {
				w = Integer.parseInt(wString);
				h = Integer.parseInt(hString);
				editor.createNewMap(w, h);
				MainFrame.this.path = null;
			}
		}

	}

	class SaveMapAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public SaveMapAction() {
			super("Save As...");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc;
			if (MainFrame.this.path != null)
				fc = new JFileChooser(MainFrame.this.path);
			else
				fc = new JFileChooser();
			int status = fc.showSaveDialog(MainFrame.this);

			if (status == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				editor.saveMap(file);
			}
		}
	}

	class OpenMapAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public OpenMapAction() {
			super(i18n.tr("Open Map..."));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter(i18n.tr("PLM map files"), "map"));
			int status = fc.showOpenDialog(MainFrame.this);

			if (status == JFileChooser.APPROVE_OPTION) {
				MainFrame.this.path = fc.getSelectedFile().getAbsolutePath();
				try {
					editor.loadMap(MainFrame.this.path);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, e1.getLocalizedMessage(),i18n.tr("Error while reading {0}",path), JOptionPane.ERROR_MESSAGE);
				}
			}
		}

	}

	class QuitAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public QuitAction() {
			super("Quit Map Editor");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
		}

	}
}

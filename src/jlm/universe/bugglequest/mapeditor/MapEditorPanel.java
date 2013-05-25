package jlm.universe.bugglequest.mapeditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import jlm.core.model.Logger;
import jlm.core.ui.ResourcesCache;


public class MapEditorPanel extends JPanel {

	private static final long serialVersionUID = -7243431953648987489L;

	private Editor editor;
	private ButtonGroup tools;
	private File path;	
	private static String HOME_DIR = System.getProperty("user.home");
	private static String SEP = System.getProperty("file.separator");
	private static String SAVE_PATH = HOME_DIR + SEP + ".jlm-export";
	private static File SAVE_DIR = new File(SAVE_PATH);
	
	public MapEditorPanel(Editor editor) {
		this.editor = editor;
		initComponents();
		setSize(800, 600);
	}

	public void initComponents() {

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");

		fileMenu.add(new NewMapAction());
		fileMenu.add(new LoadMapAction());
		fileMenu.add(new SaveAsMapAction());
		fileMenu.add(new SaveMapAction());

		menuBar.add(fileMenu);
		//this.setJMenuBar(menuBar);


		setLayout(new BorderLayout());
		add(menuBar, BorderLayout.PAGE_START);
		
		JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);

		JToggleButton topButton = createButton("topwall"); 
		JToggleButton leftButton = createButton("leftwall"); 
		JToggleButton baggleButton = createButton("baggle");
		JToggleButton buggleButton = createButton("buggle");
		JToggleButton textButton = createButton("text");
		JToggleButton colorButton = createButton("colors",new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editor.setCommand("colors");
				Object[] choices = {
						"black","blue","cyan","darkGray","gray","green","lightGray","magenta","orange","pink","red","yellow"};
				Color[] colors = {
						Color.black,Color.blue,Color.cyan,Color.darkGray,Color.gray,Color.green,Color.lightGray,Color.magenta,Color.orange,Color.pink,Color.red,Color.yellow};
				
				Object selectedValue = JOptionPane.showInputDialog(null,
						"Choose a color", "Change color",
						JOptionPane.INFORMATION_MESSAGE, null,
						choices, choices[editor.getSelectedColorNumber()]);
				
				if (selectedValue == null) // cancel pressed
					return;
					
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
		toolBar.add(colorButton);
		toolBar.add(textButton);

		tools = new ButtonGroup();
		tools.add(topButton);
		tools.add(leftButton);
		tools.add(baggleButton);
		tools.add(buggleButton);
		tools.add(colorButton);
		tools.add(textButton);

		add(toolBar, BorderLayout.LINE_START);

		MapView mapView = new MapView(editor);
		editor.addMapView(mapView);
		add(mapView, BorderLayout.CENTER);
		
		// buggles panel
		JPanel panelBuggles = new JPanel();
		panelBuggles.setLayout(new BorderLayout());
		add(panelBuggles, BorderLayout.SOUTH);
		
		// buggle selection panel
		BuggleChooser buggleChooser = new BuggleChooser(editor.getWorld());
		editor.getWorld().addEntityUpdateListener(buggleChooser);
		panelBuggles.add(buggleChooser, BorderLayout.NORTH);
		
		// control pan
		BuggleControlPanel buttonPanel;
		buttonPanel =  new BuggleControlPanel(buggleChooser);
		buttonPanel.remove(2); // don't need brush button in map editor
		JPanel controlPane = new JPanel();
		controlPane.add(buttonPanel, "grow");
		panelBuggles.add(buttonPanel, BorderLayout.CENTER);		
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
		JToggleButton bt = new JToggleButton(getIcon("resources/editor/"+name+".png"));
		bt.setActionCommand(name);
		bt.setBorderPainted(false);
		bt.addActionListener(l);
		return bt;
	}

	public ImageIcon getIcon(String path) {
		URL url = ResourcesCache.class.getClassLoader().getResource(path);
		try {
			return new ImageIcon(url);
		} catch (NullPointerException e) {
			throw new RuntimeException("Icon "+path+" not found");
		}
	}


	class NewMapAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public NewMapAction() {
			super("Create New Map");
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
				MapEditorPanel.this.path = null;
			}
		}

	}

	class SaveAsMapAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public SaveAsMapAction() {
			super("Save As...");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc;
			if (MapEditorPanel.this.path != null)
				fc = new JFileChooser(MapEditorPanel.this.path);
			else
				fc = new JFileChooser();
			int status = fc.showSaveDialog(MapEditorPanel.this);

			if (status == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				editor.saveMap(file);
			}
		}
	}

	class SaveMapAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public SaveMapAction() {
			super("Save");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(editor.getPath()!=null){
				String path = SAVE_PATH;
				if (!SAVE_DIR.exists()){
					if (! SAVE_DIR.mkdir()) {
						Logger.log("MapEditor:SaveMapAction", "cannot create session store directory (.jlm-export)");
						System.err.println("cannot create session store directory (.jlm-export)");
						return;
					};
				}
				
				String[] dirs = editor.getPath().replaceAll("^src/", "").split("/");
				for(int i=0;i<dirs.length-1;i++){
					path+=SEP + dirs[i];
					File f = new File(path);
					if (!f.exists()){
						if (! f.mkdir()) {
							Logger.log("MapEditor:SaveMapAction", "cannot create session store directory ("+path+")");
							System.err.println("cannot create session store directory ("+path+")");
							return;
						};
					}
				}
				path+=SEP + dirs[dirs.length-1];
				File file = new File(path);
				editor.saveMap(file);
			}
			else{
				JFileChooser fc;
				if (MapEditorPanel.this.path != null)
					fc = new JFileChooser(MapEditorPanel.this.path);
				else
					fc = new JFileChooser();
				int status = fc.showSaveDialog(MapEditorPanel.this);

				if (status == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					editor.saveMap(file);
				}
			}
		}
	}
	
	class LoadMapAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public LoadMapAction() {
			super("Open Map...");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser();
			int status = fc.showOpenDialog(MapEditorPanel.this);

			if (status == JFileChooser.APPROVE_OPTION) {
				MapEditorPanel.this.path = fc.getSelectedFile();
				editor.loadMap(MapEditorPanel.this.path);
			}
		}

	}
}

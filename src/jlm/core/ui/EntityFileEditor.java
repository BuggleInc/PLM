package jlm.core.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jlm.core.model.Game;
import jlm.core.model.Logger;
import jlm.core.model.ProgrammingLanguage;
import jlm.core.model.lesson.Exercise;
import jlm.core.model.lesson.SourceFile;
import jlm.universe.World;

public class EntityFileEditor implements ActionListener {
	String entity_path;
	JButton save_btn;
	SourceFile srcFile;
	private static String HOME_DIR = System.getProperty("user.home");
	private static String SEP = System.getProperty("file.separator");
	private static String SAVE_PATH = HOME_DIR + SEP + ".jlm-export";
	private static File SAVE_DIR = new File(SAVE_PATH);

	public EntityFileEditor(JTabbedPane missioneditortabs,Exercise currentExercise,ProgrammingLanguage newLang) {
		int publicSrcFileCount = ((Exercise) currentExercise).sourceFileCount(newLang);
		for (int i = 0; i < publicSrcFileCount; i++) {
			BufferedReader br = null;
			StringBuffer sb = new StringBuffer();
			
			this.entity_path = "src/"+Game.getInstance().getCurrentLesson().getCurrentExercise().getMissionMarkDownFilePath()+"Entity.java";;
			
			String path = entity_path;
			try {
				//checking if the map file already exists in '.jlm-export/' directory.
				File f = new File(MarkdownDocument.getSAVE_PATH()+entity_path.replaceAll("^src", ""));
				if (f.exists()) {
					path = f.getPath();
				}
				br = new BufferedReader(new FileReader(path));
				String newLine = System.getProperty("line.separator");

				try {
					String s;
					s = br.readLine();
					while (s != null) {
						sb.append(s);
						sb.append(newLine);
						s = br.readLine();
					}

				} catch (IOException e) {		
				} finally {
					try {
						br.close();
					} catch (IOException e) {}
				}
			} catch (Exception e) {
				System.err.println("Entity file "+entity_path+" not found.");
			}
			
			
			JPanel panel = new JPanel(new BorderLayout());
			
			/* Create the code editor */
			SourceFile srcFile0 = ((Exercise) currentExercise).getPublicSourceFile(newLang, i);
			srcFile = new SourceFile(srcFile0.getName(),"");
			srcFile.setBody(sb.toString());

			save_btn = new JButton("Save");
			save_btn.addActionListener(this);

			panel.add(save_btn,BorderLayout.NORTH);
			panel.add(srcFile.getEditorPanel(newLang),BorderLayout.CENTER);

			/* Create the tab with the code editor as content */
			missioneditortabs.addTab(srcFile.getName(), null, panel, "Type your code here"); 
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton){
			JButton btn = (JButton) e.getSource();
			if(btn.getText().equals(save_btn.getText())){
				String path = SAVE_PATH;
				if (!SAVE_DIR.exists()){
					if (! SAVE_DIR.mkdir()) {
						Logger.log("EntityFileEditor:save", "cannot create session store directory (.jlm-export)");
						System.err.println("cannot create session store directory (.jlm-export)");
						return;
					};
				}
				
				String[] dirs = entity_path.replaceAll("^src/", "").split("/");
				for(int i=0;i<dirs.length-1;i++){
					path+=SEP + dirs[i];
					File f = new File(path);
					if (!f.exists()){
						if (! f.mkdir()) {
							Logger.log("EntityFileEditor:save", "cannot create session store directory ("+path+")");
							System.err.println("cannot create session store directory ("+path+")");
							return;
						};
					}
				}
				path+=SEP + dirs[dirs.length-1];
				File file = new File(path);
				try {
					FileWriter fw = new FileWriter(file);
					fw.write(srcFile.getBody());
					fw.close();
					System.out.println("Entity file "+file.getPath()+" saved.");
				} catch (IOException ex) {
					System.err.println("Entity file '"+file.getPath()+"' cannot be saved.");
				}
			}
		}
	}

}

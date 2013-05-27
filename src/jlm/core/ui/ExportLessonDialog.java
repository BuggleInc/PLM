package jlm.core.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class ExportLessonDialog extends AbstractAboutDialog
{
	private static final long serialVersionUID = 1L;
	private JPanel container = new JPanel();
	private JComboBox combo = new JComboBox();
	private JLabel labelLesson = new JLabel("Choose a lesson : ");
	private JButton buttonExport = new JButton("Export");
	
	private static String HOME_DIR = System.getProperty("user.home");
	private static String SEP = System.getProperty("file.separator");
	private static String SAVE_PATH = HOME_DIR + SEP + ".jlm-export" + SEP + "lessons";
	private static File SAVE_DIR = new File(SAVE_PATH);
	private static ArrayList<File> filesToBeJared = new ArrayList<File>();

	protected ExportLessonDialog(JFrame parent)
	{
		super(parent);
		this.setTitle("Export a lesson in a JAR file");
		this.container.setLayout(new BorderLayout());
		//this.container.setBackground(Color.white);
		this.combo.setPreferredSize(new Dimension(100, 20));
		this.buttonExport.addActionListener(new MyListener(this));
		
		this.createLessonChooserComboBox();
		
		JPanel top = new JPanel();
		//top.setBackground(Color.white);
		top.add(labelLesson);
		top.add(combo);
		
		JPanel east = new JPanel();
		//east.setBackground(Color.white);
		east.add(buttonExport);
		
		container.add(top, BorderLayout.NORTH);
		container.add(east, BorderLayout.EAST);
		this.setContentPane(container);
		pack();
		this.resize((int)this.getSize().getWidth() + 60, (int)this.getSize().getHeight());
		this.setLocationRelativeTo(null);
	}
	
	class MyListener implements ActionListener
	{
		private AbstractAboutDialog dialog;
		
		public MyListener(AbstractAboutDialog dialog)
		{
			this.dialog = dialog;
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			ArrayList<String> exercicesEdited = new ArrayList<String>();
			File localDir = new File(SAVE_PATH + SEP + combo.getSelectedItem());
			for(String s:localDir.list())
			{
				String tab[] = s.split("\\.");
				if(tab.length > 1)
				{
					if(!exercicesEdited.contains(tab[0]) && !tab[0].endsWith("Entity") && !tab[0].endsWith("DS_Store"))
					{
						exercicesEdited.add(tab[0]);
					}
				}
			}
			
			InputStream input;
			String file;
			String localPath = SAVE_PATH + SEP + combo.getSelectedItem() + SEP;
			String path = "/lessons/" + combo.getSelectedItem() + "/";
			
			// Main file
			file = "Main.class";
			input = getClass().getResourceAsStream(path + file);
			copyFile(input, localPath, path, file);
			
			for(String s:exercicesEdited)
			{
				localPath = SAVE_PATH + SEP + combo.getSelectedItem() + SEP + s;
				path = "/lessons/" + combo.getSelectedItem() + "/" + s;
				
				// Exercise file
				file = ".class";
				input = getClass().getResourceAsStream(path + file);
				copyFile(input, localPath, path, file);
				
				// Entity java file
				file = "Entity.class";
				input = getClass().getResourceAsStream(path + file);
				copyFile(input, localPath, path, file);
				
				// Entity py file
				file = "Entity.py";
				input = getClass().getResourceAsStream(path + file);
				copyFile(input, localPath, path, file);
				
				// Md en file
				file = ".md";
				input = getClass().getResourceAsStream(path + file);
				copyFile(input, localPath, path, file);
				
				// Md fr file
				file = ".fr.md";
				input = getClass().getResourceAsStream(path + file);
				copyFile(input, localPath, path, file);
				
				// Map file
				file = ".map";
				input = getClass().getResourceAsStream(path + file);
				copyFile(input, localPath, path, file);
			}
			
			// Create the JAR file
			JFileChooser fc = new JFileChooser();
			
			class MyFilter extends FileFilter
			{
				private String description;
				private String extension;
				
				public MyFilter(String description, String extension)
				{
					this.description = description;
					this.extension = extension;
				}
				
				public boolean accept(File f)
				{
					if(f.isDirectory()) { 
				         return true; 
				      } 
				      String nomFichier = f.getName().toLowerCase(); 
				 
				      return nomFichier.endsWith(extension);
				}

				public String getDescription()
				{
					return description;
				}
			};
			
			FileFilter myFilter = new MyFilter("JAR Files", ".jar");
			
			fc.addChoosableFileFilter(myFilter);
			fc.setFileFilter(myFilter);
			
			int returnVal = fc.showSaveDialog(dialog);
	        if (returnVal == JFileChooser.APPROVE_OPTION)
	        {
	            File fi = fc.getSelectedFile();
	            if(!fi.getAbsolutePath().endsWith(".jar"))
	            	fi = new File(fi.getAbsolutePath() + ".jar");
	            if(filesToBeJared.size() > 0)
	            {
	            	createJarArchive(fi, (String)combo.getSelectedItem(), filesToBeJared);
	            	dialog.dispose();
	    	        JOptionPane.showMessageDialog(dialog.getParent(), "JAR file successfully created\n\nPath = \""+ fi.getAbsolutePath() + "\"", "JLM", JOptionPane.INFORMATION_MESSAGE);
	            }
	            else
	            {
	            	dialog.dispose();
	            }
	        }
		}
	}
	
	/** copie le fichier source dans le fichier resultat
	 * retourne vrai si cela réussit
	 */
	public static boolean copyFile(InputStream input, OutputStream output){
		try{
	 
			try{
	 
				try{
	 
					// Lecture par segment de 0.5Mo 
					byte buffer[] = new byte[512 * 1024];
					int nbLecture;
	 
					while ((nbLecture = input.read(buffer)) != -1){
						output.write(buffer, 0, nbLecture);
					}
				} finally {
					output.close();
				}
			} finally {
				input.close();
			}
		} catch (IOException e){
			e.printStackTrace();
			return false; // Erreur
		}
	 
		return true; // Résultat OK  
	}
	
	private void createLessonChooserComboBox()
	{
		String [] packages = SAVE_DIR.list();
		for(String s:packages)
		{
			if(!s.equals(".DS_Store"))
				combo.addItem(s);
		}
	}

	private void copyFile(InputStream input, String localPath, String path, String file)
	{
		OutputStream output = null;
		
		// If the file is in the JAR
		if(input!=null)
		{
			File f = new File(localPath + file);
			// copy source file if not modified
			if(!f.exists())
			{
				try
				{
					output = new FileOutputStream(f);
					copyFile(input, output);
				}
				catch (FileNotFoundException e1)
				{
					
				}
			}
			if(!filesToBeJared.contains(f))
				filesToBeJared.add(f);
		}
	}
	
	protected void createJarArchive(File archiveFile, String lesson, ArrayList<File> tobeJared) {
	    try {
	      byte buffer[] = new byte[1024];
	      // Open archive file
	      FileOutputStream stream = new FileOutputStream(archiveFile);
	      ManifestWriter manifestWriter = new ManifestWriter();
	      manifestWriter.setManifestContents("Class-Path: .\n"
	    		  + "LessonPackage: " + lesson);
	      
	      JarOutputStream out = new JarOutputStream(stream, manifestWriter.getManifest());

	      for (int i = 0; i < tobeJared.size(); i++) {
	        if (tobeJared.get(i) == null || !tobeJared.get(i).exists()
	            || tobeJared.get(i).isDirectory())
	          continue; // Just in case...

	        // Add archive entry
	        JarEntry jarAdd = new JarEntry("lessons/" + lesson + "/" + tobeJared.get(i).getName());
	        jarAdd.setTime(tobeJared.get(i).lastModified());
	        out.putNextEntry(jarAdd);

	        // Write file to archive
	        FileInputStream in = new FileInputStream(tobeJared.get(i));
	        while (true) {
	          int nRead = in.read(buffer, 0, buffer.length);
	          if (nRead <= 0)
	            break;
	          out.write(buffer, 0, nRead);
	        }
	        in.close();
	      }

	      out.close();
	      stream.close();
	    } catch (Exception ex) {
	      ex.printStackTrace();
	      System.out.println("Error: " + ex.getMessage());
	    }
	  }
	
	public void show()
	{
		if(combo.getItemCount() > 0)
			super.show();
		else
			JOptionPane.showMessageDialog(MainFrame.getInstance(), "No edited lessons to export", "JLM", JOptionPane.ERROR_MESSAGE);
	}
}
package jlm.core.ui;

/**
 * 
 * Markdown file loader class
 *
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;

import jlm.core.model.FileUtils;
import jlm.core.model.Logger;

public class MarkdownDocument extends Observable
{
	private String path;
	private String text = "";
	private boolean load_editor = false;
	private String lang = "";
	
	private static String HOME_DIR = System.getProperty("user.home");
	private static String SEP = System.getProperty("file.separator");
	private static String SAVE_PATH = HOME_DIR + SEP + ".jlm-export";
	private static File SAVE_DIR = new File(SAVE_PATH);
	
	public MarkdownDocument()
	{
		text = "";
		path = "";
		load_editor = false;
	}
	
	public MarkdownDocument(String path)
	{
		loadMarkDownDocument(path);
	}
	
	public void loadMarkDownDocument(String path)
	{
		this.path = path;
		text = new String();
		try {
			StringBuffer sb = FileUtils.readContentAsTextOption(path, "md",true,true);
			text = sb.toString();
			setLoad_editor(true);
			setChanged();
			notifyObservers();
		} catch (Exception ex) {
			System.err.println("File "+path+" not found.");
		}
		this.lang=FileUtils.getLocale();
	}
	
	public void saveMarkDownDocument()
	{
		String path = SAVE_PATH;
		if (!SAVE_DIR.exists()){
			if (! SAVE_DIR.mkdir()) {
				Logger.log("MarkDownDocument:saveMarkDownDocument", "cannot create session store directory (.jlm-export)");
				System.err.println("cannot create session store directory (.jlm-export)");
				return;
			};
		}

		String[] dirs = getPath().split("/");
		for(int i=0;i<dirs.length-1;i++){
			path+=SEP + dirs[i];
			File f = new File(path);
			if (!f.exists()){
				if (! f.mkdir()) {
					Logger.log("MarkDownDocument:saveMarkDownDocument", "cannot create session store directory ("+path+")");
					System.err.println("cannot create session store directory ("+path+")");
					return;
				}
			}
		}
		path+=SEP + dirs[dirs.length-1];
		
		try
		{
			BufferedWriter buffer = new BufferedWriter(new FileWriter(path));
			buffer.write(text);
			buffer.close();

			System.err.println("File "+path+" saved.");
		}
		catch (IOException e)
		{
			System.err.println("IO Error : cannot write md file ("+path+")");
		}
		
	}

	public String getText()
	{
		return text;
	}

	public void setTexte(String text)
	{
		this.text = text;
	}

	public void update(String text) {
		setLoad_editor(false);
		setTexte(text);
		setChanged();
		notifyObservers();
	}

	public boolean isLoad_editor() {
		return load_editor;
	}

	public void setLoad_editor(boolean load_editor) {
		this.load_editor = load_editor;
	}

	public String getPath() {
		return path.replace('.', '/')+"."+lang+".md";
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public static String getSAVE_PATH() {
		return SAVE_PATH;
	}

	public static void setSAVE_PATH(String sAVE_PATH) {
		SAVE_PATH = sAVE_PATH;
	}
}
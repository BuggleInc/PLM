package jlm.core.ui;

/**
 * 
 * Markdown file loader class
 *
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;

import jlm.core.model.FileUtils;

public class MarkdownDocument extends Observable
{
	private String path;
	private String text = "";
	private boolean load_editor = false;
	private String lang = "";
	
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
		/*
		Scanner scanner = null;
		try
		{
			scanner = new Scanner(new File(chemin));
		}
		catch (Exception e){
			System.err.println("Page \"" + chemin + "\" introuvable.");
		}

		while(scanner.hasNext())
		{
			texte += scanner.nextLine()+"\n";
		}

		scanner.close();
		*/
		try {
			StringBuffer sb = FileUtils.readContentAsText(path, "md",true);
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
		try
		{
			BufferedWriter buffer = new BufferedWriter(new FileWriter(path));
			buffer.write(text);
			buffer.close();
		}
		catch (IOException e)
		{
			System.err.println("IO Error : cannot write md file.");
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
}
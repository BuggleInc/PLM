package jlm.core.ui;

/**
 * 
 * Représente un document au format Markdown.
 *
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Scanner;

import jlm.core.model.FileUtils;

public class MarkdownDocument extends Observable
{
	private String chemin;
	private String texte = "";
	private boolean load_editor = false;
	
	public MarkdownDocument()
	{
		texte = "";
		chemin = "";
		load_editor = false;
	}
	
	public MarkdownDocument(String chemin)
	{
		chargerDocument(chemin);
	}
	
	public void chargerDocument(String chemin)
	{
		this.chemin = chemin;
		texte = new String();
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
			StringBuffer sb = FileUtils.readContentAsText(chemin, "md",true);
			texte = sb.toString();
			setLoad_editor(true);
			setChanged();
			notifyObservers();
		} catch (Exception ex) {
			System.err.println("File "+chemin+" not found.");
		}		
	}
	
	public void enregistrerDocument()
	{
		try
		{
			BufferedWriter buffer = new BufferedWriter(new FileWriter(chemin));
			buffer.write(texte);
			buffer.close();
		}
		catch (IOException e)
		{
			System.err.println("Erreur IO lors de l'écriture du fichier HTML.");
		}
	}

	public String getTexte()
	{
		return texte;
	}

	public void setTexte(String texte)
	{
		this.texte = texte;
	}

	public void maj(String text) {
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

	public String getChemin() {
		return chemin;
	}
}
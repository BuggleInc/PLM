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

import jlm.core.model.Reader;

public class MarkdownDocument extends Observable
{
	private String chemin;
	private String texte = "";
	
	public MarkdownDocument()
	{
		texte = new String();
	}
	
	public MarkdownDocument(String chemin)
	{
		this.chemin = chemin;
		chargerDocument(chemin);
	}
	
	public void chargerDocument(String chemin)
	{
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
		StringBuffer sb = Reader.fileToStringBuffer(chemin, "md",true);
		if (sb==null) {
			System.err.println("File "+chemin+" not found.");
			return;
		}		
		texte = sb.toString();
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
		setTexte(text);
		setChanged();
		notifyObservers();
	}
}
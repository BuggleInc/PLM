package jlm.core.utils;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import jlm.core.model.Game;
import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.SyntaxStyle;
import jsyntaxpane.SyntaxStyles;
import jsyntaxpane.TokenType;
import jsyntaxpane.syntaxkits.JavaSyntaxKit;
import jsyntaxpane.syntaxkits.PythonSyntaxKit;
import jsyntaxpane.syntaxkits.ScalaSyntaxKit;
import jsyntaxpane.syntaxkits.XHTMLSyntaxKit;
import jsyntaxpane.util.Configuration;

public class JlmSyntaxPane {

	public static void initKits() {
		DefaultSyntaxKit.initKit();
        //Configuration conf = DefaultSyntaxKit.getConfig(DefaultSyntaxKit.class);


		//TODO: can be configured through a property file in the new version of jsyntaxpane
		SyntaxStyles st = SyntaxStyles.getInstance();
		st.put(TokenType.OPERATOR, new SyntaxStyle(Color.BLACK, false, false)); // black
		st.put(TokenType.KEYWORD, new SyntaxStyle(new Color(0x8d0056), true, false)); // violet,
																						// bold
		st.put(TokenType.TYPE, new SyntaxStyle(Color.BLACK, false, false)); // black
		st.put(TokenType.COMMENT, new SyntaxStyle(new Color(0x29825e), false, false)); // dark
																						// green
		st.put(TokenType.NUMBER, new SyntaxStyle(Color.BLACK, false, false)); // black
		// st.add(TokenType.REGEX, new SyntaxStyle(new Color(0xcc6600), false, false)); // not used in Java
		// st.add(TokenType.IDENT, new SyntaxStyle(new Color(0x1300c5), false, false)); // dark blue
		st.put(TokenType.IDENTIFIER, new SyntaxStyle(Color.black, false, false)); // black
		st.put(TokenType.STRING, new SyntaxStyle(new Color(0x3600ff), false, false)); // blue
		st.put(TokenType.DEFAULT, new SyntaxStyle(Color.BLACK, false, false)); // black

		Configuration javaConf = JavaSyntaxKit.getConfig(JavaSyntaxKit.class);
		Configuration pyConf = PythonSyntaxKit.getConfig(PythonSyntaxKit.class);
		Configuration scalaConf = ScalaSyntaxKit.getConfig(ScalaSyntaxKit.class);
		Configuration xhtmlConf = XHTMLSyntaxKit.getConfig(XHTMLSyntaxKit.class);

		setupKit(javaConf);
		setupKit(pyConf);
		setupKit(scalaConf);
		setupKit(xhtmlConf);
	}
	
	private static void setupKit(Configuration conf) {
		Properties props = new Properties();
		File bindings = new File(Game.getSavingLocation()+File.separatorChar+"bindings.properties");
		BufferedWriter out = null;
		try {
			if (!bindings.exists()) {
				System.out.println("Bootstraping "+bindings);
				out = new BufferedWriter(new FileWriter(bindings));
				// TODO: document the content of this file
				out.write("Action.find = jsyntaxpane.actions.FindReplaceAction, control H\n");
				out.write("Action.undo = jsyntaxpane.actions.UndoAction, control Z\n");
				out.write("Action.redo = jsyntaxpane.actions.RedoAction, control Y\n");
				out.write("complete-word = jsyntaxpane.actions.CompleteWordAction, control SPACE\n");
				out.write("Action.toggle-comments = jsyntaxpane.actions.ToggleCommentsAction, control SEMICOLON\n");
				out.write("PopupMenu = cut-to-clipboard,copy-to-clipboard,paste-from-clipboard,-,select-all,-,undo,redo,-,find,find-next,goto-line,jump-to-pair,-,complete-word,-,toggle-comments");
				out.close();
			}
			props.load(new FileReader(bindings));				
			conf.putAll(props);				
		} catch (Exception e) {
			System.out.println("Error while reading the jlm bindings: "+e.getLocalizedMessage());
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}
}

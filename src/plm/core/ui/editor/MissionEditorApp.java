package plm.core.ui.editor;

import java.io.File;

import plm.core.model.Game;

public class MissionEditorApp {

	public static void main(String[] args) {
		MissionEditor editor = new MissionEditor();
		Game.getInstance().switchDebug(); // Forces the PlmHTMLEditorKit to also display all blocks marked with a class
		if (args.length>0)
			editor.loadMission(args[0]);
		else {
			String path = System.getProperty("user.dir")+"/src/lessons/welcome/Main.html";
			File f = new File(path);
			if (f.exists())
				editor.loadMission(path);
		}
	}

}

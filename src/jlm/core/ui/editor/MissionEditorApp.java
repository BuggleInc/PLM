package jlm.core.ui.editor;



public class MissionEditorApp {

	public static void main(String[] args) {
		MissionEditor editor = new MissionEditor();
		if (args.length>0)
			editor.loadMission(args[0]);
		else
			editor.loadMission("/home/mquinson/Code/JLM/src/lessons/welcome/loopfor/LoopFor.html");
	}

}

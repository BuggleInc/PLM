package plm.core.ui.editor.buggleeditor;

import java.io.IOException;


public class MapEditorApp {

	public static void main(String[] args) {
		Editor editor = new Editor();
		new MainFrame(editor);
		if (args.length>0)
			try {
				editor.loadMap(args[0]);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}

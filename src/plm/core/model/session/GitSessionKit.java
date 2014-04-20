package plm.core.model.session;

import java.io.File;
import plm.core.model.Game;
import plm.core.model.UserAbortException;
import plm.core.model.lesson.Lesson;

public class GitSessionKit implements ISessionKit {
	
	private Game game;

	public GitSessionKit(Game game) {
		this.game = game;
	}

	@Override
	public void storeAll(File path) throws UserAbortException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void loadAll(File path) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void storeLesson(File path, Lesson l) throws UserAbortException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void loadLesson(File path, Lesson l) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void cleanAll(File path) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void cleanLesson(File path, Lesson l) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}

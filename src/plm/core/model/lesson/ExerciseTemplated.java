package plm.core.model.lesson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.json.simple.JSONObject;

import plm.core.PLMCompilerException;
import plm.core.PLMEntityNotFound;
import plm.core.log.Logger;
import plm.core.model.Game;
import plm.core.model.LogHandler;
import plm.core.model.lesson.Lesson.LoadingOutcome;
import plm.universe.BrokenWorldFileException;
import plm.universe.Entity;
import plm.universe.World;



public abstract class ExerciseTemplated extends Exercise {

	protected String worldFileName = getClass().getCanonicalName(); /* Name of the save files */

	public ExerciseTemplated(String id, String name) {
		super(id, name);
	}

	public ExerciseTemplated(Game game, Lesson lesson) {
		super(game, lesson,null);
	}
	public ExerciseTemplated(Game game, Lesson lesson, String basename) {
		super(game, lesson,basename);
	}

	public ExerciseTemplated(Exercise exo) {
		super(exo);
	}

	public ExerciseTemplated(JSONObject json) {
		super(json);
	}

	protected final void setup(World w) {
		setup(new World[] {w});
	}

	protected <W extends World> void setup(W[] ws) {
		//boolean foundALanguage=false;
		ArrayList<String> files = new ArrayList<String>();
		/*
		int k = 0;

		while(getClass().getResourceAsStream("/"+Game.JAVA.nameOfCommonError(this, k).replaceAll("\\.", "/")+".java")!=null) {
			files.add("/"+Game.JAVA.nameOfCommonError(this, k).replaceAll("\\.", "/")+".java");
			k++;
		}
		*/
		setupWorlds(ws,files.size());
		/*
		foundALanguage = initSourcesFiles();
		if (!foundALanguage)
			throw new RuntimeException(getGame().i18n.tr("{0}: No entity found. You should fix your paths and such",getName()));
		computeAnswer();
		computeError(files);
		*/
	}
}

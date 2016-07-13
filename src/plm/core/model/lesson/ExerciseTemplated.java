package plm.core.model.lesson;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

import plm.universe.World;

public abstract class ExerciseTemplated extends Exercise {

	protected String worldFileName = getClass().getCanonicalName(); /* Name of the save files */

	public ExerciseTemplated(String id, String name) {
		this(name);
	}

	public ExerciseTemplated(String name) {
		super(name, name);
		setId(getClass().getCanonicalName());
	}

	public ExerciseTemplated(Exercise exo) {
		super(exo);
	}

	@JsonIgnore
	protected final void setup(World w) {
		setup(new World[] {w});
	}

	@JsonIgnore
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

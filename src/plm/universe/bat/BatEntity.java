package plm.universe.bat;

import java.io.BufferedWriter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.Game;
import plm.core.model.lesson.ExecutionProgress;
import plm.universe.Entity;
import plm.universe.World;

public class BatEntity extends Entity {
	
	public BatEntity() {
		super();
	}
	
	public BatEntity(String name, World w) {
		super(name,w);
	}
	
	public BatEntity(BatEntity other) {
		super();
		copy(other);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BatEntity)) {
			return false;
		}
		return (super.equals(o));
	}
	
	@Override
	public void run() {
		for (BatTest t:((BatWorld) world).getTests())
			try {
				run(t);
			} catch (Exception e) {
				t.setResult(Game.i18n.tr("This test raised a {0} exception: {1}",e.getClass().getName(), e.getMessage()));
				e.printStackTrace();
			}
	}

	protected void run(BatTest t) {
		// To be overriden by child classes
	}

	@Override
	public void command(String command, BufferedWriter out) {
		// TODO if use
		
	}

}

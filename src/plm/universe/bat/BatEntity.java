package plm.universe.bat;

import java.io.BufferedWriter;

import plm.core.lang.LangBlockly;
import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.universe.Entity;
import plm.universe.World;
import plm.universe.bat.operations.SetResult;

public class BatEntity extends Entity {

	boolean addedExtraScript = false;
	
	public BatEntity() {
		super();
	}

	public BatEntity(String funName) {
		super();
		setName(funName);
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
		int i = 0;
		for (BatTest t:((BatWorld) world).getTests()) {
			try {
				run(t);
				generateSetResultOperation(i, t);
			} catch (Exception e) {
				t.setResult(getGame().i18n.tr("Exception {0}: {1}",e.getClass().getName(), e.getMessage()));
				e.printStackTrace();
			}
			i++;
		}
	}

	@Override
	public void setScript(ProgrammingLanguage lang, String s) {
		String extraScript = "";
		if((lang instanceof LangPython || lang instanceof LangBlockly) && !addedExtraScript) {
			// FIXME: Find a better workflow to add this script to all BatEntity
			addedExtraScript = true;
			extraScript = 
					"i = 0\n" +
					"for t in batTests:\n" +
					"  t.setResult("+name+"(t.getParameter(0), t.getParameter(1)))\n" +
					"  entity.generateSetResultOperation(i, t)\n" +
					"  i += 1\n";	
		}
		super.setScript(lang, s + extraScript);
	}

	public void generateSetResultOperation(int index, BatTest t) {
		addOperation(new SetResult(index, t));
		stepUI();
	}

	protected void run(BatTest t) {
		// To be overriden by child classes
	}

	@Override
	public void command(String command, BufferedWriter out) {
		// TODO if use
		
	}

}

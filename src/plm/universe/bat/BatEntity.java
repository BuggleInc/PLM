package plm.universe.bat;

import java.io.BufferedWriter;

import org.xnap.commons.i18n.I18n;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import plm.core.lang.LangBlockly;
import plm.core.lang.LangPython;
import plm.core.lang.ProgrammingLanguage;
import plm.core.model.I18nManager;
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
	
	@JsonCreator
	public BatEntity(@JsonProperty("name")String name, @JsonProperty("world")World w) {
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
				I18n i18n = I18nManager.getI18n(getWorld().getLocale());
				t.setResult(i18n.tr("Exception {0}: {1}",e.getClass().getName(), e.getMessage()));
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
			BatWorld batWorld = (BatWorld) world;
			int nbParameters = batWorld.getTests().get(0).parameters.length;
			String parameters = "";
			for( int i=0; i<nbParameters; i++) {
				if(i>0) {
					parameters += ", ";
				}
				parameters += "t.getParameter(" + i + ")";
			}
			addedExtraScript = true;
			extraScript = 
					"i = 0\n" +
					"for t in batTests:\n" +
					"  t.setResult( "+name+"( "+ parameters +" ) )\n" +
					"  entity.generateSetResultOperation(i, t)\n" +
					"  i += 1\n";	
		}
		super.setScript(lang, s + extraScript);
	}

	public void generateSetResultOperation(int index, BatTest t) {
		addOperation(new SetResult(index, t.getResult()));
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

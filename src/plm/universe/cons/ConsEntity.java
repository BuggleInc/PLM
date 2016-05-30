package plm.universe.cons;

import com.fasterxml.jackson.annotation.JsonIgnore;

import plm.universe.bat.BatEntity;
import plm.universe.bat.BatWorld;

public class ConsEntity extends BatEntity {

	public ConsEntity() {
		super();
	}

	public ConsEntity(String funName) {
		super(funName);
	}

	@Override
	@JsonIgnore
	public String getScript() {
		BatWorld batWorld = (BatWorld) world;
		int nbParameters = batWorld.getTests().get(0).getParameters().length;
		String parameters = "";
		for( int i=0; i<nbParameters; i++) {
			if(i>0) {
				parameters += ", ";
			}
			Object parameter = batWorld.getTests().get(0).getParameter(i);
			String defaultScript = "t.getParameter(" + i + ")";
			if(parameter instanceof int[]) {
				parameters += " RecList.fromArray( " + defaultScript + " )";
			} else {
				parameters += defaultScript;
			}
		}
		return
				"i = 0\n" +
				"for t in batTests:\n" +
				"  t.setResult( "+name+"( "+ parameters +" ) )\n" +
				"  entity.generateSetResultOperation(i, t)\n" +
				"  i += 1\n";
	}
}

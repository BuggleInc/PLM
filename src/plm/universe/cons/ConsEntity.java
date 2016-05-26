package plm.universe.cons;

import com.fasterxml.jackson.annotation.JsonIgnore;

import plm.core.log.Logger;
import plm.universe.bat.BatEntity;

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
		String parameter = "RecList.fromArray( t.getParameter(0) )";
		String script = 
				"i = 0\n" +
				"for t in batTests:\n" +
				"  t.setResult( "+name+"( "+ parameter +" ) )\n" +
				"  entity.generateSetResultOperation(i, t)\n" +
				"  i += 1\n";
		return script;
	}
}

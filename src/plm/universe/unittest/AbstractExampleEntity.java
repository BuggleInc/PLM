package plm.universe.unittest;

import java.io.BufferedWriter;

import plm.universe.Entity;

public abstract class AbstractExampleEntity extends Entity {

	public void setObjective(boolean objective) {
		((ExampleWorld) world).setObjective(objective);
	}
	
	@Override
	public void command(String command, BufferedWriter out) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AbstractExampleEntity))
			return false;

		return true;
	}
}

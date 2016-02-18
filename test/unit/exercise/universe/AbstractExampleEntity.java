package unit.exercise.universe;

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
}

package plm.core.model.lesson;

import java.util.Map;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import plm.universe.World;

public class BlankExercise extends ExerciseTemplated {

	public BlankExercise(Exercise exo) {
		super(exo);
	}

	@JsonCreator
	public BlankExercise(@JsonProperty("id")String id, @JsonProperty("name")String name, @JsonProperty("mission")Map<String, String> mission) {
		super(id, name);
		for(String humanLang : mission.keySet()) {
			addMission(humanLang, mission.get(humanLang));
		}
	}
	
	public void setupCurrentWorld() {
		currentWorld = new Vector<World>();
		for(World w : initialWorld) {
			currentWorld.addElement(w.copy());
		}
	}
}

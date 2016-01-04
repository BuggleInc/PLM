package plm.core.model.lesson;

import org.json.simple.JSONObject;

public class BlankExercise extends ExerciseTemplated {

	public BlankExercise(Exercise exo) {
		super(exo);
	}
	
	public BlankExercise(JSONObject json) {
		super(json);
	}
}

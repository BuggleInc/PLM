package plm.core.model.lesson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BlankExercise extends ExerciseTemplated {

	public BlankExercise(Exercise exo) {
		super(exo);
	}

	@JsonCreator
	public BlankExercise(@JsonProperty("id")String id, @JsonProperty("name")String name) {//, @JsonProperty("initialWorld")World[] initialWorld, @JsonProperty("answerWorld")World[] answerWorld) {
		super(id, name);
	}
}

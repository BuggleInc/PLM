package plm.core.model.lesson;

import java.util.Map;
import java.util.Vector;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.session.SourceFile;
import plm.universe.World;

public class BlankExercise extends ExerciseTemplated {

	public BlankExercise(Exercise exo) {
		super(exo);
	}

	@JsonCreator
	public BlankExercise(@JsonProperty("id")String id, @JsonProperty("name")String name,
						 @JsonProperty("defaultSourceFiles")Map<String, SourceFile> defaultSourceFiles,
						 @JsonProperty("instructions")Map<String, String> instructions, 
						 @JsonProperty("helps")Map<String, String> helps) {
		super(name);
		setId(id);
		for(String progLangName : defaultSourceFiles.keySet()) {
			ProgrammingLanguage progLang = ProgrammingLanguage.getProgrammingLanguage(progLangName);
			addDefaultSourceFile(progLang, defaultSourceFiles.get(progLangName));
		}
	}
	
	public void setupCurrentWorld() {
		currentWorld = new Vector<World>();
		for(World w : initialWorld) {
			currentWorld.addElement(w.copy());
		}
	}
}

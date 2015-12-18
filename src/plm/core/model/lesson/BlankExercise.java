package plm.core.model.lesson;

import java.lang.reflect.InvocationTargetException;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import plm.universe.World;

public class BlankExercise extends ExerciseTemplated {

	public BlankExercise(Exercise exo) {
		super(exo);
	}
	
	public BlankExercise(JSONObject json) {
		super((String) json.get("id"), (String) json.get("name"));
		
		initialWorld = new Vector<World>();
		JSONArray jsonInitialWorlds = (JSONArray) json.get("initialWorlds");
		for(int i=0; i<jsonInitialWorlds.size(); i++) {
			JSONObject jsonWorld = (JSONObject) jsonInitialWorlds.get(i);
			String type = (String) jsonWorld.get("type");
			try {
				World w = (World) Class.forName(type).getDeclaredConstructor(JSONObject.class).newInstance(jsonWorld);
				initialWorld.add(w);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		currentWorld = new Vector<World>();
		for(World w : initialWorld) {
			currentWorld.addElement(w.copy());
		}

		answerWorld = new Vector<World>();
		JSONArray jsonAnswerWorlds = (JSONArray) json.get("answerWorlds");
		for(int i=0; i<jsonAnswerWorlds.size(); i++) {
			JSONObject jsonWorld = (JSONObject) jsonAnswerWorlds.get(i);
			String type = (String) jsonWorld.get("type");
			try {
				World w = (World) Class.forName(type).getDeclaredConstructor(JSONObject.class).newInstance(jsonWorld);
				answerWorld.add(w);
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException
					| ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
	}
}

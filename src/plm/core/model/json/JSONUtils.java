package plm.core.model.json;

import java.awt.Color;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import plm.core.model.lesson.BlankExercise;
import plm.core.model.lesson.Exercise;

public class JSONUtils {
	public static ObjectMapper mapper = initMapper();

	private static ObjectMapper initMapper() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module  = new SimpleModule();
        module.addDeserializer(Color.class, new CustomColorDeserializer());
        mapper.registerModule(module);
		return mapper;
	}

	public static Exercise jsonToExercise(String json) {
		Exercise exercise = null;
		try {
			exercise = mapper.readValue(json, BlankExercise.class);
			((BlankExercise) exercise).setupCurrentWorld();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return exercise;
	}

	public static String exerciseToJudgeJSON(Exercise exercise) {
		ObjectNode root = (ObjectNode) mapper.convertValue(exercise, JsonNode.class);
        String typeEntities = root.path("initialWorld").path(0).path("entities").path(0).path("type").asText();
        int nbWorlds = exercise.getWorldCount();
        for(int i=0; i<nbWorlds; i++) {
        	int nbEntities = exercise.getInitialWorld().get(i).getEntityCount();
        	for(int j=0; j<nbEntities; j++) {
        		ObjectNode entity = (ObjectNode) root.path("answerWorld").path(i).path("entities").path(j);
        		entity.put("type", typeEntities);
        	}
        }
        return root.toString();
	}

	public static String exerciseToClientJSON(Exercise exercise) {
		ObjectNode root = (ObjectNode) mapper.convertValue(exercise, JsonNode.class);
        root.remove("defaultSourceFile");
        return root.toString();
	}

	public static String mapToJSON(Map<String, Object> map) {
		String json = "";
		try {
			json = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}

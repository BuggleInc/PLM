package plm.core.model.json;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;

import plm.core.model.lesson.BlankExercise;
import plm.core.model.lesson.Exercise;
import plm.universe.Direction;
import plm.universe.Operation;
import plm.universe.World;

public class JSONUtils {
	public static ObjectMapper mapper = initMapper();

	private static ObjectMapper initMapper() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module  = new SimpleModule();
        module.addDeserializer(Color.class, new CustomColorDeserializer());
        module.addDeserializer(Direction.class, new CustomDirectionDeserializer());
        mapper.registerModule(module);
		return mapper;
	}

	public static Exercise fileToExercise(String path) {
		Exercise exercise = null;
		try {
			exercise = mapper.readValue(new File(path), BlankExercise.class);
			((BlankExercise) exercise).setupCurrentWorld();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return exercise;
	}

	public static Exercise jsonToExercise(JsonNode json) {
		Exercise exercise = null;
		try {
			exercise = mapper.treeToValue(json, BlankExercise.class);
			((BlankExercise) exercise).setupCurrentWorld();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return exercise;
	}

	public static ObjectNode exerciseToJudgeJSON(Exercise exercise) {
		ObjectNode root = (ObjectNode) mapper.convertValue(exercise, JsonNode.class);
        String typeEntities = root.path("initialWorlds").path(0).path("entities").path(0).path("type").asText();
        int nbWorlds = exercise.getWorldCount();
        for(int i=0; i<nbWorlds; i++) {
        	int nbEntities = exercise.getInitialWorlds().get(i).getEntityCount();
        	for(int j=0; j<nbEntities; j++) {
        		ObjectNode entity = (ObjectNode) root.path("answerWorlds").path(i).path("entities").path(j);
        		entity.put("type", typeEntities);
        	}
        }
        return root;
	}

	public static ObjectNode exerciseToClientJSON(Exercise exercise, String code, String selectedWorldID, String toolbox) {
		ObjectNode root = (ObjectNode) mapper.convertValue(exercise, JsonNode.class);
        root.remove("defaultSourceFile");
        root.put("code", code);
        root.put("selectedWorldID", selectedWorldID);
        root.put("toolbox", toolbox);
        return root;
	}

	public static String operationsToJSON(World world, int bufferSize) {
		int nbSteps = world.getSteps().size();

		if(bufferSize <= 0 ) {
			bufferSize = nbSteps;
		} else {
			bufferSize = Math.min(nbSteps, bufferSize);
		}

		List<List<Operation>> steps = new ArrayList<List<Operation>>();
        for(int i=0; i<bufferSize; i++) {
        	steps.add(world.getSteps().poll());
        }

        Map<String, Object> mapArgs = new HashMap<String, Object>();
		mapArgs.put("worldID", world.getName());
        mapArgs.put("steps", steps);

        return createMessage("operations", mapArgs);
	}

	public static String demoOperationsToJSON(World world) {
		Map<String, Object> mapArgs = new HashMap<String, Object>();
        mapArgs.put("worldID", world.getName());
        mapArgs.put("steps", world.getSteps());

        return createMessage("demoOperations", mapArgs);
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

	public static String createMessage(String cmd, Map<String, Object> mapArgs) {
		Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("args", mapArgs);
        return "{\"cmd\":\""+cmd+"\","+ mapToJSON(msg).substring(1);
	}
}

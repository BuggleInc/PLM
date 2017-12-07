package plm.core.model.json;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.module.scala.DefaultScalaModule;

import plm.core.lang.ProgrammingLanguage;
import plm.core.model.lesson.BlankExercise;
import plm.core.model.lesson.Exercise;
import plm.universe.Direction;
import plm.universe.Operation;
import plm.universe.SVGOperation;
import plm.universe.World;

public class JSONUtils {
	public static ObjectMapper mapper = initMapper();

	private static ObjectMapper initMapper() {
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module  = new SimpleModule();
        module.addDeserializer(Color.class, new CustomColorDeserializer());
        module.addDeserializer(Direction.class, new CustomDirectionDeserializer());
        mapper.registerModule(module);
        mapper.registerModule(new DefaultScalaModule());

		PropertyFilter buggleWorldCellFilter = new BuggleWorldCellFilter();
		FilterProvider filters = new SimpleFilterProvider().addFilter("buggleWorldCellFilter", buggleWorldCellFilter);

		mapper.setFilterProvider(filters);

		return mapper;
	}

	public static void exerciseToFile(String path, Exercise exercise) {
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {
			ObjectNode root = exerciseToJSON(exercise);
			fixTypeEntities(exercise, root);
			mapper.writeValue(new File(path + ".json"), root);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mapper.disable(SerializationFeature.INDENT_OUTPUT);
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

	public static Exercise jsonStringToExercise(String jsonString) {
		Exercise exercise = null;
		try {
			exercise = mapper.readValue(jsonString, BlankExercise.class);
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

	public static ObjectNode exerciseToJSON(Exercise exercise) {
		return (ObjectNode) mapper.convertValue(exercise, JsonNode.class);
	}

	public static ObjectNode exerciseToJudgeJSON(Exercise exercise) {
		ObjectNode root = exerciseToJSON(exercise);
        root.remove("instructions");
        root.remove("helps");
		fixTypeEntities(exercise, root);
		removeSteps(exercise, root);

		// Remove the doc of the worlds
		for(int i=0; i<exercise.getWorldCount(); i++) {
			ObjectNode w = (ObjectNode) root.path("answerWorlds").path(i);
			w.remove("about");
			w = (ObjectNode) root.path("initialWorlds").path(i);
			w.remove("about");
		}

        return root;
	}

	public static ObjectNode exerciseToClientJSON(Exercise exercise, String code, String selectedWorldID, String toolbox) {
		ObjectNode root = exerciseToJSON(exercise);
		Locale humanLang = exercise.getSettings().getHumanLang();
		ProgrammingLanguage progLang = exercise.getSettings().getProgLang();

        root.remove("defaultSourceFiles");
        removeSteps(exercise, root);

        root.put("instructions", exercise.getMission(humanLang, progLang));
        root.put("help", exercise.getHelp(humanLang, progLang));
        root.put("code", code);
        root.put("selectedWorldID", selectedWorldID);
        root.put("toolbox", toolbox);

        return root;
	}

	public static void fixTypeEntities(Exercise exercise, ObjectNode json) {
		String typeEntities = json.path("initialWorlds").path(0).path("entities").path(0).path("type").asText();
        int nbWorlds = exercise.getWorldCount();
        for(int i=0; i<nbWorlds; i++) {
        	int nbEntities = exercise.getInitialWorlds().get(i).getEntityCount();
        	for(int j=0; j<nbEntities; j++) {
        		ObjectNode entity = (ObjectNode) json.path("answerWorlds").path(i).path("entities").path(j);
        		entity.put("type", typeEntities);
        	}
        }
	}

	public static void removeSteps(Exercise exercise, ObjectNode json) {
		// Only need to remove steps from answerWorlds
		int nbWorlds = exercise.getWorldCount();
		for(int i=0; i<nbWorlds; i++) {
			ObjectNode answerWorld = (ObjectNode) json.path("answerWorlds").path(i);
			answerWorld.remove("steps");
		}
	}

	public static String operationsToJSON(World world, int bufferSize) {
		int nbSteps = world.getSteps().size();

		if(bufferSize <= 0 ) {
			bufferSize = nbSteps;
		} else {
			bufferSize = Math.min(nbSteps, bufferSize);
		}

		List<List<SVGOperation>> steps = new ArrayList<List<SVGOperation>>();
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

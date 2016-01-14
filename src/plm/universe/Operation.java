package plm.universe;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import plm.core.model.ToJSON;

public abstract class Operation implements ToJSON {
	private String name;

	@SuppressWarnings("unchecked")
	public static void addOperationsToBuffer(JSONArray buffer, String worldID, List<Operation> operations) {
		JSONObject mapArgs = new JSONObject();

		JSONArray jsonOperations = new JSONArray();
		for(Operation operation: operations) {
			jsonOperations.add(operation.toJSON());
		}

		mapArgs.put("operations", jsonOperations);
		mapArgs.put("worldID", worldID);

		buffer.add(mapArgs);
	}

	@SuppressWarnings("unchecked")
	public static String operationsBufferToMsg(JSONArray buffer) {
		JSONObject bufferJson = new JSONObject();
		bufferJson.put("buffer", buffer);

		JSONObject mapArgs = new JSONObject();
		mapArgs.put("args", bufferJson);

		String message = mapArgs.toString();

		// Hack to start with {"cmd":"operations", ... }
		message = "{\"cmd\":\"operations\"," + message.substring(1);

		return message;
	}

	public Operation(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("type",  name);
		return json;
	}
}

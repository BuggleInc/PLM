package plm.universe;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@jsonId")
public abstract class Operation {
	private String name;

	@SuppressWarnings("unchecked")
	public static void addOperationsToBuffer(JSONArray buffer, String worldID, List<Operation> operations) {
		JSONObject mapArgs = new JSONObject();

		JSONArray jsonOperations = new JSONArray();
		for(Operation operation: operations) {
			//jsonOperations.add(operation.toJSON());
		}

		mapArgs.put("operations", jsonOperations);
		mapArgs.put("worldID", worldID);

		buffer.add(mapArgs);
	}

	@SuppressWarnings("unchecked")
	public static String operationsBufferToMsg(String cmd, JSONArray buffer) {
		JSONObject bufferJson = new JSONObject();
		bufferJson.put("buffer", buffer);

		JSONObject mapArgs = new JSONObject();
		mapArgs.put("args", bufferJson);

		String message = mapArgs.toString();

		// Hack to start with {"cmd":"operations", ... }
		message = "{\"cmd\":\"" + cmd + "\"," + message.substring(1);

		return message;
	}

	public Operation(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	// TODO: Implement in all subclasses
	// public abstract Operation clone();
}

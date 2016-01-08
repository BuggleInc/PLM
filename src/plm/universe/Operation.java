package plm.universe;

import org.json.simple.JSONObject;

import plm.core.model.ToJSON;

public abstract class Operation implements ToJSON {
	private String name;
	
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

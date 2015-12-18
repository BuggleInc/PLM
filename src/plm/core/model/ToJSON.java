package plm.core.model;

import org.json.simple.JSONObject;

public interface ToJSON {
	public JSONObject toJSON();
	public default String getJSONType() {
        return this.getClass().getName();
    }
}

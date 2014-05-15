package plm.core.model;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.UUID;

import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

public class User implements JSONStreamAware {
	private String username;
	private boolean lastUsed;
	private UUID userUUID;

	public User(String username) {
		this.username = username;
		this.lastUsed = true;
		this.userUUID = UUID.randomUUID();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void writeJSONString(Writer out) throws IOException {
		LinkedHashMap obj = new LinkedHashMap();
		obj.put("username", username);
		obj.put("lastUsed", lastUsed);
		obj.put("userUUID", String.valueOf(userUUID));
		JSONValue.writeJSONString(obj, out);
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", lastUsed=" + lastUsed + ", userUUID=" + userUUID + "]";
	}

}

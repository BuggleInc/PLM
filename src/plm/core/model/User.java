package plm.core.model;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.UUID;

import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

public class User implements JSONStreamAware {
	private String username;
	private boolean lastUsed;
	private UUID userUUID;

	public User(String username) {
		this.username = username;
		this.lastUsed = false;
		this.userUUID = UUID.randomUUID();
	}

	public User(String username, boolean lastUsed, UUID userUUID) {
		this.username = username;
		this.lastUsed = lastUsed;
		this.userUUID = userUUID;
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
		return username + " [" + String.valueOf(userUUID).substring(0, 8) + "]";
	}

	public String toStringExtended() {
		return "User [username=" + username + ", lastUsed=" + lastUsed + ", userUUID=" + userUUID + "]";
	}

	/**
	 * Getters and Setters
	 */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(boolean lastUsed) {
		this.lastUsed = lastUsed;
	}

	
	public String getUserUUIDasString() {
		return this.userUUID.toString();
	}
	
	public UUID getUserUUID() {
		return userUUID;
	}
	
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 29 * hash + Objects.hashCode(this.userUUID);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final User other = (User) obj;
		if (! this.userUUID.equals(other.userUUID)) {
			return false;
		}
		return true;
	}

}

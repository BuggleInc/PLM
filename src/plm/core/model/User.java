package plm.core.model;

import java.io.IOException;
import java.io.File;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.UUID;

import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;

public class User implements JSONStreamAware {
	static final String DEFAULT_EXERCISE = "plm://lessons.welcome/";
	
	private String username;
	private boolean lastUsed;
	private UUID userUUID;
	private String currentExo = DEFAULT_EXERCISE;

	public User() {
		username = System.getenv("USER");
		if (username == null) 
			username = System.getenv("USERNAME");
		if (username == null) 
			username = "John Doe";

		lastUsed = false;
		userUUID = UUID.randomUUID();
		final File gitDir = new File(
				Game.getSavingLocation() + System.getProperty("file.separator") + getUserUUIDasString());
		if (!gitDir.exists()) {
			gitDir.mkdir();
		}
		System.err.println(Game.i18n.tr("A new PLM user has been created for you: {0}. Save directory: {1}", this, gitDir.toString()));
	}

	public User(String username, String uuid) {
		if (username == null || username.equals(""))
			username = System.getenv("USER");
		if (username == null)
			username = System.getenv("USERNAME");
		if (username == null)
			username = "John Doe";
		this.username = username;
		if (uuid == null || uuid.equals("")) {
			this.userUUID = UUID.randomUUID();
		} else {
			this.userUUID = UUID.fromString(uuid);
		}
	}

	private Object getOr(HashMap<String, Object> map, String key, Object defaultValue) {
		if (map.containsKey(key))
			return map.get(key);
		return defaultValue;
	}

	public User(HashMap<String, Object> map) {
		username = (String) getOr(map, "username", "John Doe");
		lastUsed = (Boolean) getOr(map, "lastUsed", false);
		String strUUID = (String) getOr(map, "userUUID", "");
		if (strUUID.equals("")) {
			this.userUUID = UUID.randomUUID();
		} else {
			this.userUUID = UUID.fromString(strUUID);
		}
		currentExo = (String) getOr(map, "exercise", DEFAULT_EXERCISE);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void writeJSONString(Writer out) throws IOException {
		LinkedHashMap obj = new LinkedHashMap();
		obj.put("username", username);
		obj.put("lastUsed", lastUsed);
		obj.put("userUUID", String.valueOf(userUUID));
		obj.put("exercise", currentExo);
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
		Game.getInstance().getUsers().flushUsersToFile();
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

	public String getCurrentExercise() {
		return currentExo;
	}

	public void setCurrentExercise(String curExo) {
		currentExo = curExo;
		Game.getInstance().getUsers().flushUsersToFile();
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

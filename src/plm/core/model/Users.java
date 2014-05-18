package plm.core.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * This class handles the insertion and deletion of users from the plm.users file.
 * 
 */
public class Users {
	private String filePath;
	private File userFile;
	private String username;
	private boolean lastUsed;
	private UUID userUUID;
	private JSONArray users;
	private List<User> usersList;

	public Users(File path) {
		username = System.getenv("USER");
		if (username == null) {
			username = System.getenv("USERNAME");
		}
		if (username == null) {
			username = "John Doe";
		}

		filePath = path.getAbsolutePath() + System.getProperty("file.separator") + "plm.users";
		userFile = new File(filePath);

		parseFile(filePath);

		// System.err.println("The file has been parsed successfully!");
		// System.err.println(users.toJSONString());

		loadUsersFromFile();
		// System.err.println("The users have been loaded successfully!");
		for (User user : usersList) {
			System.err.println("User found: " + user);
		}
	}

	/**
	 * Get the last used user. If such a user doesn't exist, meaning that all lastUsed fields are set to false, the
	 * method then sets the lastUsed variable of the firstUser to true, meaning that he becomes the last used user.
	 * 
	 * @return the last used user (which should logically also be the current user)
	 */
	public User getCurrentUser() {
		for (User user : usersList) {
			if (user.isLastUsed()) {
				System.err.println("Last user found: " + user);
				return user;
			}
		}

		// everytime we set something on an existing user, we should update the
		// plm.users file, as there is no other way to update it yet (there is
		// no listener for when the plm exits)
		usersList.get(0).setLastUsed(true);
		updateUsersFile();

		return usersList.get(0);
	}

	/**
	 * This method turns each user found in plm.users and parsed into the JSONArray user.
	 */
	@SuppressWarnings("rawtypes")
	private void loadUsersFromFile() {
		usersList = new ArrayList<User>();

		String jsonText = users.toJSONString();
		JSONParser parser = new JSONParser();
		ContainerFactory containerFactory = new ContainerFactory() {
			public List creatArrayContainer() {
				return new LinkedList();
			}

			public Map createObjectContainer() {
				return new LinkedHashMap();
			}
		};

		if (userFile.exists()) {
			try {
				List json = (List) parser.parse(jsonText, containerFactory);
				Iterator iter = json.iterator();
				// System.out.println("==iterate result==");

				while (iter.hasNext()) {
					LinkedHashMap entry = (LinkedHashMap) iter.next();
					username = (String) entry.get("username");
					lastUsed = (boolean) entry.get("lastUsed");
					userUUID = UUID.fromString((String) entry.get("userUUID"));
					usersList.add(new User(username, lastUsed, userUUID));
					// System.out.println(usersList.get(0));
				}

				// System.out.println("==toJSONString()==");
				// System.out.println(JSONValue.toJSONString(json));
			} catch (ParseException pe) {
				System.out.println(pe);
			}
		} else {
			// if the plm.users file doesn't exist yet, it means that no users have been created, so we create one first
			// user.
			User user = new User(username);
			usersList.add(user);
			updateUsersFile();
			System.err.println("A new user has been created for you!");
			System.err.println(user);
		}
	}

	/**
	 * Write the ArrayList of User in the JSONArray users. Doing so means that we update the plm.users file with the
	 * latest changes. This method should always be called after using a Setter from User.
	 */
	@SuppressWarnings("unchecked")
	public void updateUsersFile() {
		FileWriter fwUser;
		users = new JSONArray();

		try {
			fwUser = new FileWriter(userFile.getAbsoluteFile());
			BufferedWriter bwUser = new BufferedWriter(fwUser);

			for (User user : usersList) {
				users.add(user);
			}

			StringWriter out = new StringWriter();
			users.writeJSONString(out);
			// System.out.println(out.toString());

			bwUser.write(out.toString());
			bwUser.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read the plm.users file and put the results in a JSONArray.
	 * 
	 * @param filePath
	 *            the path to the plm.users file
	 */
	private void parseFile(String filePath) {
		JSONParser parser = new JSONParser();

		if (userFile.exists()) {
			try {
				users = (JSONArray) parser.parse(new FileReader(filePath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			users = new JSONArray();
		}
	}

}

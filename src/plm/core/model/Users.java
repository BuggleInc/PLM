package plm.core.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
	private JSONArray users;

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

		if (!userFile.exists()) {
			User user = new User(username);
			addUser(user);
			System.err.println("A new user has been created for you!");
			System.err.println(user);
		}

		System.err.println("The file has been parsed successfully!");
		System.err.println(users.toJSONString());
	}

	@SuppressWarnings("unchecked")
	public void addUser(User user) {
		FileWriter fwUser;
		try {
			fwUser = new FileWriter(userFile.getAbsoluteFile());
			BufferedWriter bwUser = new BufferedWriter(fwUser);

			users.add(user);
			StringWriter out = new StringWriter();
			users.writeJSONString(out);
			// System.out.println(out.toString());

			bwUser.write(out.toString());
			bwUser.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public User getCurrentUser() {
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

		try {
			List json = (List) parser.parse(jsonText, containerFactory);
			Iterator iter = json.iterator();
			System.out.println("==iterate result==");

			while (iter.hasNext()) {
				LinkedHashMap entry = (LinkedHashMap) iter.next();
				System.out.println(entry.get("userUUID"));
			}

			System.out.println("==toJSONString()==");
			System.out.println(JSONValue.toJSONString(json));
		} catch (ParseException pe) {
			System.out.println(pe);
		}

		return null;
	}

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

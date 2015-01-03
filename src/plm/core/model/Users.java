package plm.core.model;

import java.io.BufferedWriter;
import java.io.File;
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
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import plm.core.UserSwitchesListener;
import plm.core.utils.FileUtils;

/**
 * This class handles the insertion and deletion of users from the plm.users file.
 * 
 */
public class Users {
	private File userDBFile;
	private List<User> usersList;

	public Users(File path) {
		String userDBFilePath = path.getAbsolutePath() + System.getProperty("file.separator") + "plm.users";
		this.userDBFile = new File(userDBFilePath);

		loadUsersFromFile(userDBFile);
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
				return user;
			}
		}

		// In theory, we should save the file to remember that this was the lastly used user
		// In practice, we don't care: we'll mark the first of the list every time, so don't bother
		usersList.get(0).setLastUsed(true);

		return usersList.get(0);
	}

	/** 
	 * Get the rank in the list of the currently selected user
	 */
	public int getCurrentUserRank() {
		for (int cpt=0; cpt<usersList.size(); cpt++)
			if (usersList.get(cpt).isLastUsed())
				return cpt;
		return 0;
	}
	
	/**
	 * Add a new user.
	 * 
	 * @param username
	 *            the name of the new user
	 * @param uuid the UUID to pick for that user (or null or empty if the UUID should be generated)
	 * @throws IllegalArgumentException
	 * 		      if the provided UUID is not valid
	 */
	public void addUser(String username, String uuid) throws IllegalArgumentException {
		User user;
		if (uuid == null || uuid.equals("")) {
			user = new User(username);
		} else {
			user = new User(username, false, UUID.fromString(uuid));
		}
		
		if(!usersList.contains(user)) {
			usersList.add(user);
			try {
				switchToUser(user);
			} catch (UserAbortException e) {
				System.err.println("Error while switching users");
				e.printStackTrace();
			}
		} else {
			System.err.println(Game.i18n.tr("User {0} exists already; don't add it again.", uuid));
		}
		flushUsersToFile();
	}

	/**
	 * Method used to switch between users.
	 * 
	 * @param newUser
	 *            the user to which we want to switch
	 * @return true if the user has been found and has been changed
	 * @throws UserAbortException: Saving the current session went bad
	 */
	public boolean switchToUser(User newUser) throws UserAbortException {
		boolean found = false;
		Game g = Game.getInstance();

		g.saveSession();
		
		for (User user : usersList) {
			if (user.getUserUUIDasString().equals(newUser.getUserUUIDasString())) {
				found = true;
				getCurrentUser().setLastUsed(false);
				user.setLastUsed(true);
				System.err.println(Game.i18n.tr("Switched to user: {0}", newUser));
			} 
		}

		if (found) {
			flushUsersToFile();
			fireUserSwitch(newUser);
			g.loadSession();
		} else {
			System.err.println("Cannot switch to the user "+newUser+": not found");
		}

		return found;
	}

	/**
	 * Delete an existing user.
	 * 
	 * @param user
	 *            the user to remove
	 */
	public void removeUser(User user) {
		File gitDir = new File(Game.getSavingLocation() + System.getProperty("file.separator") + user.getUserUUIDasString());
		FileUtils.deleteRecursive(gitDir);
		usersList.remove(user);
		flushUsersToFile();
	}

	/**
	 * This method read each user definition from the plm.users JSON file. 
	 * If the file does not exist, it is created.
	 * 
	 * @param userDBFile
	 *            the file representing the plm.users
	 */
	@SuppressWarnings("rawtypes")
	private void loadUsersFromFile(File userDBFile) {
		this.usersList = new ArrayList<User>();

		if (!this.userDBFile.exists()) {
			// if the plm.users file doesn't exist yet, it means that no users
			// have been created, so we create one first user.

			String username = System.getenv("USER");
			if (username == null) {
				username = System.getenv("USERNAME");
			}
			if (username == null) {
				username = "John Doe";
			}
			User user = new User(username);
			this.usersList.add(user);
			flushUsersToFile();
			System.err.println(Game.i18n.tr("A new PLM user has been created for you!"));
			System.err.println(user);
		} else {
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
				List json = (List) parser.parse(new FileReader(userDBFile), containerFactory);
				Iterator iter = json.iterator();

				while (iter.hasNext()) {
					LinkedHashMap entry = (LinkedHashMap) iter.next();
					String username = (String) entry.get("username");
					boolean lastUsed = (Boolean) entry.get("lastUsed");
					UUID userUUID = UUID.fromString((String) entry.get("userUUID"));
					usersList.add(new User(username, lastUsed, userUUID));
				}

			} catch (ParseException | IOException pe) {
				System.out.println(pe);
			}
		}
	}

	/**
	 * Write the ArrayList of User in the JSONArray users. Doing so means that we update the plm.users file with the
	 * latest changes. This method should always be called after using a Setter from User.
	 */
	@SuppressWarnings("unchecked")
	public void flushUsersToFile() {
		FileWriter fwUser;
		JSONArray users = new JSONArray();

		try {
			fwUser = new FileWriter(userDBFile);
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
	 * Getters and Setters
	 */
	public List<User> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<User> usersList) {
		this.usersList = usersList;
	}

	/**
	 * User switches listeners
	 */
	private List<UserSwitchesListener> userSwitchesListeners = new Vector<UserSwitchesListener>();
	public void addUserSwitchesListener(UserSwitchesListener l) {
		userSwitchesListeners.add(l);
	}
	public void fireUserSwitch(User newUser) {
		for (UserSwitchesListener l : userSwitchesListeners)
			l.userHasChanged(newUser);
	}
	public void removeUserSwitchesListener(UserSwitchesListener l) {
		this.userSwitchesListeners.remove(l);
	}
}

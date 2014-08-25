package plm.core;

import plm.core.model.User;

public interface UserSwitchesListener {
    // when the current user is switched
	public void userHasChanged(User newUser);

}

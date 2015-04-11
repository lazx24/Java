package com.coscon.shipsuite.common.session;

import com.coscon.shipsuite.common.util.timetask.GenericTimeTask;
import org.springframework.stereotype.Component;

@Component("sessionRecycleManager")
public final class SessionRecycleManager extends GenericTimeTask {
    public SessionRecycleManager() {
	super(SessionManager.getInstance());
    }

    public SessionRecycleManager(int recycleDurationMinutes) {
	super(SessionManager.getInstance(), recycleDurationMinutes * 60);
    }

    public void setSessionTimeout(long sessionTimeout) {
	SessionManager.getInstance().setSessionTimeout(sessionTimeout);
    }
}

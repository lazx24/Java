package com.common.servlet;

import com.common.exception.NoSecurityTokenException;
import com.common.exception.SessionTimeoutException;
import com.common.session.ISession;
import com.common.session.SessionManager;
import com.common.util.string.StringUtil;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractServlet extends HttpServlet {
    private static final long serialVersionUID = -5626673506858149202L;

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	checkSecurityToken(req);

	super.doGet(req, resp);
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	checkSecurityToken(req);

	super.doDelete(req, resp);
    }

    protected void doHead(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	checkSecurityToken(req);

	super.doHead(req, resp);
    }

    protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	checkSecurityToken(req);

	super.doOptions(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	checkSecurityToken(req);

	super.doPost(req, resp);
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	checkSecurityToken(req);

	super.doPut(req, resp);
    }

    protected void doTrace(HttpServletRequest req, HttpServletResponse resp)
	    throws ServletException, IOException {
	checkSecurityToken(req);

	super.doTrace(req, resp);
    }

    public boolean checkSecurityToken(HttpServletRequest request) {
	String token = request.getParameter("Token");
	String userId = request.getParameter("UserId");
	if ((StringUtil.isNullOrEmpty(token))
		|| (StringUtil.isNullOrEmpty(userId))) {
	    throw new NoSecurityTokenException();
	}
	ISession session = SessionManager.getInstance().getSession(token);
	if ((session != null) && (userId.equals(session.getUserId()))) {
	    return true;
	}
	throw new SessionTimeoutException();
    }
}

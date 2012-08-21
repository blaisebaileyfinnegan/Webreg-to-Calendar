package edu.uci.mvu1.webreg2cal;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AuthServlet extends AbstractAppEngineAuthorizationCodeServlet {
    private static final Logger log = Logger.getLogger(AuthServlet.class.getName());

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
                throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        
        resp.sendRedirect("/welcome.jsp");
    }

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException,
			IOException {
		return Utils.newFlow();
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		return Utils.getRedirectUri(req);
	}
}
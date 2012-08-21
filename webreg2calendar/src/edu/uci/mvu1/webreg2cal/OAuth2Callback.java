package edu.uci.mvu1.webreg2cal;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeCallbackServlet;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OAuth2Callback extends AbstractAppEngineAuthorizationCodeCallbackServlet {

	@Override
	protected void onSuccess(HttpServletRequest req, HttpServletResponse resp, Credential credential) throws ServletException, IOException 
	{
		resp.sendRedirect("/welcome.jsp");
	}
	
	@Override
	protected void onError(HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse) throws ServletException, IOException 
	{
		String nickname = UserServiceFactory.getUserService().getCurrentUser().getNickname();
		resp.getWriter().print("You must log in to use this application! <a href=\"window.history.back()\">Go back</a>");
		resp.setStatus(200);
		resp.addHeader("Content-Type", "text/html");
	}
	
	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException,
			IOException {
		// TODO Auto-generated method stub
		return Utils.newFlow();
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		return Utils.getRedirectUri(req);
	}

}

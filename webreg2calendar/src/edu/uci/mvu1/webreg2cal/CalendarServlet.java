package edu.uci.mvu1.webreg2cal;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.ValidationException;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.Calendar;


public class CalendarServlet extends AbstractAppEngineAuthorizationCodeServlet {
	private static final Logger log = Logger.getLogger(AuthServlet.class.getName());
	
	private static String calendarId = "";
	
	public static final String ERROR_INVALID_QUARTER = "1";
	public static final String ERROR_INVALID_STUDYLIST = "2";
	public static final String ERROR_EMPTY_CALENDAR_NAME = "3";
	
	private static com.google.api.services.calendar.Calendar client;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
	{
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
		try {
			client = Utils.loadCalendarClient();
			
			String schedule = req.getParameter("content");
			String name = req.getParameter("name");
			String quarter = req.getParameter("quarter");
			String format = req.getParameter("submit");
			
			ArrayList<Class> classes = null;
			try
			{
				log.warning(schedule);
				classes = Parser.parseSchedule(schedule);
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
			}
			catch (NoSuchElementException e)
			{
			}
			catch (NumberFormatException e)
			{
			}
			
			Calendar instructionBegins = null, instructionEnds = null;
			
			ArrayList<Quarter> quarters;
			try {
				quarters = QuarterManager.getQuarters();
				for (Quarter q : quarters)
				{
					if (q.getTitle().equals(quarter))
					{
						instructionBegins = q.getInstructionBegins();
						instructionEnds = q.getInstructionEnds();
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String errorCode = null;
			if (instructionBegins == null || instructionEnds == null)
				errorCode = ERROR_INVALID_QUARTER;
			else if (name.isEmpty())
				errorCode = ERROR_EMPTY_CALENDAR_NAME;
			else if (schedule.isEmpty() || classes == null || classes.isEmpty())
				errorCode = ERROR_INVALID_STUDYLIST;
			
			if (errorCode != null)
			{
				resp.sendRedirect("/error.jsp?error=" + errorCode);
			}
			else
			{
				ArrayList<CalendarClass> calendarClasses = CalendarBuilder.initializeCalendar(resp, client, classes, instructionBegins, instructionEnds);
				if (format.equals("Google Calendar"))
				{
					GoogleCalendar.CreateCalendar(resp, client, calendarClasses, name, instructionBegins, instructionEnds);
					resp.sendRedirect("/google_success.jsp");
				}
				else
				{
					net.fortuna.ical4j.model.Calendar calendar = iCalendar.CreateCalendar(calendarClasses, name);
					CalendarOutputter outputter = new CalendarOutputter();
					
					FileService fileService = FileServiceFactory.getFileService();
					AppEngineFile file = fileService.createNewBlobFile("text/calendar");
					boolean lock = true;
					FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);
	
					StringWriter writer = new StringWriter();
					try {
						outputter.output(calendar, writer);
					} catch (ValidationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					writeChannel.write(ByteBuffer.wrap(writer.toString().getBytes()));
					writeChannel.closeFinally();
					
					BlobKey blobKey = fileService.getBlobKey(file);
					
					if (blobKey == null)
					{
						resp.sendRedirect("/ical_failure.jsp");
					}
					else
					{
						resp.sendRedirect("/ical_success.jsp?name=" + name + "&blob-key=" + blobKey.getKeyString());
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public static void setCalendarId(String id) {
		calendarId = id;
		
	}
}

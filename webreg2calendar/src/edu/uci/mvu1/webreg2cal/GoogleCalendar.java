package edu.uci.mvu1.webreg2cal;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.*;


public class GoogleCalendar 
{
	public static String calendarId;
	
    private static final Logger log = Logger.getLogger(GoogleCalendar.class.getName());

	private static final String RFC_MONDAY = "Mo";
	private static final String RFC_TUESDAY = "Tu";
	private static final String RFC_WEDNESDAY = "We";
	private static final String RFC_THURSDAY = "Th";
	private static final String RFC_FRIDAY = "Fr";
	
	public static void CreateCalendar(final HttpServletResponse resp, final com.google.api.services.calendar.Calendar client, final ArrayList<CalendarClass> classes,  String name,
			final java.util.Calendar instructionBegins, final java.util.Calendar instructionEnds) throws IOException
	{
		BatchRequest batch = client.batch();
		com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar().setSummary(name);
		calendar.setTimeZone("America/Los_Angeles");
		
		client.calendars().insert(calendar).queue(batch, 
				new JsonBatchCallback<com.google.api.services.calendar.model.Calendar>() {

					@Override
					public void onSuccess(
							com.google.api.services.calendar.model.Calendar t,
							GoogleHeaders responseHeaders) {
							BatchRequest b;
							try {
								CreateEvents(resp, client, classes, t.getId());
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} 
						
					@Override
					public void onFailure(GoogleJsonError e,
							GoogleHeaders responseHeaders) throws IOException {
						log.warning(e.getMessage());
					}
					
				});
		batch.execute();
	}
	
	public static void CreateEvents(final HttpServletResponse resp, com.google.api.services.calendar.Calendar client, ArrayList<CalendarClass> classes, String calendarId) throws IOException
	{
		BatchRequest b = client.batch();
		
		int color = 1;
		for (CalendarClass c : classes)
		{	
			
			Event event = new Event();
	
			
			EventDateTime beginDateTime = new EventDateTime();
			beginDateTime.setDateTime(new DateTime(c.getStartCalendar().getTime(), TimeZone.getTimeZone("America/Los_Angeles")));
			beginDateTime.setTimeZone("America/Los_Angeles");
			event.setStart(beginDateTime);
			
		
			EventDateTime endDateTime = new EventDateTime();
			endDateTime.setDateTime(new DateTime(c.getEndCalendar().getTime(), TimeZone.getTimeZone("America/Los_Angeles")));
			endDateTime.setTimeZone("America/Los_Angeles");
			event.setEnd(endDateTime);
			
			color = (color+1)%11;

			event.setSummary(c.getC().getDept() + " " + c.getC().getCourseNumber() + " " + c.getC().getType());
			event.setLocation(c.getC().getBuilding() + " " + c.getC().getRoom());
			event.setDescription("Course Code: " + c.getC().getCourseCode());
			event.setColorId(String.valueOf(color));
				
			c.getRecurrence().set(0, "RRULE:" + c.getRecurrence().get(0));
			event.setRecurrence(c.getRecurrence());
			
			client.events().insert(calendarId, event).queue(b, new JsonBatchCallback<com.google.api.services.calendar.model.Event>(){
				@Override
				public void onFailure(GoogleJsonError e,
						GoogleHeaders responseHeaders) throws IOException {
					log.warning(e.getMessage());
				}

				@Override
				public void onSuccess(Event t, GoogleHeaders responseHeaders) {
					log.warning(t.toString());
				}
				
			});
		}
		
		
		b.execute();
	}
}

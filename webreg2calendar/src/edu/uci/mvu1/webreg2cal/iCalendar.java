package edu.uci.mvu1.webreg2cal;

import java.net.SocketException;
import java.text.ParseException;
import java.util.ArrayList;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.RRule;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.SimpleHostInfo;
import net.fortuna.ical4j.util.UidGenerator;

public class iCalendar {
	public static Calendar CreateCalendar(ArrayList<CalendarClass> classes, String name) throws SocketException
	{
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone("America/Los_Angeles");
		VTimeZone tz = timezone.getVTimeZone();
		
		Calendar iCalendar = new Calendar();
		iCalendar.getProperties().add(new ProdId(name));
		iCalendar.getProperties().add(Version.VERSION_2_0);
		iCalendar.getProperties().add(CalScale.GREGORIAN);
		iCalendar.getComponents().add(tz);
		
		for (CalendarClass c : classes)
		{

			
			DateTime start = new DateTime(c.getStartCalendar().getTime());
			start.setTimeZone(timezone);
			DateTime end = new DateTime(c.getEndCalendar().getTime());
			end.setTimeZone(timezone);
			
			String eventName = c.getC().getDept() + " " + c.getC().getCourseNumber() + " " + c.getC().getType();
			String eventLocation = c.getC().getBuilding() + " " + c.getC().getRoom();
			VEvent event = new VEvent(start, end, eventName);
			
			event.getProperties().add(tz.getTimeZoneId());
			
			UidGenerator ug = new UidGenerator(new SimpleHostInfo("uci.edu"), "1");
			Uid uid = ug.generateUid();
			event.getProperties().add(uid);
			
			RRule rule = new RRule();
			try {
				rule.setValue(c.getRecurrence().get(0));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			event.getProperties().add(new Location(eventLocation));
			event.getProperties().add(rule);
			
			iCalendar.getComponents().add(event);
		}
		
		return iCalendar;
	}
}

package edu.uci.mvu1.webreg2cal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletResponse;

public class CalendarBuilder {
	public static ArrayList<CalendarClass> initializeCalendar(final HttpServletResponse resp, com.google.api.services.calendar.Calendar client, ArrayList<Class> classes, 
			java.util.Calendar instructionBegins, java.util.Calendar instructionEnds) {
		
		ArrayList<CalendarClass> calendarClasses = new ArrayList<CalendarClass>();
		for (Class c : classes)
		{
			java.util.Calendar startCalendar = java.util.Calendar.getInstance();
			java.util.Calendar endCalendar = java.util.Calendar.getInstance();
			
			if (c.isTba())
				continue;
			
			int beginInstructionDayOfWeek = instructionBegins.get(Calendar.DAY_OF_WEEK) - 2;
			int firstDayOfClass = -1;
			for (int i = 0; i < 5; i++)
			{
				if (c.getDay(i))
				{
					if (firstDayOfClass == -1)
						firstDayOfClass = i;
					if(i >= beginInstructionDayOfWeek)
					{
						firstDayOfClass = i;
						break;
					}
				}
			}
			
			int instructionGap = 0;
			if (beginInstructionDayOfWeek > firstDayOfClass)
			{
				instructionGap = 7 - beginInstructionDayOfWeek + firstDayOfClass;
			}
			else
			{
				instructionGap = firstDayOfClass-beginInstructionDayOfWeek;
			}
			
			
			int beginHour = (int)Math.floor((float)c.getBeginTime()/100f);
	
			startCalendar.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
			startCalendar.set(Calendar.MONTH, instructionBegins.get(Calendar.MONTH));
			startCalendar.set(Calendar.DAY_OF_MONTH, instructionBegins.get(Calendar.DAY_OF_MONTH) + instructionGap);
			startCalendar.set(Calendar.HOUR_OF_DAY, beginHour);
			startCalendar.set(Calendar.MINUTE, c.getBeginTime()%100);
			
			
			int endHour = (int)Math.floor((float)c.getEndTime()/100f);
			
			endCalendar.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
			endCalendar.set(Calendar.MONTH, instructionBegins.get(Calendar.MONTH));
			endCalendar.set(Calendar.DAY_OF_MONTH, instructionBegins.get(Calendar.DAY_OF_MONTH) + instructionGap);
			endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
			endCalendar.set(Calendar.MINUTE, c.getEndTime()%100);
			
			ArrayList<String> recurrence = new ArrayList<String>();
			
			String year = String.valueOf(instructionEnds.get(Calendar.YEAR));
			String month = String.valueOf(instructionEnds.get(Calendar.MONTH) + 1);
			String dayOfMonth = String.valueOf(instructionEnds.get(Calendar.DAY_OF_MONTH));
			
			String recurringDays = "";
	
			if (c.getDay(0))
				recurringDays = recurringDays + "MO,";
			if (c.getDay(1))
				recurringDays = recurringDays + "TU,";
			if (c.getDay(2))
				recurringDays = recurringDays + "WE,";
			if (c.getDay(3))
				recurringDays = recurringDays + "TH,";
			if (c.getDay(4))
				recurringDays = recurringDays + "FR,";
				
			recurringDays = recurringDays.substring(0, recurringDays.length()-1);
	
			if (month.length() == 1)
				month = "0" + month;
			if (dayOfMonth.length() == 1)
				dayOfMonth = "0" + dayOfMonth;
			
			String endDate = year+month+dayOfMonth;
			recurrence.add("FREQ=WEEKLY;BYDAY=" + recurringDays + ";UNTIL=" + endDate);
			
			CalendarClass calendarClass = new CalendarClass(c, startCalendar, endCalendar, recurrence);
			calendarClasses.add(calendarClass);
		}
		return calendarClasses;
	}
}

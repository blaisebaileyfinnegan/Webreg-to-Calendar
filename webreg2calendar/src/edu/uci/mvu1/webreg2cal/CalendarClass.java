package edu.uci.mvu1.webreg2cal;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarClass{

	private edu.uci.mvu1.webreg2cal.Class c = null;
	private Calendar startCalendar = Calendar.getInstance();
	private Calendar endCalendar = Calendar.getInstance();
	private ArrayList<String> recurrence = new ArrayList<String>();
	
	CalendarClass(Class c, Calendar startCalendar, Calendar endCalendar, ArrayList<String> recurrence)
	{
		this.c = c;
		setStartCalendar(startCalendar);
		setEndCalendar(endCalendar);
		setRecurrence(recurrence);
	}
	
	public edu.uci.mvu1.webreg2cal.Class getC()
	{
		return c;
	}

	public Calendar getStartCalendar() {
		return startCalendar;
	}

	public void setStartCalendar(Calendar startCalendar) {
		this.startCalendar = startCalendar;
	}

	public Calendar getEndCalendar() {
		return endCalendar;
	}

	public void setEndCalendar(Calendar endCalendar) {
		this.endCalendar = endCalendar;
	}


	public ArrayList<String> getRecurrence() {
		return recurrence;
	}

	public void setRecurrence(ArrayList<String> recurrence) {
		this.recurrence = recurrence;
	}
}


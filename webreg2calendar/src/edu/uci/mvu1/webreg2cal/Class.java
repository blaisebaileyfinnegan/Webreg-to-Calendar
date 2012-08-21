package edu.uci.mvu1.webreg2cal;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Class {

	private int courseCode;
	private String dept;
	private String courseNumber;
	private String type;
	private char sectionNumber;
	private float units;
	private String gradeOption;
	private int beginTime;
	private int endTime;
	private int duration;
	private boolean[] days = new boolean[5];
	private boolean tba = false;
	private String building;
	private String room;
	
	Class()
	{
		
	}
	
	void setDay(int day, boolean present)
	{
		this.days[day] = present;
	}
	
	boolean getDay(int day)
	{
		return this.days[day];
	}
	
	int getCourseCode() {
		return courseCode;
	}
	void setCourseCode(int courseCode) {
		this.courseCode = courseCode;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public char getSection() {
		return sectionNumber;
	}
	public void setSection(char section) {
		this.sectionNumber = section;
	}
	public float getUnits() {
		return units;
	}
	public void setUnits(float units) {
		this.units = units;
	}
	public String getGradeOption() {
		return gradeOption;
	}
	public void setGradeOption(String gradeOption) {
		this.gradeOption = gradeOption;
	}
	public int getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public boolean isTba() {
		return tba;
	}

	public void setTba(boolean tba) {
		this.tba = tba;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}
	
	public String toString()
	{
		String daysText = "";
		for (int i = 0; i < 5; i++)
		{
			if(days[i])
			{
				daysText += "1";
			}
			else
			{
				daysText += "0";
			}
				
		}
		
		return courseCode + " " + dept + " " + courseNumber + " " + type + " " + sectionNumber + " " + units + " " + gradeOption + " " 
				+ daysText + " " + beginTime + " " + duration + " " + building + " " + room;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

}
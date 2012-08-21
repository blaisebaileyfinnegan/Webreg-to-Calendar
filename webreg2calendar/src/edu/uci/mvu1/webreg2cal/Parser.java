package edu.uci.mvu1.webreg2cal;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Parser {
	private static String newline = "(?=\\d{5})";

	public static ArrayList<Class> parseSchedule(String input) throws NoSuchElementException
	{	
		ArrayList<Class> schedule = new ArrayList<Class>();
		String[] classes = input.split(newline);
		for (String s : classes)
		{
			Class b = new Class();
			Scanner scanner = new Scanner(s);
			
			if (!scanner.hasNextInt())
				continue;
			
            
			//Crse           Crse     Sec      Grd                      
			//Code  Dept     Num  Typ Num Unts Opt    Days    Time          Bldg Room
			//
			//04005 MUSIC       8 LEC   A  4.0  GR      T T   12:30-01:50   SSLH 100       
			//30300 PHILOS     29 LEC A    4.0  GR     M W F  09:00-09:50   SSLH 100       
			//30303 PHILOS     29 DIS   3  0.0  GR     M      10:00-10:50   HICF 100M      
			//34130 COMPSCI   132 LEC   A  4.0  GR mc  M W    03:30-04:50   SSL  228       
			//34131 COMPSCI   132 DIS   1  0.0  GR    TBA     TBA  -TBA     TBA  TBA       
			//34350 COMPSCI   171 LEC   A  4.0  GR mc   T T   11:00-12:20   ICS  174       
			//34351 COMPSCI   171 DIS 1    0.0  GR mc  M      02:00-02:50   MSTB 118    
			
			int courseCode = scanner.nextInt();
			
			boolean readingDept = true;
			String dept = scanner.next();
			String next = "";
			ALL:
			while(readingDept)
			{
				next = scanner.next();
				for (int i = 0; i < next.length(); i++)
				{
					if (Character.isDigit(next.charAt(i)))
					{
						readingDept = false;
						break ALL;
					}
				}
				dept = dept + " " + next;
			}
			dept = dept.trim();
			
			String courseNumber = next;
			String type = scanner.next();
			char sectionNumber = scanner.next().charAt(0);
			float units = scanner.nextFloat();
			String gradeOption = scanner.next();
			
			b.setCourseCode(courseCode);
			b.setDept(dept);
			b.setCourseNumber(courseNumber);
			b.setType(type);
			b.setSection(sectionNumber);
			b.setUnits(units);
			b.setGradeOption(gradeOption);
			
			
			
			String rest = scanner.nextLine();
			
			boolean pretext = false;
			Scanner s2 = new Scanner(rest);
			String n2 = s2.next();
			if (n2.equals("mc"))
			{
				rest = s2.nextLine();
			}
			else if (n2.equals("TBA"))
			{
				b.setTba(true);
				schedule.add(b);
				continue;
			}
			
			rest = parseDays(b, rest).trim();
			
			scanner = new Scanner(rest);
			
			String rawTime = scanner.next();
			String building = scanner.next();
			String room = scanner.next();
			
			int[] intervals = processRawTime(rawTime);
			int duration = intervals[1] - intervals[0];
			
			b.setBeginTime(intervals[0]);
			b.setEndTime(intervals[1]);
			b.setDuration(duration);
			b.setBuilding(building);
			b.setRoom(room);
			
			schedule.add(b);
		}
		return schedule;
	}
	
	public static int[] processRawTime(String time)
	{
		String[] intervals = time.split("-");
		
		String[] beginTimes = intervals[0].split(":");
		if (beginTimes[1].length() == 4)
			beginTimes[1] = beginTimes[1].substring(0, 2);
			
		int beginTime = Integer.valueOf(beginTimes[0].concat(beginTimes[1]));
		
		boolean pm = false;
		String[] endTimes = intervals[1].split(":");
		if (endTimes[1].endsWith("pm"))
		{
			endTimes[1] = endTimes[1].substring(0, 2);
			pm = true;
		}
		
	
		int endTime = Integer.valueOf(endTimes[0].concat(endTimes[1]));
		if (pm)
		{
			endTime += 1200;
			beginTime += 1200;
		}
		
		return GuessTime(beginTime, endTime);
	}

	public static String parseDays(Class b, String text)
	{
		String[] str = text.split("(?=\\d{2}:\\d{2}-\\d{2}:\\d{2})");
		if (str[0].charAt(str[0].length()-3) == 'F')
		{
			b.setDay(4, true);
		}
		if (str[0].charAt(str[0].length()-4) == 'T')
		{
			b.setDay(3, true);
		}
		if (str[0].charAt(str[0].length()-5) == 'W')
		{
			b.setDay(2, true);
		}
		if (str[0].charAt(str[0].length()-6) == 'T')
		{
			b.setDay(1, true);
		}
		if (str[0].charAt(str[0].length()-7) == 'M')
		{
			b.setDay(0, true);
		}
		
		return str[1];
	}
	
	
	public static int[] GuessTime(int startTime, int endTime)
	{	
		if (startTime <= 530)
		{
			startTime += 1200;
			endTime += 1200;
		}
		else if (startTime > endTime)
		{
			endTime += 1200;
		}
		
		int[] intervals = {startTime, endTime};
		return intervals;
	}
}
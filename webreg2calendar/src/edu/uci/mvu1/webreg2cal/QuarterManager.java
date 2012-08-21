package edu.uci.mvu1.webreg2cal;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import sun.misc.IOUtils;

import com.google.appengine.labs.repackaged.com.google.common.base.Preconditions;

public class QuarterManager {
	
	private static final String RESOURCE_LOCATION = "/quarter_schedule.json";
	private static final String DATE_FORMAT = "yyyy.MM.dd.HH.mm.ss";
	
	private static final String KEY_TITLE = "title";
	private static final String KEY_INSTRUCTION_BEGINS = "begin";
	private static final String KEY_INSTRUCTION_ENDS = "end";
	
	
	private static ArrayList<Quarter> quarters = null;
	private static String quartersHTML = "";
	
	public static String retrieveQuartersHTML() throws java.text.ParseException
	{
		if (quartersHTML.isEmpty())
		{
			ArrayList<Quarter> qs = getQuarters();
			for (Quarter q : qs)
			{
				quartersHTML = "<option>" + q.getTitle() + "</option>" + quartersHTML;
			}
		}
		return quartersHTML;
	}
	
	public static ArrayList<Quarter> getQuarters() throws java.text.ParseException
	{
		if (quarters == null)
		{
			DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			JSONParser parser = new JSONParser();
			
			quarters = new ArrayList<Quarter>();
			
			try {
				InputStream inputStream = QuarterManager.class.getResourceAsStream(RESOURCE_LOCATION);
				Preconditions.checkNotNull(inputStream, "Missing resource %s", RESOURCE_LOCATION);
				String inputStreamString = new java.util.Scanner(inputStream).useDelimiter("\\A").next();
				Object obj = parser.parse(inputStreamString);
				JSONObject jsonObject = (JSONObject) obj;
				for (Object obj2 : jsonObject.values())
				{
					JSONObject jsonObject2 = (JSONObject) obj2;
					String title = (String) jsonObject2.get(KEY_TITLE);
					String begins = (String) jsonObject2.get(KEY_INSTRUCTION_BEGINS);
					String ends = (String) jsonObject2.get(KEY_INSTRUCTION_ENDS);
					
					Calendar instructionBegins = Calendar.getInstance();
					Calendar instructionEnds = Calendar.getInstance();
					
					instructionBegins.setTime(formatter.parse(begins));
					instructionEnds.setTime(formatter.parse(ends));
					
					quarters.add(new Quarter(title, instructionBegins, instructionEnds));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		
		return quarters;
	}

}

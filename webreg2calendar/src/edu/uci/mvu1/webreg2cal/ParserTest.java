package edu.uci.mvu1.webreg2cal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

public class ParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filename = args[0];
		
		try {
			String text = readFile(filename);
			ArrayList<Class> classes = Parser.parseSchedule(text);
			System.out.println ("Number of classes: " + classes.size());
			for (Class b : classes)
				System.out.println(b.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}

	}

	private static String readFile(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuilder stringBuilder = new StringBuilder();
		String ls = System.getProperty("line.separator");

		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
			stringBuilder.append(ls);
		}

		return stringBuilder.toString();
	}

}

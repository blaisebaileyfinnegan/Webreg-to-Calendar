package edu.uci.mvu1.webreg2cal;
import java.util.Calendar;

public class Quarter {
	
	private String title;
	private Calendar instructionBegins;
	private Calendar instructionEnds;
	
	Quarter(String title, Calendar begins, Calendar ends)
	{
		setTitle(title);
		setInstructionBegins(begins);
		setInstructionEnds(ends);
	}
	
	public Calendar getInstructionBegins() {
		return instructionBegins;
	}
	public void setInstructionBegins(Calendar instructionBegins) {
		this.instructionBegins = instructionBegins;
	}
	
	public Calendar getInstructionEnds() {
		return instructionEnds;
	}
	public void setInstructionEnds(Calendar instructionEnds) {
		this.instructionEnds = instructionEnds;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	

}

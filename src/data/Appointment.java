package data;

import java.util.ArrayList;
import java.util.Calendar;


public class Appointment {
	
	private int appointmentId;
	
	private String title;
	private String description;
	private Calendar startTime;
	private Calendar finishTime;
	private Room room;
	private User owner;
	
	private ArrayList<Participant> participants;

}

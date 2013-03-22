package data;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Appointment {
	
	private int appointmentId;
	private String title;
	private String description;
	private Calendar startTime;
	private Calendar finishTime;
	private Room room;
	private User owner;
	
	public Appointment(int appointmentId, String title, String description, long startTime, long finishTime, Room room, User owner) {
		this.appointmentId = appointmentId;
		this.title = title;
		this.description = description;
		this.startTime = new GregorianCalendar();
		this.startTime.setTimeInMillis(startTime);
		this.finishTime = new GregorianCalendar();
		this.finishTime.setTimeInMillis(finishTime);
		this.room = room;
		this.owner = owner;
	}
	
	public int getAppointmentId() {
		return appointmentId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Calendar getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}
	
	public Calendar getFinishTime() {
		return finishTime;
	}
	
	public void setFinishTime(Calendar finishTime) {
		this.finishTime = finishTime;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	public User getOwner() {
		return owner;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}

}

package data;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Participant {
	
	private Appointment appointment;
	private User user;
	private Calendar alarm;
	private String status;
	
	public Participant(Appointment appointment, User user, long alarm, String status) {
		this.appointment = appointment;
		this.user = user;
		this.alarm = new GregorianCalendar();
		this.alarm.setTimeInMillis(alarm);
		this.status = status;
	}
	
	public Appointment getAppointment() {
		return appointment;
	}
	
	public User getUser() {
		return user;
	}
	
	public Calendar getAlarm() {
		return alarm;
	}
	
	public void setAlarm(Calendar alarm) {
		this.alarm = alarm;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

}

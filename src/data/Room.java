package data;

public class Room {
	
	private String roomNumber;
	private int size;
	
	public Room(String roomNumber, int size) {
		this.roomNumber = roomNumber;
		this.size = size;
	}
	
	public String getRoomNumber() {
		return roomNumber;
	}
	
	public int getSize() {
		return size;
	}

}

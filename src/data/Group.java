package data;

public class Group {
	
	private String groupName;
	private User leader;
	
	public Group(String groupName, User leader) {
		this.groupName = groupName;
		this.leader = leader;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public User getLeader() {
		return leader;
	}

}

package data;

public class Member {
	
	private Group group;
	private User user;
	
	public Member(Group group, User user) {
		this.group = group;
		this.user = user;
	}
	
	public Group getGroup() {
		return group;
	}
	
	public User getUser() {
		return user;
	}

}

package data;

public class Subgroup {
	
	private Group parentGroup;
	private Group subgroup;
	
	public Subgroup(Group parentGroup, Group subgroup) {
		this.parentGroup = parentGroup;
		this.subgroup = subgroup;
	}
	
	public Group getParentGroup() {
		return parentGroup;
	}
	
	public Group getSubgroup() {
		return subgroup;
	}

}

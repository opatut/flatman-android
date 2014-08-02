package de.opatut.flatman.data;

import de.opatut.flatman.R;

public class Task {
	public int id;
	public String title;
	public String description;
	public String repeating;
	public String assignment;
	public int interval_days;
	public String interval_start;
	public boolean skippable;
	public String deadline;
	public int assignee_id;
	public String assignee;
	public int group_id;
	
	public int getRepeatingIcon() {
		if(repeating.equals("interval")) 
			return R.string.fa_repeat;
		else if(repeating.equals("ondemand"))
			return R.string.fa_flag;
		else if(repeating.equals("single"))
			return R.string.fa_dot_circle_o;
		else
			return R.string.fa_question_circle;
	}

	public int getAssignmentIcon() {
		if(assignment.equals("order")) 
			return R.string.fa_ellipsis_h;
		else if(assignment.equals("all"))
			return R.string.fa_users;
		else if(assignment.equals("one"))
			return R.string.fa_user;
		else
			return R.string.fa_question_circle;
	}
}

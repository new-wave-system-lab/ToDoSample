package blog.tsuchiya.todo;

public enum ToDoStatus {
	NOT_STARTED(1),
	PROGRESSiNG(2),
	END(3);
	
	private final int status;
	private ToDoStatus(int status) {
		this.status = status;
	}
	
	public int getInt() {
		return this.status;
	}
}

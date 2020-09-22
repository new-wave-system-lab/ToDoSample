package blog.tsuchiya.todo;

public enum ToDoStatus {
	NOT_STARTED(1, "未着手", "bg-primary"),
	PROGRESSiNG(2, "進行中", "bg-success"),
	END(3, "完了", "bg-secondary");
	
	private final Integer status;
	private final String name;
	private final String bgClass;
	private ToDoStatus(Integer status, String name, String bgClass) {
		this.status = status;
		this.name = name;
		this.bgClass = bgClass;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getBgClass() {
		return this.bgClass;
	}
}

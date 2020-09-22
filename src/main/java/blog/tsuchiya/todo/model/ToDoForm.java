package blog.tsuchiya.todo.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ToDoForm {

	private Long id;
	@NotNull
	@Size(min=1, max=100)
	private String name;
	
	@NotNull
	private Long directoryId;
	
	@NotNull
	private Integer status;
}

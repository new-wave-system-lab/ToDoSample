package blog.tsuchiya.todo.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import blog.tsuchiya.todo.validator.UniqueLogin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ToDoUserForm {
	private Long id;
	
	@NotNull
	@Size(min=4, max=20)
	@UniqueLogin
	private String username;
	
	@NotNull
	@Size(min=4, max=255)
	private String password;
	
	@NotNull
	@Size(min=4, max=20)
	private String nickname;
	
	@NotNull
	@Size(min=2, max=20)
	private String role;
}
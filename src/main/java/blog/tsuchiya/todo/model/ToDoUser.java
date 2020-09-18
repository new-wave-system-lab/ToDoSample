package blog.tsuchiya.todo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ToDoUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min=4, max=20)
	private String username;
	
	@NotNull
	@Size(min=4, max=255)
	private String password;
	
	@NotNull
	@Size(min=4, max=20)
	private String nickname;
	
	@NotNull
	@Max(20)
	private String role;
	
}

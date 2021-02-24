package blog.tsuchiya.todo.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
	@Size(min=4, max=100)
	private String username;
	
	private String password;
	
	@Size(min=4, max=20)
	private String nickname;
	
	@NotNull
	@Size(min=2, max=20)
	private String role;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Directory> directories;

}

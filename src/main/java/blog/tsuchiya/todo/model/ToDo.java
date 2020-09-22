package blog.tsuchiya.todo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ToDo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min=1, max=100)
	private String name;
	
	@NotNull
	private Integer status;
	
	@NotNull
	@ManyToOne
	private ToDoUser user;

	@ManyToOne
	@NotNull
	private Directory directory;
}

package blog.tsuchiya.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import blog.tsuchiya.todo.model.Directory;
import blog.tsuchiya.todo.model.ToDo;
import blog.tsuchiya.todo.model.ToDoUser;
import blog.tsuchiya.todo.repository.ToDoRepository;

@Service
public class ToDoService {

	@Autowired
	private ToDoRepository toDoRep;
	
	public Page<ToDo> getByUserAndDirectory(ToDoUser user, Directory d, Pageable pageable){
		return toDoRep.findByUserAndDirectory(user, d, pageable);
	}
	
	public ToDo getToDo(Long id) {
		return toDoRep.getOne(id);
	}
	
	public void save(ToDo todo) {
		toDoRep.save(todo);
	}
}

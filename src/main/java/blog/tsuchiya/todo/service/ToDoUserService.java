package blog.tsuchiya.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.tsuchiya.todo.model.ToDoUser;
import blog.tsuchiya.todo.repository.ToDoUserRepository;

@Service
public class ToDoUserService {
	
	@Autowired
	private ToDoUserRepository userRep;

	public ToDoUser getToDoUserByName(String name) {
		return userRep.findByUsername(name);
	}
}

package blog.tsuchiya.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	public Page<ToDoUser> getUserByPageable(Pageable pageable){
		return userRep.findAll(pageable);
	}
	
	public void save(ToDoUser user) {
		userRep.save(user);
	}
	
	public void delete(Long id) {
		userRep.deleteById(id);
	}
	
	public ToDoUser getUserById(Long id) {
		return userRep.getOne(id);
	}
}

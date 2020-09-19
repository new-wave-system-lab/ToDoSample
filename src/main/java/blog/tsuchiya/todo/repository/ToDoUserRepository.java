package blog.tsuchiya.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.tsuchiya.todo.model.ToDoUser;

@Repository
public interface ToDoUserRepository extends JpaRepository<ToDoUser, Long> {

	public ToDoUser findByUsername(String username);
	public boolean existsByUsername(String username);
}

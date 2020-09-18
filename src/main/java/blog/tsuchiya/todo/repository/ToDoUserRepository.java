package blog.tsuchiya.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import blog.tsuchiya.todo.model.ToDoUser;

public interface ToDoUserRepository extends JpaRepository<ToDoUser, Long> {

	ToDoUser findByUsername(String username);
	boolean existsByUsername(String username);
}

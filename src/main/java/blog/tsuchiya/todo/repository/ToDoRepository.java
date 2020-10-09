package blog.tsuchiya.todo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.tsuchiya.todo.model.Directory;
import blog.tsuchiya.todo.model.ToDo;
import blog.tsuchiya.todo.model.ToDoUser;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {

	public Page<ToDo> findByUserAndDirectory(ToDoUser user, Directory directory, Pageable pageable);
}

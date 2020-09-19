package blog.tsuchiya.todo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.tsuchiya.todo.model.Directory;

@Repository
public interface DirectoryRepository extends JpaRepository<Directory, Long> {
	
	public List<Directory> findByUserId(Long id);

}

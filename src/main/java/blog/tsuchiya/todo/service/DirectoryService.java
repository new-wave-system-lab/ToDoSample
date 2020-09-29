package blog.tsuchiya.todo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import blog.tsuchiya.todo.model.Directory;
import blog.tsuchiya.todo.repository.DirectoryRepository;

@Service
public class DirectoryService {

	@Autowired
	private DirectoryRepository dirRep;
	
	public List<Directory> getDirectoriesByUserId(Long id){
		return dirRep.findByUserId(id);
	}
	
	public void save(Directory d) {
		dirRep.save(d);
	}
	
	public Directory findDirectoryById(Long id) {
		return dirRep.findById(id).get();
	}
	
	public void delete(Long id) {
		dirRep.deleteById(id);
	}
	
	public void flush() {
		dirRep.flush();
	}
}

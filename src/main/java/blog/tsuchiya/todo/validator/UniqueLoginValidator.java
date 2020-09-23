package blog.tsuchiya.todo.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import blog.tsuchiya.todo.repository.ToDoUserRepository;

public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {

	private final ToDoUserRepository userRep;
	
	public UniqueLoginValidator() {
		this.userRep = null; 
	}
	
	@Autowired
	public UniqueLoginValidator(ToDoUserRepository userRep) {
		this.userRep = userRep; 
	}
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return userRep == null || userRep.findByUsername(value) == null;
	}
	
}

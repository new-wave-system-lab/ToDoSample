package blog.tsuchiya.todo.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import blog.tsuchiya.todo.model.ToDoUser;
import blog.tsuchiya.todo.repository.ToDoUserRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final ToDoUserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ToDoUser toDoUuser = repository.findByUsername(username);
		if (toDoUuser == null) {
			throw new UsernameNotFoundException("User [" + username + "] not found.");
		}
		return createUser(toDoUuser);
	}

	private UserDetails createUser(ToDoUser toDoUuser) {
		Set<GrantedAuthority> auth = new HashSet<>();
		auth.add(new SimpleGrantedAuthority("ROLE_" + toDoUuser.getRole()));
		User user = new User(toDoUuser.getUsername(), toDoUuser.getPassword(), auth);
		return user;
	}

}

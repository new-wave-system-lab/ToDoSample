/**
 * Oidcでソーシャルログインした際に動くUserService。
 * ソーシャルログインの際に対応するユーザがDBになかったら、新たにユーザを登録する。
 */
package blog.tsuchiya.todo.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import blog.tsuchiya.todo.model.LoginUser;
import blog.tsuchiya.todo.model.ToDoUser;
import blog.tsuchiya.todo.repository.ToDoUserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomOidcUserService extends OidcUserService{
	
	private final ToDoUserRepository tdur;
	
	@Override
	public OidcUser loadUser(OidcUserRequest req) {
		var oidcUser = super.loadUser(req);
		var toDoUser = tdur.findByUsername(oidcUser.getName());
		// もし初めてログインするユーザなら、DBに情報を登録する
		if (toDoUser == null) {
			toDoUser = new ToDoUser();
			toDoUser.setUsername(oidcUser.getName());
			toDoUser.setNickname(oidcUser.getFullName());
			toDoUser.setPassword("");
			toDoUser.setRole("USER");
			tdur.save(toDoUser);
		}
		Set<GrantedAuthority> auth = new HashSet<>();
		auth.add(new SimpleGrantedAuthority("ROLE_" + toDoUser.getRole()));

		var user = LoginUser.create(oidcUser, auth);
		return user;
	}

}

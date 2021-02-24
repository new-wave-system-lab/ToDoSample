/**
 * ログインしたユーザの情報を管理する。
 * フォームログイン、OAuth2、Oidcの全てでこのクラスを使う。
 */
package blog.tsuchiya.todo.model;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class LoginUser implements OAuth2User, OidcUser, UserDetails {
	private static final long serialVersionUID = 1L;

	/** コンストラクタは外部からの使用不可、createメソッドでのみ作れる */
	private LoginUser() {}
	
	/**
	 * ToDoUserからLoginUserを作るファクトリーメソッド
	 * @param toDoUser
	 * @param authorities
	 * @return
	 */
	public static LoginUser create(ToDoUser toDoUser, Collection<? extends GrantedAuthority> authorities) {
		var user = new LoginUser();
		user.name = toDoUser.getUsername();
		user.password = toDoUser.getPassword();
		user.authorities = authorities;
		return user;
	}
	
	/**
	 * OidcUserからLoginUserを作るファクトリーメソッド
	 * @param oicdUSer
	 * @param authorities
	 * @return
	 */
	public static LoginUser create(OidcUser oidcUser, Collection<? extends GrantedAuthority> authorities) {
		var user = new LoginUser();
		user.name = oidcUser.getName();
		user.authorities = authorities;
		user.idToken = oidcUser.getIdToken();
		user.userInfo = oidcUser.getUserInfo();
		return user;
	}
	private Map<String, Object> attributes;
	private Collection<? extends GrantedAuthority> authorities;
	private String name;
	private String password;
	private Map<String, Object> claims;
	private OidcUserInfo userInfo;
	private OidcIdToken idToken;
	
	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.name;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public Map<String, Object> getClaims() {
		return this.claims;
	}

	@Override
	public OidcUserInfo getUserInfo() {
		return this.userInfo;
	}

	@Override
	public OidcIdToken getIdToken() {
		return this.idToken;
	}

}

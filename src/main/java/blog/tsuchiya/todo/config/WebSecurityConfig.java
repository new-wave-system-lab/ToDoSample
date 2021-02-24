package blog.tsuchiya.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import blog.tsuchiya.todo.Role;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsService uds;
	private final OidcUserService oidcUserService;
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> o2us;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webjars/**", "/css/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// /だけ無条件で表示、それ以外は全部認証が必要
		// /admin/以下は管理者権限を持っている場合のみ表示
		http.authorizeRequests().antMatchers("/").permitAll()
			.antMatchers("/admin/**").hasRole(Role.ADMIN.name())
			.anyRequest().authenticated();

		// ログイン処理は/で行う
		http.formLogin()
			.loginProcessingUrl("/")
			.loginPage("/")
			.failureUrl("/")
			.usernameParameter("username")
			.passwordParameter("password")
			.defaultSuccessUrl("/todo", true);

		// ログアウト処理
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutUrl("/logout")
				.logoutSuccessUrl("/");

		// Oauth2設定
		http.oauth2Login()
			.defaultSuccessUrl("/todo", true)
			.userInfoEndpoint()
				// OAuth2とOidcのユーザサービス設定
				.oidcUserService(oidcUserService)
				.userService(o2us);
		
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// ログイン処理時のユーザー情報をDBから取得する
		auth.userDetailsService(uds).passwordEncoder(passwordEncoder());
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}

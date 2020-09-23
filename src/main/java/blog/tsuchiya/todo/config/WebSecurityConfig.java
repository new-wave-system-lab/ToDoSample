package blog.tsuchiya.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import blog.tsuchiya.todo.Role;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private final UserDetailsService uds;
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
		http
			.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/admin/**").hasRole(Role.ADMIN.name())
				.anyRequest().authenticated();

		// ログイン処理は/で行う
		http.formLogin()
			.loginProcessingUrl("/")
			.loginPage("/")
			.failureUrl("/")
			.usernameParameter("userId")
			.passwordParameter("password")
			.defaultSuccessUrl("/todo", true);
		
		// ログアウト処理
		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutUrl("/logout")
			.logoutSuccessUrl("/");

	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		// ログイン処理時のユーザー情報をDBから取得する
		auth.userDetailsService(uds).passwordEncoder(passwordEncoder());
	}

}

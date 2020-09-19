package blog.tsuchiya.todo.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import blog.tsuchiya.todo.model.Directory;
import blog.tsuchiya.todo.model.ToDoUser;
import blog.tsuchiya.todo.service.DirectoryService;
import blog.tsuchiya.todo.service.ToDoUserService;

@Component
public class DirectoryIntercepter implements HandlerInterceptor {
	private final DirectoryService dirService;
	private final ToDoUserService userService;

	@Autowired
	public DirectoryIntercepter(DirectoryService dirService, ToDoUserService userService) {
		this.dirService = dirService;
		this.userService = userService;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// ログインユーザ名からUserIdを取得
		var context = SecurityContextHolder.getContext();
		var auth = context.getAuthentication();
		// Loginしていないとauthはnullなので、処理を分ける
		List<Directory> dirs;
		if (auth==null) {
			// ログインしていない場合はディレクトリは長さ０で定義しておく
			dirs = new ArrayList<Directory>();
		} else {
			//そうじゃなかったら、DBから取り出して格納する
			var principal = auth.getPrincipal();
			User user = (User) principal;
			ToDoUser todoUser = userService.getToDoUserByName(user.getUsername());
			 dirs = dirService.getDirectoriesByUserId(todoUser.getId());
		}
		if (modelAndView != null) {
			modelAndView.addObject("directories", dirs);
		}
	}
}

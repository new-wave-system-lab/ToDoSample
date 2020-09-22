package blog.tsuchiya.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import blog.tsuchiya.todo.model.ToDo;
import blog.tsuchiya.todo.model.ToDoForm;
import blog.tsuchiya.todo.model.ToDoUser;
import blog.tsuchiya.todo.service.ToDoService;
import blog.tsuchiya.todo.service.ToDoUserService;

@Controller
@RequestMapping("/todo")
public class ToDoController {
	@Autowired
	private ToDoUserService userService;

	@Autowired
	private ToDoService toDoService;

	@GetMapping("/{id}/edit")
	public String getTodoEdit(@PathVariable Long id, Model model) throws IllegalAccessException {
		// ログインユーザ名からToDoUserを取得
		ToDoUser todoUser = getToDoUser();

		ToDo todo = toDoService.getToDo(id);
		// ログイン中のユーザが保持するToDoか確認
		Long currentUserId = todoUser.getId();
		Long todoUserId = todo.getUser().getId();
		if (!currentUserId.equals(todoUserId)) {
			throw new IllegalAccessException("アクセス権を持たないToDoを編集しようとしました。");
		}
		
		ToDoForm form = new ToDoForm();
		form.setId(id);
		form.setName(todo.getName());
		
		// 各種パラメータをモデルに格納
		model.addAttribute("toDoForm", form);
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "todo/edit :: edit");
		return "layout/layout";

	}
	@PostMapping("/{id}/edit")
	public String postDirectoryEdit(@Validated @ModelAttribute ToDoForm toDoForm, BindingResult result,
			Model model) throws IllegalAccessException {
		if (result.hasErrors()) {
			model.addAttribute("contents", "layout/main :: main");
			model.addAttribute("main", "todo/edit :: edit");
			return "layout/layout";
		}
		// ログインユーザ名からToDoUserを取得
		ToDoUser todoUser = getToDoUser();

		ToDo todo = toDoService.getToDo(toDoForm.getId());
		// ログイン中のユーザが保持するToDoか確認
		Long currentUserId = todoUser.getId();
		Long todoUserId = todo.getUser().getId();
		if (!currentUserId.equals(todoUserId)) {
			throw new IllegalAccessException("アクセス権を持たないToDoを編集しようとしました。");
		}

		todo.setName(toDoForm.getName());

		// DBにセーブ
		toDoService.save(todo);
		return "redirect:/todo";
	}

	/**
	 * ログインユーザ名からToDoUserを取得 ユーザー名はセッションから取り出すので、引数にして渡す必要はない
	 * 
	 * @return ToDoUser
	 */
	private ToDoUser getToDoUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ToDoUser todoUser = userService.getToDoUserByName(user.getUsername());
		return todoUser;
	}

}

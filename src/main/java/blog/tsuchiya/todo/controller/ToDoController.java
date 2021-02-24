package blog.tsuchiya.todo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import blog.tsuchiya.todo.ToDoStatus;
import blog.tsuchiya.todo.model.Directory;
import blog.tsuchiya.todo.model.ToDo;
import blog.tsuchiya.todo.model.ToDoForm;
import blog.tsuchiya.todo.model.ToDoUser;
import blog.tsuchiya.todo.service.DirectoryService;
import blog.tsuchiya.todo.service.ToDoService;
import blog.tsuchiya.todo.service.ToDoUserService;

@Controller
@RequestMapping("/todo")
public class ToDoController {
	@Autowired
	private ToDoUserService userService;

	@Autowired
	private ToDoService toDoService;
	
	@Autowired
	private DirectoryService dirService;

	@GetMapping("/create")
	public String getToDoCreate(@ModelAttribute ToDoForm toDoForm, Model model) {
		// このユーザーが持っているディレクトリ一覧を取得
		List<Directory> directories = getDirectories();

		model.addAttribute("statuses", ToDoStatus.values());
		model.addAttribute("directories", directories);
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "todo/create :: create");
		return "layout/layout";
	}
	@PostMapping("/create")
	public String postToDoCreatet(@Validated @ModelAttribute ToDoForm toDoForm, BindingResult result,
			Model model) throws IllegalAccessException {
		if (result.hasErrors()) {
			model.addAttribute("statuses", ToDoStatus.values());
			model.addAttribute("contents", "layout/main :: main");
			model.addAttribute("main", "todo/create :: create");
			return "layout/layout";
		}
		// ログインユーザ名からToDoUserを取得
		ToDoUser todoUser = getToDoUser();

		// ログイン中のユーザが保持するToDoか確認
		Long currentUserId = todoUser.getId();
		Directory dir = dirService.findDirectoryById(toDoForm.getDirectoryId());
		// ログイン中のユーザーが保持するディレクトリか確認
		Long dirUserId = dir.getUser().getId();
		if (!currentUserId.equals(dirUserId)) {
			throw new IllegalAccessException("アクセス権を持たないディレクトリを編集しようとしました。");
		}

		// ToDoの組み立て
		ToDo todo = new ToDo();
		todo.setName(toDoForm.getName());
		todo.setDirectory(dir);
		todo.setStatus(toDoForm.getStatus());
		todo.setUser(todoUser);

		// DBにセーブ
		toDoService.save(todo);
		return "redirect:/todo";
	}

	private List<Directory> getDirectories() {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ToDoUser todoUser = userService.getToDoUserByName(user.getUsername());
		List<Directory> directories = dirService.getDirectoriesByUserId(todoUser.getId());
		return directories;
	}

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
		form.setDirectoryId(todo.getDirectory().getId());
		
		// このユーザーが持っているディレクトリ一覧を取得
		List<Directory> directories = getDirectories();

		// 各種パラメータをモデルに格納
		model.addAttribute("directories", directories);
		model.addAttribute("toDoForm", form);
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "todo/edit :: edit");
		return "layout/layout";

	}
	@PostMapping("/{id}/edit")
	public String postToDoEdit(@Validated @ModelAttribute ToDoForm toDoForm, BindingResult result,
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

		Directory dir = dirService.findDirectoryById(toDoForm.getDirectoryId());
		// ログイン中のユーザーが保持するディレクトリか確認
		Long dirUserId = dir.getUser().getId();
		if (!currentUserId.equals(dirUserId)) {
			throw new IllegalAccessException("アクセス権を持たないディレクトリを編集しようとしました。");
		}

		// ToDoの組み立て
		todo.setName(toDoForm.getName());
		todo.setDirectory(dir);

		// DBにセーブ
		toDoService.save(todo);
		return "redirect:/todo";
	}
	@GetMapping("/{id}/delete")
	public String getTodoDelete(@PathVariable Long id, Model model) throws IllegalAccessException {
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
		form.setId(todo.getId());
		form.setDirectoryId(todo.getDirectory().getId());
		form.setName(todo.getName());
		form.setStatus(todo.getStatus());

		// 各種パラメータをモデルに格納
		model.addAttribute("toDoForm", form);
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "todo/delete :: delete");
		return "layout/layout";

	}
	@PostMapping("/{id}/delete")
	public String postToDoDelete(@Validated @ModelAttribute ToDoForm toDoForm, BindingResult result,
			Model model) throws IllegalAccessException {
		if (result.hasErrors()) {
			model.addAttribute("contents", "layout/main :: main");
			model.addAttribute("main", "todo/delete :: delete");
			return "layout/layout";
		}
		// ログインユーザ名からToDoUserを取得
		ToDoUser todoUser = getToDoUser();

		// DBからToDoを取得
		ToDo todo = toDoService.getToDo(toDoForm.getId());

		// ログイン中のユーザが保持するディレクトリか確認
		Long currentUserId = todoUser.getId();
		Long dirUserId = todo.getUser().getId();
		if (!currentUserId.equals(dirUserId)) {
			throw new IllegalAccessException("アクセス権を持たないToDoを編集しようとしました。");
		}

		// DBから削除
		toDoService.delete(todo.getId());
		return "redirect:/todo";
	}

	/**
	 * ログインユーザ名からToDoUserを取得 ユーザー名はセッションから取り出すので、引数にして渡す必要はない
	 * 
	 * @return ToDoUser
	 */
	private ToDoUser getToDoUser() {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ToDoUser todoUser = userService.getToDoUserByName(user.getUsername());
		return todoUser;
	}

}

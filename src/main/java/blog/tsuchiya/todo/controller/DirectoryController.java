package blog.tsuchiya.todo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import blog.tsuchiya.todo.model.Directory;
import blog.tsuchiya.todo.model.DirectoryForm;
import blog.tsuchiya.todo.model.ToDo;
import blog.tsuchiya.todo.model.ToDoUser;
import blog.tsuchiya.todo.service.DirectoryService;
import blog.tsuchiya.todo.service.ToDoService;
import blog.tsuchiya.todo.service.ToDoUserService;

@Controller
@RequestMapping("/dir")
public class DirectoryController {

	@Autowired
	private DirectoryService dirService;
	
	@Autowired
	private ToDoUserService userService;

	@Autowired
	private ToDoService toDoService;
	
	@GetMapping("/create")
	public String getDirectoryCreate(@ModelAttribute DirectoryForm directoryForm, Model model) {
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "directory/create :: create");
		return "layout/layout";
	}

	@PostMapping("/create")
	public String postDirectoryCreate(@Validated @ModelAttribute DirectoryForm directoryForm, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("contents", "layout/main :: main");
			model.addAttribute("main", "directory/create :: create");
			return "layout/layout";
		}
		// ログインユーザ名からToDoUserを取得
		ToDoUser todoUser = getToDoUser();
		
		// Directoryを組み立て
		Directory d = new Directory();
		d.setName(directoryForm.getName());
		d.setUser(todoUser);
		
		// DBにセーブ
		dirService.save(d);
		return "redirect:/todo";
	}

	@GetMapping("/{id}")
	public String getDirectoryId(@PathVariable Long id, Model model, Pageable pageable) {
		// idからディレクトリを取得
		Directory d = dirService.findDirectoryById(id);
		// ログインユーザ名からToDoUserを取得
		ToDoUser todoUser = getToDoUser();
		Page<ToDo> toDos = toDoService.getByUserAndDirectory(todoUser, d, pageable);
		
		// 各種パラメータをモデルに格納
		model.addAttribute("directory", d);
		model.addAttribute("toDos", toDos);
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "directory/list :: list");
		return "layout/layout";
	}
	/**
	 * ログインユーザ名からToDoUserを取得
	 * ユーザー名はセッションから取り出すので、引数にして渡す必要はない
	 * @return ToDoUser
	 */
	private ToDoUser getToDoUser() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ToDoUser todoUser = userService.getToDoUserByName(user.getUsername());
		return todoUser;
	}
	
}

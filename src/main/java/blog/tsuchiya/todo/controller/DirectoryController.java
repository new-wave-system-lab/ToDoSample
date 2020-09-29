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

import blog.tsuchiya.todo.ToDoStatus;
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
		System.out.println("------------into create-------------");
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
	public String getDirectoryId(@PathVariable Long id, Model model, Pageable pageable) throws IllegalAccessException {
		// idからディレクトリを取得
		Directory d = dirService.findDirectoryById(id);
		// ログインユーザ名からToDoUserを取得
		ToDoUser todoUser = getToDoUser();
		Page<ToDo> toDos = toDoService.getByUserAndDirectory(todoUser, d, pageable);

		// ログイン中のユーザが保持するディレクトリか確認
		Long currentUserId = todoUser.getId();
		Long dirUserId = d.getUser().getId();
		if (!currentUserId.equals(dirUserId)) {
			throw new IllegalAccessException("アクセス権を持たないディレクトリを編集しようとしました。");
		}
		// 各種パラメータをモデルに格納
		model.addAttribute("statuses", ToDoStatus.values());
		model.addAttribute("directory", d);
		model.addAttribute("toDos", toDos);
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "directory/list :: list");
		return "layout/layout";
	}

	@GetMapping("/{id}/edit")
	public String getDirectoryEdit(@PathVariable Long id, Model model) throws IllegalAccessException {
		// ログインユーザ名からToDoUserを取得
		ToDoUser todoUser = getToDoUser();

		// idからディレクトリを取得
		Directory d = dirService.findDirectoryById(id);

		// ログイン中のユーザが保持するディレクトリか確認
		Long currentUserId = todoUser.getId();
		Long dirUserId = d.getUser().getId();
		if (!currentUserId.equals(dirUserId)) {
			throw new IllegalAccessException("アクセス権を持たないディレクトリを編集しようとしました。");
		}

		DirectoryForm form = new DirectoryForm();
		form.setId(d.getId());
		form.setName(d.getName());

		model.addAttribute("directoryForm", form);
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "directory/edit :: edit");
		return "layout/layout";
	}

	@PostMapping("/{id}/edit")
	public String postDirectoryEdit(@Validated @ModelAttribute DirectoryForm directoryForm, BindingResult result,
			Model model) throws IllegalAccessException {
		if (result.hasErrors()) {
			model.addAttribute("contents", "layout/main :: main");
			model.addAttribute("main", "directory/edit :: edit");
			return "layout/layout";
		}
		// ログインユーザ名からToDoUserを取得
		ToDoUser todoUser = getToDoUser();

		// DBからディレクトリを取得
		Directory d = dirService.findDirectoryById(directoryForm.getId());

		// ログイン中のユーザが保持するディレクトリか確認
		Long currentUserId = todoUser.getId();
		Long dirUserId = d.getUser().getId();
		if (!currentUserId.equals(dirUserId)) {
			throw new IllegalAccessException("アクセス権を持たないディレクトリを編集しようとしました。");
		}

		d.setName(directoryForm.getName());
		d.setUser(todoUser);

		// DBにセーブ
		dirService.save(d);
		return "redirect:/todo";
	}

	@GetMapping("/{id}/delete")
	public String getDirectoryDelete(@PathVariable Long id, Model model) throws IllegalAccessException {
		// ログインユーザ名からToDoUserを取得
		ToDoUser todoUser = getToDoUser();

		// idからディレクトリを取得
		Directory d = dirService.findDirectoryById(id);

		// ログイン中のユーザが保持するディレクトリか確認
		Long currentUserId = todoUser.getId();
		Long dirUserId = d.getUser().getId();
		if (!currentUserId.equals(dirUserId)) {
			throw new IllegalAccessException("アクセス権を持たないディレクトリを編集しようとしました。");
		}

		DirectoryForm form = new DirectoryForm();
		form.setId(d.getId());
		form.setName(d.getName());

		model.addAttribute("directoryForm", form);
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "directory/delete :: delete");
		return "layout/layout";
	}

	@PostMapping("/{id}/delete")
	public String postDirectoryDelete(@Validated @ModelAttribute DirectoryForm directoryForm, BindingResult result,
			Model model) throws IllegalAccessException {
		if (result.hasErrors()) {
			model.addAttribute("contents", "layout/main :: main");
			model.addAttribute("main", "directory/delete :: delete");
			return "layout/layout";
		}
		// ログインユーザ名からToDoUserを取得
		ToDoUser todoUser = getToDoUser();

		// DBからディレクトリを取得
		Directory d = dirService.findDirectoryById(directoryForm.getId());

		// ログイン中のユーザが保持するディレクトリか確認
		Long currentUserId = todoUser.getId();
		Long dirUserId = d.getUser().getId();
		if (!currentUserId.equals(dirUserId)) {
			throw new IllegalAccessException("アクセス権を持たないディレクトリを編集しようとしました。");
		}

		// DBから削除
		dirService.delete(d.getId());
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

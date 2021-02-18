package blog.tsuchiya.todo.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import blog.tsuchiya.todo.Role;
import blog.tsuchiya.todo.model.ToDoUser;
import blog.tsuchiya.todo.model.ToDoUserForm;
import blog.tsuchiya.todo.service.ToDoUserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private ToDoUserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/list")
	public String getAdminList(Model model, Pageable pageable) {
		Page<ToDoUser> users = userService.getUserByPageable(pageable);
		// 各種パラメータをモデルに格納
		model.addAttribute("users", users);
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "admin/list :: list");
		return "layout/layout";

	}

	@GetMapping("/create")
	public String getAdminCreate(@ModelAttribute ToDoUserForm toDoUserForm, Model model) {
		// 各種パラメータをモデルに格納
		model.addAttribute("roles", Arrays.asList(Role.values()));
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "admin/form :: form");
		return "layout/layout";

	}

	@GetMapping("/{id}/edit")
	public String getAdminEdit(@PathVariable Long id, @ModelAttribute ToDoUserForm toDoUserForm, Model model) {
		// idからユーザー情報を取得
		ToDoUser user = userService.getUserById(id);

		// DBのユーザー情報をFormに格納
		toDoUserForm.setId(id);
		toDoUserForm.setUsername(user.getUsername());
		toDoUserForm.setNickname(user.getNickname());
		toDoUserForm.setRole(user.getRole());

		// 各種パラメータをモデルに格納
		model.addAttribute("roles", Role.values());
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "admin/form :: form");
		return "layout/layout";

	}

	@PostMapping("/process")
	public String postAdminProcess(@Validated @ModelAttribute ToDoUserForm toDoUserForm, BindingResult result,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("roles", Role.values());
			model.addAttribute("contents", "layout/main :: main");
			model.addAttribute("main", "admin/form :: form");
			return "layout/layout";
		}
		// FormからToDoUserインスタンスを作成
		ToDoUser user = new ToDoUser();
		user.setId(toDoUserForm.getId());
		user.setUsername(toDoUserForm.getUsername());
		user.setPassword(passwordEncoder.encode(toDoUserForm.getPassword()));
		user.setNickname(toDoUserForm.getNickname());
		user.setRole(toDoUserForm.getRole());

		userService.save(user);

		return "redirect:/admin/list";
	}

	@GetMapping("/{id}/delete")
	public String getAdminDelete(@PathVariable Long id, Model model) {
		// idからユーザー情報を取得
		ToDoUser user = userService.getUserById(id);

		// 各種パラメータをモデルに格納
		model.addAttribute("username", user.getUsername());
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "admin/delete :: delete");
		return "layout/layout";

	}

	@PostMapping("/{id}/delete")
	public String postAdminDelete(@PathVariable Long id, Model model) {
		userService.delete(id);
		return "redirect:/admin/list";
	}
}

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import blog.tsuchiya.todo.model.Directory;
import blog.tsuchiya.todo.model.DirectoryForm;
import blog.tsuchiya.todo.model.ToDoUser;
import blog.tsuchiya.todo.service.DirectoryService;
import blog.tsuchiya.todo.service.ToDoUserService;

@Controller
@RequestMapping("/dir")
public class DirectoryController {

	@Autowired
	private DirectoryService dirService;
	
	@Autowired
	private ToDoUserService userService;

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
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ToDoUser todoUser = userService.getToDoUserByName(user.getUsername());
		
		// Directoryを組み立て
		Directory d = new Directory();
		d.setName(directoryForm.getName());
		d.setUser(todoUser);
		dirService.save(d);
		return "redirect:/todo";
	}
}

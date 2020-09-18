package blog.tsuchiya.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@GetMapping("/")
	public String getRoot(Model model) {
		model.addAttribute("contents", "login :: login");
		return "layout/layout";
	}
}

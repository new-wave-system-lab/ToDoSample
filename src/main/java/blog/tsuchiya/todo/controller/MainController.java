package blog.tsuchiya.todo.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MainController {
	
	@GetMapping("/")
	public String getRoot(Model model, HttpServletRequest request) {
		String host = request.getHeader("host");
		String subDomain;
		int index = host.indexOf(".");
		if(index < 0) {
			subDomain = "";
		} else {
			subDomain = host.substring(0, index);
		}
		log.info("subdomain : {}", subDomain);
		model.addAttribute("contents", "login :: login");
		return "layout/layout";
	}
	
	@GetMapping("/todo")
	public String getTodo(Model model) {
		// 表示する画面を指定
		model.addAttribute("contents", "layout/main :: main");
		model.addAttribute("main", "empty :: empty");
		return "layout/layout";
	}
}
